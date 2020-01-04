package com.hflw.vasp.admin.modules.youpincard.service;

import com.hflw.vasp.admin.modules.youpincard.dto.YoupinCardSearch;
import com.hflw.vasp.admin.modules.youpincard.model.YoupinCardListModel;
import com.hflw.vasp.modules.dao.IYoupinCardDao;
import com.hflw.vasp.modules.entity.YoupinCard;
import com.hflw.vasp.utils.ReflectUtils;
import com.hflw.vasp.utils.StringUtils;
import com.hflw.vasp.web.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;

@Slf4j
@Service
public class YoupinCardService {

    @Autowired
    private IYoupinCardDao youpinCardDao;

    @Autowired
    private EntityManager entityManager;

    public Pagination<YoupinCardListModel> search(final YoupinCardSearch search, cn.hutool.db.Page page) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append(" select o.id, ");
        sql.append("         o.user_id userId, ");
        sql.append("         o.order_no orderNo, ");
        sql.append("         o.status, ");
        sql.append("         ypc.status ypcStatus, ");
        sql.append("         o.pay_amount payAmount, ");
        sql.append("         o.promo_code promoCode, ");
        sql.append("         c.realname name, ");
        sql.append("         c.phone tel, ");
        sql.append("         o.create_time createTime ");
        sql.append(" FROM d_order o ");
        sql.append(" left join d_customer c on c.id = o.user_id ");
        sql.append(" left join d_youpin_card ypc on ypc.user_id = o.user_id ");
        sql.append(" where o.del_flag = 0 and o.type= 1 ");

        if (StringUtils.isNotBlank(search.getMerchantId())) {
            sql.append(" and o.promo_code = '").append(search.getMerchantId()).append("' ");
        }
        if (null != search.getOrderStatus()) {
            sql.append(" and o.status=").append(search.getOrderStatus());
        }
        if (StringUtils.isNotBlank(search.getOrderNo())) {
            sql.append(" and o.order_no like '%").append(search.getOrderNo()).append("%' ");
        }
        if (StringUtils.isNotBlank(search.getPhone())) {
            sql.append(" and c.phone like '%").append(search.getPhone()).append("%' ");
        }
        if (StringUtils.isNotBlank(search.getSDate()) && StringUtils.isNotBlank(search.getEDate())) {
            sql.append(" and DATE(o.create_time) between '").append(search.getSDate()).append("' and '").append(search.getEDate()).append("' ");
        }
        if (null != search.getYpcStatus()) {
            if (1 == search.getYpcStatus()) {
                sql.append(" and ypc.status=").append(search.getYpcStatus());
                sql.append(" and now() between ypc.effective_date and ypc.expiration_date ");
            } else {
                sql.append(" and (ypc.status is null or ypc.status <> 1) ");
            }
        }
        sql.append(" order by o.id desc");

        String sqlStr = sql.toString();

        String count = "SELECT count(1) ";
        String substring = sqlStr.substring(0, sql.indexOf("FROM"));

        String countSql = sqlStr.replace(substring, count);

        Query query = entityManager.createNativeQuery(sqlStr);
        Query countQuery = entityManager.createNativeQuery(countSql);

        //设置分页
        if (page != null) {
            query.setFirstResult((page.getPageNumber() - 1) * 10);
            query.setMaxResults(page.getPageSize());
        }

        long total = ((BigInteger) countQuery.getSingleResult()).longValue();

        List list = query.getResultList();

        List<YoupinCardListModel> modelList = ReflectUtils.castEntity(list, YoupinCardListModel.class);
        for (YoupinCardListModel model : modelList) {
            YoupinCard card = youpinCardDao.findByUserId(model.getUserId().longValue());
            model.setYpcStatus(verifyValid(card) ? (byte) 1 : (byte) 2);
        }
        return new Pagination<>(total, modelList);
    }

    /**
     * 验证优品卡是否有效
     *
     * @param card
     * @return
     */
    public boolean verifyValid(YoupinCard card) {
        if (card == null) return false;
        if (card.getStatus() == null || card.getStatus() != 1) return false;

        Calendar c = Calendar.getInstance();
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(card.getEffectiveDate());
        c2.setTime(card.getExpirationDate());
        return (c.after(c1) && c.before(c2)); //在权益有效期内
    }

}
