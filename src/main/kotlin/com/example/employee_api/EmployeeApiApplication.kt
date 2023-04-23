package com.example.employee_api

import com.example.employee_api.db.entities.EmployeeDocumentsEntity
import com.example.employee_api.db.entities.EmployeeEntity
import com.example.employee_api.db.services.EmployeeDocumentServiceImpl
import com.example.employee_api.db.services.EmployeeServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
@ConfigurationPropertiesScan
@SpringBootApplication
@EntityScan("com.example.employee_api.db.entities")
class EmployeeApiApplication @Autowired constructor(
    val employeeServiceImpl: EmployeeServiceImpl,
    val employeeDocumentServiceImpl: EmployeeDocumentServiceImpl
) {

    @GetMapping
    fun rootHandler(): String {
        return "OK";
    }

    @GetMapping("/employees")
    fun getAllEmployees(): MutableList<EmployeeEntity>? {
        return employeeServiceImpl.findAll()
    }

    @GetMapping("/employees/{id}")
    fun getEmployeeById(@PathVariable("id") id: Long): EmployeeEntity? {
        return employeeServiceImpl.findById(id)
    }

    @PostMapping("/employees/create")
    fun createEmployee(@RequestBody employee: EmployeeEntity): EmployeeEntity? {
        return employeeServiceImpl.create(employee)
    }

    @PutMapping("/employees/update/{id}")
    fun updateEmployee(@PathVariable("id") id: Long, @RequestBody employee: EmployeeEntity): EmployeeEntity? {
        return employeeServiceImpl.update(id, employee)
    }

    @DeleteMapping("/employees/{id}")
    fun deleteEmployee(@PathVariable("id") id: Long): EmployeeEntity? {
        return employeeServiceImpl.deleteById(id)
    }

    @GetMapping("/documents")
    fun getAllEmployeeDocuments(): MutableList<EmployeeDocumentsEntity>? {
        return employeeDocumentServiceImpl.findAll()
    }

    @GetMapping("/documents/{id}")
    fun getEmployeeDocumentsById(@PathVariable("id") id: Long): EmployeeDocumentsEntity? {
        return employeeDocumentServiceImpl.findById(id)
    }

    @PostMapping("/documents/create")
    fun createEmployeeDocuments(@RequestBody employee: EmployeeDocumentsEntity): EmployeeDocumentsEntity? {
        return employeeDocumentServiceImpl.create(employee)
    }

    @PutMapping("/documents/update/{id}")
    fun updateEmployeeDocuments(
        @PathVariable("id") id: Long,
        @RequestBody employee: EmployeeDocumentsEntity
    ): EmployeeDocumentsEntity? {
        return employeeDocumentServiceImpl.update(id, employee)
    }

    @DeleteMapping("/documents/{id}")
    fun deleteEmployeeDocuments(@PathVariable("id") id: Long): EmployeeDocumentsEntity? {
        return employeeDocumentServiceImpl.deleteById(id)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<EmployeeApiApplication>(*args)
        }
    }
}