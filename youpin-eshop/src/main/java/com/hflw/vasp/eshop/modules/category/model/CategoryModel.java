package com.hflw.vasp.eshop.modules.category.model;

import java.io.Serializable;

/**
 * 商品分类表
 * 
 * @author liuyf
 * @date 2019-04-01 15:17:15
 */

public class CategoryModel implements Serializable {
	/**
	 * 类别code
	 */
	private String code;
	/**
	 * 类别名称
	 */
	private String name;
	/**
	 * 父级id
	 */
	private String parentCode;
	/**
	 * 分类层级
	 */
	private Integer level;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 类别图片url
	 */
	private String icon;
	/**
	 * 启用状态 1:启用,2:停用
	 */
	private Integer enableStatus;
	/**
	 * 删除标记  -1：已删除  0：正常
	 */
	private Integer delFlag;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getEnableStatus() {
		return enableStatus;
	}

	public void setEnableStatus(Integer enableStatus) {
		this.enableStatus = enableStatus;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	@Override
	public String toString() {
		return "CategoryModel{" +
				"code='" + code + '\'' +
				", name='" + name + '\'' +
				", parentCode='" + parentCode + '\'' +
				", level=" + level +
				", remark='" + remark + '\'' +
				", icon='" + icon + '\'' +
				", enableStatus=" + enableStatus +
				", delFlag=" + delFlag +
				'}';
	}
}
