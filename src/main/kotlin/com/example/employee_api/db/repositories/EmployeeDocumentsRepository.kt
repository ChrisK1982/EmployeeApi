package com.example.employee_api.db.repositories

import com.example.employee_api.db.entities.EmployeeDocumentsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeeDocumentsRepository : JpaRepository<EmployeeDocumentsEntity, Long> {}