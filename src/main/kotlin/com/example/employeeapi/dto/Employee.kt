package com.example.employeeapi.dto

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.messaging.handler.annotation.Payload
import java.time.LocalDate
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [EmployeeIdValidator::class])
annotation class EmployeeId(
        val message: String = "Invalid employee ID",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)

class EmployeeIdValidator : ConstraintValidator<EmployeeId, String> {

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return value != null && value.matches(Regex("EMP-([a-zA-Z]){2,6}-\\d{4}-\\d{4}"))
    }

}

data class Employee(
        @field:EmployeeId
        val id: String,
        val firstName: String? = null,
        val lastName: String? = null,
        val email: String? = null,
        val hireDate: LocalDate? = null,
        val department: String? = null,
        val jobTitle: String? = null,
        val salary: Double? = null
)



