package com.example.employee_api.configuration

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@ConfigurationProperties(prefix = "api")
class EmployeeAPIConfiguration(
    val basicAuthUsername: String,
    val basicAuthPassword: String,
    val basicAuthRole: String
)
