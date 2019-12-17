package com.hflw.vasp.admin.modules.order.dto;

import com.google.common.base.Converter;
import com.hflw.vasp.modules.entity.OrderLogistics;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class OrderLogisticsDTO {

    @NotNull
    private Long orderId;

    @NotBlank
    private String number;

    private String company;

    private String remark;

    public OrderLogistics convertTo() {
        DTOConverter converter = new DTOConverter();
        return converter.convert(this);
    }

    public OrderLogisticsDTO convertFor(OrderLogistics o) {
        DTOConverter converter = new DTOConverter();
        return converter.reverse().convert(o);
    }

    private static class DTOConverter extends Converter<OrderLogisticsDTO, OrderLogistics> {
        @Override
        protected OrderLogistics doForward(OrderLogisticsDTO source) {
            OrderLogistics target = new OrderLogistics();
            BeanUtils.copyProperties(source, target);
            return target;
        }

        @Override
        protected OrderLogisticsDTO doBackward(OrderLogistics source) {
            OrderLogisticsDTO target = new OrderLogisticsDTO();
            BeanUtils.copyProperties(source, target);
            return target;
        }
    }

}
