package com.example.springcloud.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.example.dto.Coupon;
import com.example.springcloud.model.Product;
import com.example.springcloud.repos.ProductRepo;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ProductController {

	@Autowired
	private ProductRepo repo;

	@Autowired
    private RestTemplate restTemplate;

	@Value("${couponservice.url}")
    private String couponServiceUrl;

	@GetMapping("/showCreateProduct")
	public String showCreateProduct() {
		return "createProduct";
	}

	@PostMapping("/saveProduct")
	public String save(Product product) {
    	// Extract authorization header from incoming request
    	//String authHeader = request.getHeader("Authorization");
		
    
		// Retrieve the current HttpServletRequest
    	HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth("doug@bailey.com", "doug");
    	// if (authHeader != null) {
        // 	headers.set("Authorization", authHeader);
    	// }

    	HttpEntity<String> entity = new HttpEntity<>(headers);

    	ResponseEntity<Coupon> response = restTemplate.exchange(
        	couponServiceUrl + product.getCouponCode(), 
        	HttpMethod.GET, 
        	entity, 
        	Coupon.class);
    
    	Coupon coupon = response.getBody();
    	product.setPrice(product.getPrice().subtract(coupon.getDiscount()));
		repo.save(product);
		return "createResponse";
	}

	@GetMapping("/showGetProduct")
	public String showGetProduct() {
		return "getProduct";
	}

	@PostMapping("/getProduct")
	public ModelAndView getProduct(Long id) {
		ModelAndView mav = new ModelAndView("productDetails");
    	Product product = repo.findById(id)
                          .orElseThrow(() -> new RuntimeException("Product not found"));
    	mav.addObject("product", product);
		return mav;
	}

}
