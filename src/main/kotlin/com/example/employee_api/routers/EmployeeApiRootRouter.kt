package com.example.employee_api.routers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router

@Configuration
class EmployeeApiRootRouter {
    @Bean
    fun apiRootRoutes(): RouterFunction<ServerResponse> = router {
        GET("/api") { ServerResponse.ok().body("OK") }
    }
}