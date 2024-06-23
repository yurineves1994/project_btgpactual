package com.project.btgpactual.orderms.controllers.dto;

import com.project.btgpactual.orderms.entities.OrderEntity;

import java.math.BigDecimal;

public record OrderResponse(Long orderId, Long customerId, BigDecimal total) {

    public static OrderResponse fromEntity(OrderEntity entity) {
        return new OrderResponse(entity.getId(), entity.getCustomerId(), entity.getTotal());
    }
}
