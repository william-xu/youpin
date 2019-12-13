package com.hflw.vasp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hflw.vasp.enums.StatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = -8402343759691572908L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @ApiModelProperty("主键")
    protected Long id;

    /**
     * 删除状态：0未删除，-1删除，默认未删除
     */
    @Column(name = "del_flag", length = 2)
    @ApiModelProperty(value = "删除状态 0:未删除,-1:删除")
    private Integer delFlag = StatusEnum.FALSE.getStatus();

    /**
     * 启用状态：0启用，1停用，默认启用
     */
    @Column(name = "enable_status", length = 2)
    @ApiModelProperty(value = "启用状态 1:启用,2:停用")
    private Integer enableStatus;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    @Column(name = "create_user", length = 10)
    private Integer createUser;

    /**
     * 操作者
     */
    @Column(name = "update_user", length = 10)
    @ApiModelProperty("修改人")
    private Integer updateUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "create_time", length = 19)
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "update_time", length = 19)
    private Date updateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity)) return false;

        BaseEntity that = (BaseEntity) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
