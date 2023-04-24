package com.example.employee_api.db.repositories

import com.example.employee_api.db.entities.EmployeeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeesRepository : JpaRepository<EmployeeEntity, Long> {}