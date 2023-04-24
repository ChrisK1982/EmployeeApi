package com.example.employee_api.routers

import com.example.employee_api.db.entities.EmployeeDocumentsEntity
import com.example.employee_api.db.repositories.EmployeeDocumentsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router

@RestController
@RequestMapping(path = ["/api/documents"])
class EmployeeDocumentRouter(employeeDocumentsRepository: EmployeeDocumentsRepository) :
        AbstractEmployeeRouter<EmployeeDocumentsEntity, EmployeeDocumentsRepository>(employeeDocumentsRepository)