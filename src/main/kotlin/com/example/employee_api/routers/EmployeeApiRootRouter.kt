package com.example.employee_api.routers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(produces = ["application/text", "application/json"])
class EmployeeApiRootRouter {
    @GetMapping(path = ["/api"])
    fun rootHandler(): String {
        return "OK"
    }
}