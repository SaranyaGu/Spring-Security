package com.example.springcloud.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springcloud.model.Product;

public interface ProductRepo extends JpaRepository<Product, Long> {

}
