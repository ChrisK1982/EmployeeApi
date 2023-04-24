package com.example.employee_api.db.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDate

@Entity
@DynamicUpdate
@Table(name = "employee")
class EmployeeEntity : BaseEntity() {
    var firstName: String? = null
    var lastName: String? = null
    var email: String? = null
    var hireDate: LocalDate? = null
    var department: String? = null
    var jobTitle: String? = null
    var salary: Double? = null
}