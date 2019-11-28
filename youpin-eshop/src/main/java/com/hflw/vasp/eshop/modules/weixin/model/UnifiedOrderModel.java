/**
 * Copyright (C), 2015-2018, 广联赛讯有限公司
 * FileName: UnifiedOrderModel
 * Author:   liuyf
 * Date:     2018/5/17 13:59
 * Description: 微信支付预订单Modle
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hflw.vasp.eshop.modules.weixin.model;

import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈微信支付预订单Model〉
 *
 * @author liuyf
 * @create 2018/5/17
 * @since 1.0.0
 */
@Data
public class UnifiedOrderModel {

    private String body;
    private Integer totalFee;

    private String notifyUrl;

}