package com.example.springcloud.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
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
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.httpBasic(Customizer.withDefaults());
        
        http.authorizeHttpRequests( authorize -> {
            authorize.requestMatchers(HttpMethod.GET, "/productapi/products/**", "/showGetProduct", "/getProduct")
                .hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/showCreateProduct", "/createProduct", "/createResponse")
                .hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/productapi/products", "/saveProduct", "/getProduct")
                .hasRole("ADMIN")
                .requestMatchers("/", "/login")
                .permitAll()
                .anyRequest().authenticated();
            });

        //Uncomment the below line to disable CSRF protection
        //http.csrf(csrf -> csrf.disable());

        http.logout(logout -> logout.logoutSuccessUrl("/"));

        http.csrf(csrf -> csrf
            .ignoringRequestMatchers(
                new AntPathRequestMatcher("/getProduct"),
                new RegexRequestMatcher("^/productapi/products/\\d+$", "POST")
            )
        );

        http.securityContext(context -> context.requireExplicitSave(true));

        return http.build();
    }

}
