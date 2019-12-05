package com.hflw.vasp.framework.service;

import com.hflw.vasp.framework.components.RedisCacheUtils;
import com.hflw.vasp.framework.constant.ConstantKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisCacheUtils redisCacheUtil;

    public boolean addRedisLock(String redisLockMaxOrderNumber, int i, int i1) {
        return false;
    }

    public void delRedisLock(String redisLockMaxOrderNumber) {

    }

    /**
     * Redis生成数据库全局唯一性id
     *
     * @return
     */
    public Long getGlobalUniqueId() {
        String orderIdPrefix = this.getOrderIdPrefix(new Date());
        Long id = this.orderId(orderIdPrefix);
        return id;
    }

    /**
     * 获取年的后两位加上一年多少天+当前小时数作为前缀
     *
     * @param date
     * @return
     */
    public String getOrderIdPrefix(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        //补两位,因为一年最多三位数
        String monthFormat = String.format("%1$02d", month + 1);
        //补两位，因为日最多两位数
        String dayFormat = String.format("%1$02d", day);
        //补两位，因为小时最多两位数
        String hourFormat = String.format("%1$02d", hour);
        return year + monthFormat + dayFormat + hourFormat;
    }

    /**
     * 生成订单
     *
     * @param prefix
     * @return
     */
    public Long orderId(String prefix) {
        String key = ConstantKeys.REDIS_GLOBAL_ORDER_ID + prefix;
        String orderId = null;
        try {
            Long increment = redisTemplate.opsForValue().increment(key, 1);
            //往前补6位
            orderId = prefix + String.format("%1$06d", increment);
        } catch (Exception e) {
            System.out.println("生成订单号失败");
            e.printStackTrace();
        }
        return Long.valueOf(orderId);
    }

}
