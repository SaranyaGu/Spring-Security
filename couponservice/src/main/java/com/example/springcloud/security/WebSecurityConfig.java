package com.example.springcloud.security;

// import java.beans.Customizer; // Remove this import if not used elsewhere
import org.springframework.security.config.Customizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
public class WebSecurityConfig {

    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(new RequestAttributeSecurityContextRepository(),
        new HttpSessionSecurityContextRepository());
    }

    @Bean
    AuthenticationManager authManager() {
        System.out.println("Configuring authManager");
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }


    // @Bean
    // HandlerMappingIntrospector handlerMappingIntrospector() {
    //     return new HandlerMappingIntrospector();
    // }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        System.out.println("Configuring security filter chain");
        http.authorizeHttpRequests(authorize -> {
            try {
                authorize.requestMatchers(HttpMethod.GET, "/couponapi/coupons/{code}", "/showGetCoupon", "/getCoupon")
                        .hasAnyRole("USER", "ADMIN")
                        //.permitAll()
                        .requestMatchers(HttpMethod.GET, "/showCreateCoupon", "/createCoupon", "/createResponse")
                        .hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/couponapi/coupons", "/saveCoupon")
                        .hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/getCoupon")
                        .hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/", "/login", "/showReg", "/registerUser").permitAll()
                        .anyRequest().authenticated();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        http.httpBasic(org.springframework.security.config.Customizer.withDefaults());

        http.logout(logout -> logout.logoutSuccessUrl("/"));

        //The below is used to disable CSRF
        //http.csrf(csrf -> csrf.disable());

        //The below URLs will be ignored for CSRF checks
         http.csrf(csrfCustomizer -> {
                csrfCustomizer.ignoringRequestMatchers("/couponapi/coupons/**");
                RequestMatcher regexMatchers = new RegexRequestMatcher("^/couponapi/coupons/[A-Z0-9]*$", "POST");
                csrfCustomizer.ignoringRequestMatchers(regexMatchers);
                csrfCustomizer.ignoringRequestMatchers(new AntPathRequestMatcher("/getCoupon"));
         });

        http.securityContext(securityContext -> securityContext.requireExplicitSave(true));
       
        return http.build();
    }

}
