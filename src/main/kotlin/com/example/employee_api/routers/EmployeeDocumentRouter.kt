package com.example.employee_api.routers

import com.example.employee_api.db.entities.EmployeeDocumentsEntity
import com.example.employee_api.db.repositories.EmployeeDocumentsRepository
import jakarta.persistence.EntityNotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router

@Configuration
class EmployeeDocumentRouter(val employeesDocumentsRepository: EmployeeDocumentsRepository) {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(EmployeeDocumentRouter::class.java)
    }

    fun getAllEmployeesDocuments(request: ServerRequest): ServerResponse {
        logger.info("Retrieving all employees.")
        return ServerResponse.ok().body(employeesDocumentsRepository.findAll())
    }

    fun getEmployeeDocumentsById(request: ServerRequest): ServerResponse {
        return try {
            val id = request.pathVariable("id").toLong()
            logger.info("A request to retrieve an employees is being processed for ID: $id")
            val employee = employeesDocumentsRepository.findById(id)
            if (employee.isEmpty)
                ServerResponse.notFound().build()
            else
                ServerResponse.ok().body(employee)
        } catch (e: IllegalArgumentException) {
            logger.error("An error occurred when retrieving an employee: {}", e.message, e)
            ServerResponse.badRequest().body("An invalid id was used when retrieving an employee object.")
        }
    }

    fun createAnEmployeesDocuments(request: ServerRequest): ServerResponse {
        return try {
            logger.info("Processing a request to create a new employee.")
            ServerResponse.ok().body(employeesDocumentsRepository.save(request.body(EmployeeDocumentsEntity::class.java)))
        } catch (e: Exception) {
            logger.error("An error occurred while attempting to save a new employee to the DB: {}", e.message, e)
            ServerResponse.badRequest().body("An invalid create employee request was sent. Please try again later.")
        }
    }

    fun updateAnEmployeesDocuments(request: ServerRequest): ServerResponse {
        return try {
            logger.info("Processing a request to update an employee.")
            ServerResponse.ok().body(employeesDocumentsRepository.save(request.body(EmployeeDocumentsEntity::class.java)))
        } catch (e: Exception) {
            logger.error("An error occurred while attempting to update an employee: {}", e.message, e)
            ServerResponse.badRequest().body("An invalid update employee request was sent. Please try again later.")
        }
    }

    fun deleteAnEmployeesDocuments(request: ServerRequest): ServerResponse {
        return try {
            val id = request.pathVariable("id").toLong()
            val deleted = employeesDocumentsRepository.findById(id)

            deleted.ifPresentOrElse(
                { employeesDocumentsRepository.delete(it) },
                { throw EntityNotFoundException("There was no employee found for ID: $id") }
            )
            ServerResponse.ok().body(deleted)
        } catch (e: EntityNotFoundException) {
            logger.error("${e.message}")
            ServerResponse.badRequest().body("${e.message}")
        } catch (e: Exception) {
            logger.error("An error occurred while attempting to delete an employee: {}", e.message, e)
            ServerResponse.badRequest().build()
        }
    }

    @Bean
    fun employeeDocumentsApiRoutes(): RouterFunction<ServerResponse> = router {
        GET("/api/documents/") { serverRequest -> getAllEmployeesDocuments(serverRequest) }
        GET("/api/documents/{id}") { serverRequest -> getEmployeeDocumentsById(serverRequest) }
        POST("/api/documents/create") { serverRequest: ServerRequest -> createAnEmployeesDocuments(serverRequest) }
        PUT("/api/documents/update") { serverRequest: ServerRequest -> updateAnEmployeesDocuments(serverRequest) }
        DELETE("/api/documents/{id}") { serverRequest: ServerRequest -> deleteAnEmployeesDocuments(serverRequest) }
    }
}