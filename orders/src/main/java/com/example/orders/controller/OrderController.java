package com.example.orders.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.orders.dto.OrderRequestDto;
import com.example.orders.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	private OrderService service;
	
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDto order, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String jwtToken = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.replace("Bearer ", "");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
        }
        return ResponseEntity.ok(service.createOrder(order, jwtToken));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getOrderById(id));
    }
    
	@ExceptionHandler
    protected ResponseEntity<?> handleExceptions(Exception ex) {
		ex.printStackTrace();
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
