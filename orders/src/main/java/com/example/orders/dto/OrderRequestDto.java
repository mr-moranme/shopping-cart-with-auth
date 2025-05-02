package com.example.orders.dto;



import java.util.List;

import com.example.common.dto.CustomerDto;
import com.example.common.dto.OrderDetailDto;

public class OrderRequestDto {
    private CustomerDto customer;
    private List<OrderDetailDto> items;
    
	public CustomerDto getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerDto customer) {
		this.customer = customer;
	}
	public List<OrderDetailDto> getItems() {
		return items;
	}
	public void setItems(List<OrderDetailDto> items) {
		this.items = items;
	}


}
