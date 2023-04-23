package com.example.employee_api.db.repositories

import com.example.employee_api.db.entities.EmployeeEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeesRepository : CrudRepository<EmployeeEntity, Long> {}