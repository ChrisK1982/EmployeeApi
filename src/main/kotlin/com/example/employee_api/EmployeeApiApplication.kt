package com.example.employee_api

import com.example.employee_api.routers.EmployeeDocumentRouter
import com.example.employee_api.routers.EmployeeRouter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.function.ServerRequest
import java.util.*

@RestController
@RequestMapping("/api")
@ConfigurationPropertiesScan
@SpringBootApplication
@EntityScan("com.example.employee_api.db.entities")
class EmployeeApiApplication @Autowired constructor(
    private val employeeRouter: EmployeeRouter,
    private val employeeDocumentRouter: EmployeeDocumentRouter,
) {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<EmployeeApiApplication>(*args)
        }
    }

    @RequestMapping(path = ["/employees/**"])
    fun employeesApiHandler(request: ServerHttpRequest) {
        return employeeRouter
            .routes()
            .route(request as ServerRequest)
            .ifPresent { handler -> handler.handle(request) }
    }

    @RequestMapping(path = ["/documents/**"])
    fun employeesDocumentsApiHandler(request: ServerHttpRequest) {
        return employeeDocumentRouter
            .routes()
            .route(request as ServerRequest)
            .ifPresent { handler -> handler.handle(request) }
    }

    @GetMapping
    fun rootHandler(): String {
        return "OK";
    }

}
