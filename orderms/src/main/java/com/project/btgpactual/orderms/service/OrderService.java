package com.project.btgpactual.orderms.service;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import java.math.BigDecimal;
import java.util.List;

import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.project.btgpactual.orderms.controllers.dto.OrderResponse;
import com.project.btgpactual.orderms.entities.OrderEntity;
import com.project.btgpactual.orderms.entities.OrderItem;
import com.project.btgpactual.orderms.entities.dto.OrderCreatedEvent;
import com.project.btgpactual.orderms.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository repository;

    private final MongoTemplate mongoTemplate;

    public OrderService(OrderRepository repository, MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    public void save(OrderCreatedEvent event) {
        var entity = new OrderEntity();
        entity.setId(event.codigoPedido());
        entity.setCustomerId(event.codigoCliente());
        entity.setItems(getOrderItems(event));
        entity.setTotal(getTotal(event));

        repository.save(entity);
    }

    public Page<OrderResponse> findAllByCustomerId(long customerId, PageRequest pageRequest) {
        var orders = repository.findAllByCustomerId(customerId, pageRequest);

        return orders.map(OrderResponse::fromEntity);
    }

    public BigDecimal findTotalOnOrderByCustomerId(long customerId) {
        var aggregations = newAggregation(
                match(Criteria.where("customerId").is(customerId)),
                group().sum("total").as("total"));

        var response = mongoTemplate.aggregate(aggregations, "tb_orders", Document.class);

        return new BigDecimal(response.getUniqueMappedResult().get("total").toString());
    }

    private static BigDecimal getTotal(OrderCreatedEvent event) {
        return event.itens().stream().map(item -> item.preco().multiply(BigDecimal.valueOf(item.quantidade())))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    private static List<OrderItem> getOrderItems(OrderCreatedEvent event) {
        return event.itens().stream().map(item -> new OrderItem(item.produto(), item.quantidade(), item.preco()))
                .toList();
    }
}
