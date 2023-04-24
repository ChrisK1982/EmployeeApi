package com.example.employee_api.routers

import com.example.employee_api.db.entities.EmployeeEntity
import com.example.employee_api.db.repositories.EmployeesRepository
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api/employees"])
class EmployeeRouter(employeesRepository: EmployeesRepository) :
    AbstractEmployeeRouter<EmployeeEntity, EmployeesRepository>(employeesRepository)