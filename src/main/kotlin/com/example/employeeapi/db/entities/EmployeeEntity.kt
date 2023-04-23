package com.example.employeeapi.db.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDate

@Entity
@DynamicUpdate
@Table(name = "employee")
class EmployeeEntity : BaseEntity() {
    @Column(name = "first_name")
    var firstName: String? = null
    @Column(name = "last_name")
    var lastName: String? = null
    @Column(name = "email")
    var email: String? = null
    @Column(name = "hire_date")
    val hireDate: LocalDate? = null
    @Column(name = "department")
    val department: String? = null
    @Column(name = "job_title")
    val jobTitle: String? = null
    @Column(name = "salary")
    val salary: Double? = null
}