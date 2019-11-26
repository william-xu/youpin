package com.hflw.vasp.message;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * BaseMsg
 * 消息基类
 *
 * @author jin
 * @date 2018/12/3 19:44
 */
public class BaseMsg implements Serializable {

    private Integer messageCode;

    public Integer getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(Integer messageCode) {
        this.messageCode = messageCode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}



