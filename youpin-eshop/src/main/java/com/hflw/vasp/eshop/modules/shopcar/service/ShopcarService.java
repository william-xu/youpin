package com.hflw.vasp.eshop.modules.shopcar.service;

import com.hflw.vasp.eshop.common.constant.Constants;
import com.hflw.vasp.eshop.common.utils.UserUtils;
import com.hflw.vasp.eshop.modules.common.controller.CommonController;
import com.hflw.vasp.eshop.modules.shopcar.model.ShopcarModel;
import com.hflw.vasp.modules.dao.IGoodsDao;
import com.hflw.vasp.modules.dao.IShopcarDao;
import com.hflw.vasp.modules.entity.Customer;
import com.hflw.vasp.modules.entity.Goods;
import com.hflw.vasp.modules.entity.Shopcar;
import com.hflw.vasp.eshop.modules.shopcar.model.ShopcarDetail;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


@Service
public class ShopcarService {

    private static final Logger logger = LoggerFactory.getLogger(ShopcarService.class);

    @Resource
    private IShopcarDao shopcarDao;

    @Resource
    private IGoodsDao goodsDao;

    @Autowired
    private UserUtils userUtils;

    /**
     * 购物车列表
     *
     * @param userId
     * @return
     */
    public List<ShopcarDetail> list(Long userId) {
        List<Shopcar> list = shopcarDao.findAllByUserId(userId);

        List<ShopcarDetail> detailList = new ArrayList<>(list.size());
        for (Shopcar shopcar : list) {
            Goods goods = goodsDao.getOne(shopcar.getGoodsId());

            ShopcarDetail detail = new ShopcarDetail();
            detail.setUserId(shopcar.getUserId());
            detail.setName(goods.getName());
            detail.setGoodsId(shopcar.getGoodsId());
            detail.setShopPrice(goods.getShopPrice());
            detail.setGoodsNum(shopcar.getGoodsNum());
            detailList.add(detail);
        }
        return detailList;
    }

    /**
     * 初始化购物车，没登录变成登录情况
     *
     * @param shopcarModel
     */
    public void initShopcar(ShopcarModel shopcarModel) {

    }

    /**
     * 添加商品到购物车
     *
     * @param model
     */
    public void addToShopcar(ShopcarModel model) {
        boolean existFlag = false;
        List<Shopcar> existShopcarList = shopcarDao.findAllByUserId(model.getUserId());
        for (Shopcar shopcar : existShopcarList) {
            if (shopcar.getGoodsId().equals(model.getGoodsId())) {
                shopcar.setGoodsNum(shopcar.getGoodsNum() + model.getGoodsNum());
                existFlag = true;
                shopcarDao.save(shopcar);
                break;
            }
        }
        if (!existFlag) {
            Shopcar shopcar = new Shopcar();
            shopcar.setUserId(model.getUserId());
            shopcar.setGoodsId(model.getGoodsId());
            shopcar.setGoodsNum(model.getGoodsNum());
            shopcar.setCreateTime(new Date());
            shopcarDao.save(shopcar);
        }
    }

    /**
     * 更新购物车
     */
    public void updateShopcar(ShopcarModel model) {
        Shopcar shopcar = shopcarDao.findByUserIdAndGoodsId(model.getUserId(), model.getGoodsId());
        shopcar.setGoodsNum(model.getGoodsNum());
        shopcar.setUpdateTime(new Date());
        shopcarDao.save(shopcar);
    }

    /**
     * 从购物车删除商品
     *
     * @param ids 购物车id
     */
    public void delFromShopcar(Long[] ids) {
        for (Long id : ids) {
            shopcarDao.deleteById(id);
        }
    }

    public void saveList(List<ShopcarModel> list) {
        if (!CollectionUtils.isEmpty(list)) {
            for (ShopcarModel shopcarModel : list) {
                Customer sessionUser = userUtils.getSessionUser();
                HashMap<String, Object> params = new HashMap<>();
                params.put("userId", sessionUser.getId());
                params.put("goodsId", shopcarModel.getGoodsId());
                params.put("delFlag", 0);//未删除
                params.put("enableStatus", 1);//正常
                List<Shopcar> shopcarList = null;//shopcarMapper.search(params);
                if (CollectionUtils.isNotEmpty(shopcarList)) {
                    //存在相同商品
                    Shopcar shopcar = shopcarList.get(0);
                    logger.info("新增商品数量:" + shopcarModel.getGoodsNum() + "购物车相同商品数量:" + shopcar.getGoodsNum());
                    int goodsNum = shopcarModel.getGoodsNum() + shopcar.getGoodsNum();
                    shopcar.setGoodsNum(goodsNum > 10 ? 10 : goodsNum);
//                    shopcarMapper.updateByPrimaryKeySelective(shopcar);
                } else {
                    //没有相同商品
                    logger.info("新增商品数量:" + shopcarModel.getGoodsNum());
                    Shopcar target = new Shopcar();
                    BeanUtils.copyProperties(shopcarModel, target);
                    int goodsNum = target.getGoodsNum();
                    target.setGoodsNum(goodsNum > 10 ? 10 : goodsNum);
                    target.setUserId(sessionUser.getId());
                    target.setEnableStatus(1);
//                    shopcarMapper.insert(target);
                }
            }
        }
    }
}
