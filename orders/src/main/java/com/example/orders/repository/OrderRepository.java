package com.example.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.orders.model.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long> {

}
