package com.example.orders.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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
    
	public String createOrder(OrderRequestDto request, String jwtToken) {
        CustomerDto customer = request.getCustomer();
        List<OrderDetailDto> items = request.getItems();

        Orders order = new Orders();
        order.setCustomerId(customer.getId());
        order.setCustomerEmail(customer.getEmail());

        List<OrderDetail> details = items.stream().map(item -> {        	
        	// Validate product exists
            if (!productExists(item.getProductId(), jwtToken)) {
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

        return createOrderAndPay(order, request.getCustomer(), jwtToken);
    }
    
    private String createOrderAndPay(Orders order, CustomerDto customerDto, String jwtToken) {
        // Save order
        Orders savedOrder = repository.save(order);

        // Call payment service
        PaymentRequest paymentRequest = new PaymentRequest(customerDto, convertToDtos(savedOrder.getDetails()));
        
        return (String) processRequest(jwtToken, paymentServiceUrl, paymentRequest, HttpMethod.POST).getBody();

    }
    
    private ResponseEntity<?> processRequest(String jwtToken, String url, Object request, HttpMethod method) {
    	 HttpHeaders headers = new HttpHeaders();
         headers.setContentType(MediaType.APPLICATION_JSON);
         headers.setBearerAuth(jwtToken);
         HttpEntity<?> requestEntity = new HttpEntity<>(request, headers);
         
         ResponseEntity<?> response = restTemplate.exchange(
                 url,
                 method,
                 requestEntity,
                 String.class
             );
         return response;
    }
    
    private boolean productExists(Long productId, String jwtToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);

            HttpEntity<Void> request = new HttpEntity<>(headers);

            ResponseEntity<ProductDto> response = restTemplate.exchange(
            		productServiceUrl + "/" + productId,
                HttpMethod.GET,
                request,
                ProductDto.class
            );

            return response.getStatusCode() == HttpStatus.OK && response.getBody() != null;
        } catch (HttpClientErrorException.NotFound e) {
            return false; 
        } catch (Exception e) {
            throw new RuntimeException("Product ID: " + productId + " not found", e);
        }
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
