package com.hflw.vasp.model;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ImportResultModel {
    @ApiModelProperty(value = "导入成功数",required = true,name = "success")
    private Integer success;
    @ApiModelProperty(value = "导入失败数",required = true,name = "fault")
    private Integer fault = 0;
    @ApiModelProperty(value = "失败Excel文件地址",required = true,name = "address")
    private String address;

    public ImportResultModel() {
    }

    public ImportResultModel(Integer success, Integer fault, String address) {
        this.success = success;
        this.fault = fault;
        this.address = address;
    }

    public ImportResultModel(Integer success) {
        this.success = success;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getFault() {
        return fault;
    }

    public void setFault(Integer fault) {
        this.fault = fault;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
