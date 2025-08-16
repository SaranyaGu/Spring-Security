package com.example.springcloud.couponservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
class CouponserviceApplicationTests {

	@Autowired
	MockMvc mvc;

	@Test
	public void testGetCouponWithoutAuth_forbidden() throws Exception{
		mvc.perform(get("/couponapi/coupons/SUPERSALE")).andExpect(status().isUnauthorized());
	}

	@Test
	//@WithMockUser(roles = {"ADMIN"}) 
	@WithUserDetails("doug@bailey.com")
	public void testGetCouponWithoutAuth_ok() throws Exception{
		mvc.perform(get("/couponapi/coupons/SUPERSALE")).andExpect(status().isOk())
		.andExpect(content().string("{\"id\":1,\"code\":\"SUPERSALE\",\"discount\":10.00,\"expDate\":\"12/12/2020\"}"));
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	public void testCreateCoupon_WithoutCsrf_Forbidden() throws Exception {
		mvc.perform(
			post("/saveCoupon")
				.content("{\"code\":\"SUPERSALECSRF\",\"discount\":70.00,\"expDate\":\"12/12/2020\"}")
				.contentType("application/json")
		)
		.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	public void testCreateCoupon_WithCsrf_Ok() throws Exception {
		mvc.perform(
			post("/saveCoupon")
				.content("{\"code\":\"SUPERSALECSRF\",\"discount\":70.00,\"expDate\":\"12/12/2020\"}")
				.contentType("application/json").with(csrf().asHeader())
		)
		.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = {"USER"})
	public void testCreateCoupon_NonAdminUser_Forbidden() throws Exception {
		mvc.perform(
			post("/saveCoupon")
				.content("{\"code\":\"SUPERSALECSRF\",\"discount\":70.00,\"expDate\":\"12/12/2020\"}")
				.contentType("application/json").with(csrf().asHeader())
		)
		.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	public void testCORS() throws Exception {
		mvc.perform(options("/couponapi/coupons").header("Access-Control-Request-Method", "POST").
		header("Origin", "http://www.google.com")).andExpect(status().isOk())
			.andExpect(header().exists("Access-Control-Allow-Origin"))
			.andExpect(header().string("Access-Control-Allow-Origin", "*"))
			.andExpect(header().exists("Access-Control-Allow-Methods"))
			.andExpect(header().string("Access-Control-Allow-Methods", "POST"));
		
	}
	

}
