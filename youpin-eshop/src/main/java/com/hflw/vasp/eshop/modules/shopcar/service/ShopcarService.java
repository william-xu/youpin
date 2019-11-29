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
import com.hflw.vasp.modules.entity.Store;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
     * @param goodsId
     */
    public void addToShopcar(Long goodsId) {

    }

    /**
     * 更新购物车
     */
    public void updateShopcar() {

    }

    /**
     * 从购物车删除商品
     */
    public void delFromShopcar(Long[] goodsIds) {

    }

    public void save(ShopcarModel shopcarModel) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", userUtils.getSessionUser().getId());
        params.put("goodsId", shopcarModel.getGoodsId());
        params.put("enableStatus", Constants.ENABLE_STATUS_EFFECT);//正常
        List<Shopcar> shopcarList = null;//shopcarMapper.search(params);
        if (CollectionUtils.isNotEmpty(shopcarList)) {
            //存在相同商品
            Shopcar shopcar = shopcarList.get(0);
            logger.info("新增商品数量:" + shopcarModel.getGoodsNum() + "购物车相同商品数量:" + shopcar.getGoodsNum());
            int goodsNum = shopcarModel.getGoodsNum() + shopcar.getGoodsNum();
            shopcar.setGoodsNum(goodsNum > 10 ? 10 : goodsNum);
//            shopcarMapper.updateByPrimaryKeySelective(shopcar);
        } else {
            //没有相同商品
            logger.info("新增商品数量:" + shopcarModel.getGoodsNum());
            Shopcar shopcar = new Shopcar();
            BeanUtils.copyProperties(shopcarModel, shopcar);
            int goodsNum = shopcar.getGoodsNum();
            shopcar.setGoodsNum(goodsNum > 10 ? 10 : goodsNum);
            shopcar.setEnableStatus(Constants.ENABLE_STATUS_EFFECT);
            shopcar.setUserId(userUtils.getSessionUser().getId());
//            shopcarMapper.insertSelective(shopcar);
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


    public void updateById(Shopcar shopcar) {
        int goodsNum = shopcar.getGoodsNum();
        shopcar.setGoodsNum(goodsNum > 10 ? 10 : goodsNum);
//        shopcarMapper.updateByPrimaryKeySelective(shopcar);
    }

    public Shopcar selectByPrimaryKey(Integer id) {
//        return shopcarMapper.selectByPrimaryKey(id);
        return null;
    }

    public int delete(Shopcar shopcar) {
        shopcar.setDelFlag(Constants.IS_DEL);
//        return shopcarMapper.updateByPrimaryKeySelective(shopcar);
        return 1;
    }

    public Shopcar search(Map<String, Object> shopParams) {
        return null;
    }
}
