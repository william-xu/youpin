package com.hflw.vasp.eshop.modules.goods.model;

import com.hflw.vasp.modules.entity.Goods;
import lombok.Data;

import java.util.List;

/**
 * 商品表
 * 
 * @author liumh
 * @date 2019-04-01 17:52:54
 */
@Data
public class GoodsModel {

	private String category;

	private List<Goods> list;

}
