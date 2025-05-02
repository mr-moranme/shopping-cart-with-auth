package com.example.payments.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.payments.dto.PaymentRequestDto;
import com.example.payments.service.PaymentService;

@RestController
@RequestMapping("/payments")
public class PaymentController {

	@Autowired
	private PaymentService service;
	
    @PostMapping
    public ResponseEntity<String> processPayment(@RequestBody PaymentRequestDto request) {
        return ResponseEntity.ok(service.processPayment(request));
    }
    
	@ExceptionHandler
    protected ResponseEntity<?> handleExceptions(Exception ex) {
		ex.printStackTrace();
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
