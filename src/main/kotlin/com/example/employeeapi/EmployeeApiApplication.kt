package com.example.employeeapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@SpringBootApplication
class EmployeeApiApplication {

	@RequestMapping("/")
	fun rootHandler(): String {
		return "OK";
	}

	fun main(args: Array<String>) {
		runApplication<EmployeeApiApplication>(*args)
	}
}