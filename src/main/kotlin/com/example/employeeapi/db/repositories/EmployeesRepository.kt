package com.example.employeeapi.db.repositories

import com.example.employeeapi.db.entities.EmployeeEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeesRepository : CrudRepository<EmployeeEntity, Long> {}