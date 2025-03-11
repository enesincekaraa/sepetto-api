package com.sepetto.api.orderline.repository;

import com.sepetto.api.orderline.dto.OrderLineResponse;
import com.sepetto.api.orderline.model.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderLineRepository extends JpaRepository<OrderLine, Integer> {
    List<OrderLine> findAllByOrderId(Integer orderId);

}
