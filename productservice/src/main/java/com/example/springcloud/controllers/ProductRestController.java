package com.example.springcloud.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.dto.Coupon;
import com.example.springcloud.model.Product;
import com.example.springcloud.repos.ProductRepo;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/productapi")
public class ProductRestController {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${couponservice.url}")
    private String couponServiceUrl;

    @PostMapping("/products")
    public Product createProduct(@RequestBody Product product, HttpServletRequest request) {
    // Extract authorization header from incoming request
    String authHeader = request.getHeader("Authorization");
    
    HttpHeaders headers = new HttpHeaders();
    if (authHeader != null) {
        headers.set("Authorization", authHeader);
    }
    
    HttpEntity<String> entity = new HttpEntity<>(headers);
    
    ResponseEntity<Coupon> response = restTemplate.exchange(
        couponServiceUrl + product.getCouponCode(), 
        HttpMethod.GET, 
        entity, 
        Coupon.class
    );
    
    Coupon coupon = response.getBody();
    //Coupon coupon = restTemplate.getForObject(couponServiceUrl+product.getCouponCode(), Coupon.class);
    product.setPrice(product.getPrice().subtract(coupon.getDiscount()));
    return productRepo.save(product);
    }

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable Long id) throws Exception {
        return productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));   
    }       
}
