package com.example.employee_api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
@ConfigurationPropertiesScan
@SpringBootApplication
@EntityScan("com.example.employee_api.db.entities")
class EmployeeApiApplication {

    @GetMapping
    fun rootHandler(): String {
        return "OK";
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<EmployeeApiApplication>(*args)
        }
    }
}