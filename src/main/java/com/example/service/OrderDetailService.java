package com.example.service;

import com.example.model.Order;
import com.example.model.OrderDetail;
import com.example.model.Product;
import com.example.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailService implements IOrderDetailService {


private final OrderDetailRepository orderDetailRepository;

@Autowired
public OrderDetailService(OrderDetailRepository orderDetailRepository) {
    this.orderDetailRepository = orderDetailRepository;
}

@Override
public OrderDetail saveOrUpdateOrderDetail(OrderDetail orderDetail) {
    return orderDetailRepository.save(orderDetail);
}

@Override
public List<OrderDetail> getAllOrderDetails() {
    return orderDetailRepository.findAll();
}

@Override
public Optional<OrderDetail> getOrderDetailById(int id) {
    return orderDetailRepository.findById(id);
}

@Override
public List<OrderDetail> getOrderDetailsByOrder(Order order) {
    return orderDetailRepository.findByOrder(order);
}

@Override
public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
    return orderDetailRepository.findByOrderOrderId(orderId);
}

@Override
public List<OrderDetail> getOrderDetailsByProduct(Product product) {
    return orderDetailRepository.findByProduct(product);
}

@Override
public void deleteOrderDetailById(int id) {
    orderDetailRepository.deleteById(id);
}
}