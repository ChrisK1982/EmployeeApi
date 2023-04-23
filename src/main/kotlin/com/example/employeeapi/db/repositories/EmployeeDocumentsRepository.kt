package com.example.employeeapi.db.repositories

import com.example.employeeapi.db.entities.EmployeeDocumentsEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeeDocumentsRepository : CrudRepository<EmployeeDocumentsEntity, Long> {}