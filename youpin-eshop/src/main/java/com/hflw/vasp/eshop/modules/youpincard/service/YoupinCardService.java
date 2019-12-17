package com.hflw.vasp.eshop.modules.youpincard.service;

import com.alibaba.fastjson.JSONObject;
import com.hflw.vasp.exception.BusinessException;
import com.hflw.vasp.modules.dao.IYoupinCardDao;
import com.hflw.vasp.modules.entity.YoupinCard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@Service
public class YoupinCardService {

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
        if (verifyValid(youpinCard))
            throw BusinessException.create(13543, "优品卡已是生效状态");

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
        log.info("当前权益详情：" + JSONObject.toJSONString(card));

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
