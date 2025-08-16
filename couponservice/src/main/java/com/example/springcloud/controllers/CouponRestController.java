package com.example.springcloud.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springcloud.model.Coupon;
import com.example.springcloud.repos.CouponRepo;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/couponapi")
@CrossOrigin
public class CouponRestController {

    @Autowired
    CouponRepo couponRepo;

    @PostMapping("/coupons")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Coupon createCoupon(@RequestBody Coupon coupon) {
        // Logic to create a coupon
        return couponRepo.save(coupon);
    }

    @GetMapping("/coupons/{code}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Coupon getCoupon(@PathVariable("code") String code) {
        // Logic to retrieve a coupon by code
        return couponRepo.findByCode(code);
    }

}
