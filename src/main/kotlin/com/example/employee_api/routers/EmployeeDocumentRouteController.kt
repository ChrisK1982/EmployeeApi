package com.example.employee_api.routers

import com.example.employee_api.db.entities.EmployeeDocumentsEntity
import com.example.employee_api.db.repositories.EmployeeDocumentsRepository
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api/documents"])
class EmployeeDocumentRouteController(employeeDocumentsRepository: EmployeeDocumentsRepository) :
        AbstractEmployeeAPIRouteController<EmployeeDocumentsEntity, EmployeeDocumentsRepository>(employeeDocumentsRepository)