package com.hflw.vasp.modules.entity;

import com.hflw.vasp.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 优惠券表
 *
 * @author liuyf
 * @date 2019-04-04 17:23:51
 */
@Accessors(chain = true)
@Data
@Entity
@Table(name = "d_coupon")
@ApiModel("优惠券表")
public class Coupon extends BaseEntity {

    private static final long serialVersionUID = 9205083168508127449L;
    /**
     * 优惠劵名称(有图片则显示图片)
     */
    @Column(name = "title", length = 64)
    private String title;
    /**
     * 图片
     */
    @Column(name = "icon", length = 123)
    private String icon;
    /**
     * 可用于：10店铺优惠券 11新人店铺券  20商品优惠券  30类目优惠券  60平台优惠券 61新人平台券
     */
    @Column(name = "used", length = 2)
    private Integer used;
    /**
     * 1抵用券 2满减券 3无门槛券（需要限制大小）
     */
    @Column(name = "type", length = 2)
    private Integer type;
    /**
     * 1可用于特价商品 2不能 默认不能(商品优惠卷除外)
     */
    @Column(name = "with_special", length = 2)
    private Integer withSpecial;
    /**
     * 店铺或商品流水号/id,如果used=20券作用于商品，used=30券作用于整个商品类目
     */
    @Column(name = "with_sn", length = 64)
    private String withSn;
    /**
     * 满多少金额
     */
    @Column(name = "with_amount", length = 20)
    private BigDecimal withAmount;
    /**
     * 劵成本价
     */
    @Column(name = "cost_amount", length = 20)
    private BigDecimal costAmount;
    /**
     * 劵抵用金额
     */
    @Column(name = "used_amount", length = 20)
    private BigDecimal usedAmount;
    /**
     * 单个（商品）使用配额：针对单个商品、服务等使用券的个数最大限制
     */
    @Column(name = "quota", length = 32)
    private Integer quota;
    /**
     * 总配额：发（行）券数量
     */
    @Column(name = "total_quota", length = 10)
    private Integer totalQuota;
    /**
     * 已领取的优惠券数量
     */
    @Column(name = "take_count", length = 10)
    private Integer takeCount;
    /**
     * 已使用的优惠券数量
     */
    @Column(name = "used_count", length = 10)
    private Integer usedCount;
    /**
     * 发放开始时间
     */
    @Column(name = "start_time", length = 10)
    private Date startTime;
    /**
     * 发放结束时间
     */
    @Column(name = "end_time", length = 20)
    private Date endTime;
    /**
     * 时效:1绝对时效（领取后XXX-XXX时间段有效），2相对时效（领取后N天有效）
     */
    @Column(name = "valid_type", length = 20)
    private Integer validType;
    /**
     * 使用开始时间
     */
    @Column(name = "valid_start_time", length = 20)
    private Date validStartTime;
    /**
     * 使用结束时间
     */
    @Column(name = "valid_end_time", length = 20)
    private Date validEndTime;
    /**
     * 自领取之日起有效天数
     */
    @Column(name = "valid_days", length = 5)
    private Integer validDays;
    /**
     * 单个商品用券数量
     */
    @Column(name = "used_quantity", length = 5)
    private Integer usedQuantity;
    /**
     * 使用门槛：1无使用门槛，2需做金融
     */
    @Column(name = "used_threshold", length = 2)
    private Integer usedThreshold;

}
