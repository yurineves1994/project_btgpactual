package com.project.btgpactual.orderms.entities.dto;

import java.util.List;

public record OrderCreatedEvent(long codigoPedido,
                                long codigoCliente,
                                List<OrderItemEvent> itens) {

}
