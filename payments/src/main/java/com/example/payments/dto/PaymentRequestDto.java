package com.example.payments.dto;

import java.util.List;

import com.example.common.model.Customer;
import com.example.common.model.OrderDetail;

public class PaymentRequestDto {
    private Customer customer;
    private List<OrderDetail> items;
    
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public List<OrderDetail> getItems() {
		return items;
	}
	public void setItems(List<OrderDetail> items) {
		this.items = items;
	}

   
}
