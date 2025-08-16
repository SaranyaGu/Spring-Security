# Spring Cloud Microservices

This project contains two microservices:

- **Coupon Service**
- **Product Service**

Both services are secured with custom authentication and expose REST APIs.

## Features

- **Coupon Service:** Manage coupons (list, create, etc.)
- **Product Service:** Manage products (list, create, etc.)
- **Custom Authentication:** All endpoints are protected.
- **REST APIs:** Easily interact with both services via HTTP.

## API Endpoints

- **List Products/Coupons:**  
    `GET /products`  
    `GET /coupons`

- **Create Product/Coupon:**  
    `POST /products`  
    `POST /coupons`

## Getting Started

1. Clone the repository.
2. Follow the setup instructions in each service directory.
3. Use an API client (e.g., Postman) to interact with the endpoints.

AUTH FLOW in Spring security with each components : 
   
   <img width="1064" height="289" alt="image" src="https://github.com/user-attachments/assets/0c63bdef-4c16-4550-9745-424fcbdd05c4" />


