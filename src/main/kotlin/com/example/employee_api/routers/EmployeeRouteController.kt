package com.example.employee_api.routers

import com.example.employee_api.db.entities.EmployeeEntity
import com.example.employee_api.db.repositories.EmployeesRepository
import com.example.employee_api.dto.StandardResponse
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.reflect.full.memberProperties

@RestController
@RequestMapping(path = ["/api/employees"])
class EmployeeRouteController(val employeesRepository: EmployeesRepository) :
    AbstractEmployeeAPIRouteController<EmployeeEntity, EmployeesRepository>(employeesRepository) {

    @PatchMapping(path = ["/update/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun updateAnEmployee(
        @PathVariable id: Long,
        @RequestBody updatedEmployee: EmployeeEntity
    ): StandardResponse<Optional<EmployeeEntity>> {
        val original = employeesRepository.findById(id)

        return if (original.isPresent) {
            val retrieved = original.get()
            mapPartialUpdateToEntity(retrieved, updatedEmployee)
            StandardResponse(
                Optional.of(employeesRepository.save(retrieved)),
                "An employee was successfully updated."
            )
        } else
            StandardResponse(Optional.empty(), "No Employee is in the system for ID: $id")
    }

    @PatchMapping(path = ["/batch/update"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun updateAnEmployee(@RequestBody updatedEmployees: MutableList<EmployeeEntity>): StandardResponse<Optional<MutableList<EmployeeEntity>>> {
        val originalEmployeeEntities =
            employeesRepository.findAllById(updatedEmployees.map { employeeEntity -> employeeEntity.id })

        for (og in originalEmployeeEntities) {
            for (up in updatedEmployees) {
                if (og.id == up.id) {
                    mapPartialUpdateToEntity(og, up)
                }
            }
        }

        return StandardResponse(
            Optional.of(employeesRepository.saveAll(originalEmployeeEntities)),
            "A batch update employees request was successfully processed."
        )
    }

    fun mapPartialUpdateToEntity(original: EmployeeEntity, updatedEmployee: EmployeeEntity) {
        for (property in EmployeeEntity::class.memberProperties) {
            if (property.get(updatedEmployee) != null) {
                when (property.name) {
                    "firstName" -> original.firstName = updatedEmployee.firstName
                    "lastName" -> original.lastName = updatedEmployee.lastName
                    "email" -> original.email = updatedEmployee.email
                    "department" -> original.department = updatedEmployee.department
                    "jobTitle" -> original.jobTitle = updatedEmployee.jobTitle
                    "salary" -> original.salary = updatedEmployee.salary
                }
            }
        }
    }
}