package com.ecommerce.eccomerce.security;

import com.ecommerce.eccomerce.security.filter.SecurityFilterCompany;
import com.ecommerce.eccomerce.security.filter.SecurityFilterUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final SecurityFilterUser securityFilterUser;
    private final SecurityFilterCompany securityFilterCompany;

    public SecurityConfig(SecurityFilterUser securityFilterUser, SecurityFilterCompany securityFilterCompany){
        this.securityFilterUser = securityFilterUser;
        this.securityFilterCompany = securityFilterCompany;
    }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(auth->{
           auth.requestMatchers("/user/login").permitAll();
           auth.requestMatchers("/user/register").permitAll();
           auth.requestMatchers("/company/login").permitAll();
           auth.requestMatchers("/company/register").permitAll();
           auth.anyRequest().authenticated();
        })
                .addFilterBefore(securityFilterUser, BasicAuthenticationFilter.class)
                .addFilterBefore(securityFilterCompany, BasicAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
