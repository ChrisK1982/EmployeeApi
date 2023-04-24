package com.example.employee_api.dto

import java.time.LocalDate

data class Employee(
        val id: Long,
        val firstName: String? = null,
        val lastName: String? = null,
        val email: String? = null,
        val hireDate: LocalDate? = null,
        val department: String? = null,
        val jobTitle: String? = null,
        val salary: Double? = null
)
