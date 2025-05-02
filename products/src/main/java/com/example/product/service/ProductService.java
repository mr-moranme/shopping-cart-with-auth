package com.example.product.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.product.dto.ProductDto;

@Service
public class ProductService {

	@Autowired
	private RestTemplate restTemplate;
	
    private final String FAKESTORE_API = "https://fakestoreapi.com/products";

    public List<ProductDto> getAll() {
    	ProductDto[] productos = restTemplate.getForObject(FAKESTORE_API, ProductDto[].class);
        return Arrays.asList(productos);
    }

    public ProductDto getById(Long id) {
        return restTemplate.getForObject(FAKESTORE_API + "/" + id, ProductDto.class);
    }
}
