package com.example.employee_api.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "api")
class EmployeeAPIConfiguration(
        val basicAuthUsername: String,
        val basicAuthPassword: String
)
