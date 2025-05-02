package com.example.common.dto;

public class OrderDetailDto {
    private Long productId;
    private String productTitle;
    private Double productPrice;
    private Integer quantity;
    
    public OrderDetailDto() {
    	
    }
	public OrderDetailDto(Long productId, String productTitle, Double productPrice, Integer quantity) {
		this.productId = productId;
		this.productTitle = productTitle;
		this.productPrice = productPrice;
		this.quantity = quantity;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductTitle() {
		return productTitle;
	}
	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}
	public Double getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(Double productPrice) {
		this.productPrice = productPrice;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

    
}
