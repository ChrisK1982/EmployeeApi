package com.example.employee_api.security

import com.example.employee_api.configuration.EmployeeAPIConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class EmployeeAPISecurityConfig(val apiConfiguration: EmployeeAPIConfiguration) {
    @Bean
    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity
                .authorizeHttpRequests { auth ->
                    auth
                            .anyRequest()
                            .authenticated()
                }
                .httpBasic()
        return httpSecurity.build()
    }

    @Bean
    fun userDetailsService(): InMemoryUserDetailsManager {
        val user = User
                .withUsername(apiConfiguration.basicAuthUsername)
                .password(passwordEncoder().encode(apiConfiguration.basicAuthPassword))
                .roles("EmployeeAPIUser")
                .build()

        return InMemoryUserDetailsManager(user)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}