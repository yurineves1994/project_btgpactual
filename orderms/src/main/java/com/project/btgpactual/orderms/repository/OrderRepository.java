package com.project.btgpactual.orderms.repository;

import com.project.btgpactual.orderms.entities.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<OrderEntity, Long> {
    Page<OrderEntity> findAllByCustomerId(long customerId, PageRequest pageRequest);
}
