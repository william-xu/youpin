package com.hflw.vasp.model.thirdpart;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Date;

/**
 * 第三方短信数据记录;存储在 mongodb
 */
public class SmsModel {
    private String id;
    private String phone;
    private String code;
    private Date createTime;

    public SmsModel() {
    }

    public SmsModel(String phone, String code, Date createTime) {
        this.phone = phone;
        this.code = code;
        this.createTime = createTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
