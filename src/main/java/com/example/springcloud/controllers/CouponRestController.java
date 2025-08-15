package com.example.springcloud.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springcloud.model.Coupon;
import com.example.springcloud.repos.CouponRepo;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/couponapi")
public class CouponRestController {

    @Autowired
    CouponRepo couponRepo;

    @PostMapping("/coupons")
    public Coupon createCoupon(@RequestBody Coupon coupon) {
        // Logic to create a coupon
        return couponRepo.save(coupon);
    }

    @GetMapping("/coupons/{code}")
    public Coupon getCoupon(@PathVariable("code") String code) {
        // Logic to retrieve a coupon by code
        return couponRepo.findByCode(code);
    }

}
