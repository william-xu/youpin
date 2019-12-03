package com.hflw.vasp.eshop.modules.youpincard.service;

import com.alibaba.fastjson.JSONObject;
import com.hflw.vasp.modules.dao.IYoupinCardDao;
import com.hflw.vasp.modules.entity.YoupinCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;


@Service
public class YoupinCardService {

    private static final Logger logger = LoggerFactory.getLogger(YoupinCardService.class);

    @Autowired
    private IYoupinCardDao youpinCardDao;

    public YoupinCard findByUserId(Long userId) {
        return youpinCardDao.findByUserId(userId);
    }

    /**
     * 激活优品卡
     *
     * @param userId
     * @return
     */
    public void active(Long userId) {
        LocalDateTime effectiveDate = LocalDateTime.now();
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(365);

        Instant instant1 = effectiveDate.atZone(ZoneId.systemDefault()).toInstant();
        Instant instant2 = expirationDate.atZone(ZoneId.systemDefault()).toInstant();

        YoupinCard youpinCard = youpinCardDao.findByUserId(userId);
        if (youpinCard == null) {
            youpinCard = new YoupinCard();
            youpinCard.setCreateTime(Date.from(instant1));
        }
        youpinCard.setUserId(userId);
        youpinCard.setStatus((byte) 1);
        youpinCard.setEffectiveDate(Date.from(instant1));
        youpinCard.setExpirationDate(Date.from(instant2));
        youpinCard.setUpdateTime(youpinCard.getEffectiveDate());
        youpinCardDao.save(youpinCard);
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
        logger.info("权益：" + JSONObject.toJSONString(card));
        return (c.after(c1) && c.before(c2)); //在权益有效期内
    }

}
