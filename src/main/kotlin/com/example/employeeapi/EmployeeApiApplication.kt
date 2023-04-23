package com.example.employeeapi

import com.example.employeeapi.dto.Employee
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
@ConfigurationPropertiesScan
@SpringBootApplication
@EntityScan("com.example.employeeapi.db.entities")
class EmployeeApiApplication {

    @GetMapping
    fun rootHandler(): String {
        return "OK";
    }

    @PostMapping("/employees/info/update")
    fun batchEmployeesUpdate(@RequestBody employees: List<Employee>) {

    }

    @PostMapping("/employees/documents/upload")
    fun batchDocumentUpload(@RequestBody employees: List<Employee>) {

    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<EmployeeApiApplication>(*args)
        }
    }
}