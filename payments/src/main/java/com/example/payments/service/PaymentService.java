package com.example.payments.service;

import org.springframework.stereotype.Service;

import com.example.payments.dto.PaymentRequestDto;

@Service
public class PaymentService {

    public String processPayment(PaymentRequestDto request) {
        double total = calculateTotal(request);
        String customerEmail = request.getCustomer().getEmail();

        // Simulate payment process
        return String.format("Payment of $%.2f for customer %s was successful", total, customerEmail);
    }

    private double calculateTotal(PaymentRequestDto request) {
        return request.getItems().stream()
                .mapToDouble(item -> item.getProductPrice() * item.getQuantity())
                .sum();
    }
}
