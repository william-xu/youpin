package com.hflw.vasp.modules.entity;

import com.hflw.vasp.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * (由平台)配置到门店的商品到店价
 * 
 * @author liumh
 * @date 2019-04-15 14:56:01
 */
@Data
@Entity
@Table(name = "d_store_goods_config")
public class StoreGoodsConfig extends BaseEntity {

	/**
	 * 门店id
	 */
	private Integer storeId;
	/**
	 * 商品id
	 */
	private Integer goodsId;
	/**
	 * 到店价
	 */
	private BigDecimal shopPrice;

}
