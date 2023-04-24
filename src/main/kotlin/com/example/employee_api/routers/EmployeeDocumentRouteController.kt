package com.example.employee_api.routers

import com.example.employee_api.db.entities.EmployeeDocumentsEntity
import com.example.employee_api.db.repositories.EmployeeDocumentsRepository
import com.example.employee_api.dto.StandardResponse
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Async
import org.springframework.web.bind.annotation.*
import java.util.*

@Async
@RestController
@RequestMapping(path = ["/api/documents"])
class EmployeeDocumentRouteController(val employeeDocumentsRepository: EmployeeDocumentsRepository) :
    AbstractEmployeeAPIRouteController<EmployeeDocumentsEntity, EmployeeDocumentsRepository>(employeeDocumentsRepository) {

    @PatchMapping(path = ["/update/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun updateAnEmployee(
        @PathVariable id: Long,
        @RequestBody updatedEmployeeDocumentsEntity: EmployeeDocumentsEntity
    ): StandardResponse<Optional<EmployeeDocumentsEntity>> {
        val original = employeeDocumentsRepository.findById(id)

        return if (original.isPresent && updatedEmployeeDocumentsEntity.documents != null) {
            val retrieved = original.get()
            retrieved.documents?.plus(updatedEmployeeDocumentsEntity.documents!!)

            StandardResponse(
                Optional.of(employeeDocumentsRepository.save(retrieved)),
                "An employee was successfully updated."
            )
        } else
            StandardResponse(Optional.empty(), "No Employee is in the system for ID: $id")
    }

    @PatchMapping(path = ["/batch/update"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun updateAnEmployee(@RequestBody updatedEmployeeDocuments: MutableList<EmployeeDocumentsEntity>): StandardResponse<Optional<MutableList<EmployeeDocumentsEntity>>> {
        val originalEmployeeDocumentEntities: MutableList<EmployeeDocumentsEntity> =
            employeeDocumentsRepository.findAllById(updatedEmployeeDocuments.map { employeeDocumentsEntity -> employeeDocumentsEntity.id })

        for (og in originalEmployeeDocumentEntities) {
            for (up in updatedEmployeeDocuments) {
                if (og.id == up.id && up.documents != null) {
                    og.documents?.plus(up.documents!!)
                }
            }
        }

        return StandardResponse(
            Optional.of(employeeDocumentsRepository.saveAll(originalEmployeeDocumentEntities)),
            "A batch update employees request was successfully processed."
        )
    }
}