package com.example.employeeapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@SpringBootApplication(exclude = [ DataSourceAutoConfiguration::class ])
@RequestMapping("/api")
@ConfigurationPropertiesScan
class EmployeeApiApplication {

	@RequestMapping
	fun rootHandler(): String {
		return "OK";
	}
}

fun main(args: Array<String>) {
	runApplication<EmployeeApiApplication>(*args)
}