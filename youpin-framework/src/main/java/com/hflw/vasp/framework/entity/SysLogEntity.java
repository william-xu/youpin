package com.hflw.vasp.framework.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统日志,不采用表来存储 since 2019/1/14 存储在 mongodb
 */
@Data
//@Entity
//@Table(name = "sys_log")
public class SysLogEntity implements Serializable {


    private String id;

    // 模块功能
    @Column(name = "modul", length = 255)
    private String modul;

    // 操作人
    @Column(name = "operator", length = 10)
    private Long operator;

    // 请求参数
    @Column(name = "request_data", length = 512)
    private String requestData;

    /**
     * ip地址
     */
    @Column(name = "ip", length = 46)
    private String ip;

    // 创建时间
    @Column(name = "create_time", length = 19)
    private Date createTime;

}
