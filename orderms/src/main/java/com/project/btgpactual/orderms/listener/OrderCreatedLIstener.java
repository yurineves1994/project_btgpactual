package com.project.btgpactual.orderms.listener;

import com.project.btgpactual.orderms.entities.dto.OrderCreatedEvent;
import com.project.btgpactual.orderms.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static com.project.btgpactual.orderms.config.RabbitMQConfig.ORDER_CREATED_QUEUE;

@Component
public class OrderCreatedLIstener {

    private final Logger logger = LoggerFactory.getLogger(OrderCreatedLIstener.class);

    private final OrderService orderService;

    public OrderCreatedLIstener(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = ORDER_CREATED_QUEUE)
    public void listen(Message<OrderCreatedEvent> message) {
        logger.info("Recebido evento de criação de pedido: {}", message);

        orderService.save(message.getPayload());
    }
}
