package com.example.orders.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.common.dto.CustomerDto;
import com.example.common.dto.OrderDetailDto;
import com.example.common.dto.ProductDto;
import com.example.common.model.OrderDetail;
import com.example.orders.dto.OrderRequestDto;
import com.example.orders.dto.PaymentRequest;
import com.example.orders.model.Orders;
import com.example.orders.repository.OrderRepository;

@Service
public class OrderService {

	@Autowired
    private OrderRepository repository;
	@Autowired
	private RestTemplate restTemplate;

    @Value("${payments.service.url}") // http://localhost:8083/payments
    private String paymentServiceUrl;
    
    @Value("${products.service.url}") // http://localhost:8081/products
    private String productServiceUrl;
    
	public String createOrder(OrderRequestDto request) {
        CustomerDto customer = request.getCustomer();
        List<OrderDetailDto> items = request.getItems();

        Orders order = new Orders();
        order.setCustomerId(customer.getId());
        order.setCustomerEmail(customer.getEmail());

        List<OrderDetail> details = items.stream().map(item -> {        	
        	// Validate product exists
        	String url = productServiceUrl + "/" + item.getProductId();
            ProductDto product = restTemplate.getForObject(url, ProductDto.class);
            if (product == null) {
                throw new RuntimeException("Product ID " + item.getProductId() + " not found");
            }
            OrderDetail detail = new OrderDetail();
            detail.setProductId(item.getProductId());
            detail.setProductTitle(item.getProductTitle());
            detail.setProductPrice(item.getProductPrice());
            detail.setQuantity(item.getQuantity());
            return detail;
        }).collect(Collectors.toList());

        order.setDetails(details);

        return createOrderAndPay(order, request.getCustomer());
    }
    
    public String createOrderAndPay(Orders order, CustomerDto customerDto) {
        // Save order
        Orders savedOrder = repository.save(order);

        // Call payment service
        PaymentRequest paymentRequest = new PaymentRequest(customerDto, convertToDtos(savedOrder.getDetails()));
        String result = restTemplate.postForObject(paymentServiceUrl, paymentRequest, String.class);

        return result;
    }
    public Optional<Orders> getOrderById(Long id) {
        return repository.findById(id);
    }
    
    private List<OrderDetailDto> convertToDtos(List<OrderDetail> details) {
        return details.stream()
            .map(d -> new OrderDetailDto(d.getProductId(), d.getProductTitle(), d.getProductPrice(), d.getQuantity()))
            .toList();
    }
}
