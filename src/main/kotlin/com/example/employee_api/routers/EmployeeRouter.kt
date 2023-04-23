package com.example.employee_api.routers

import com.example.employee_api.db.entities.EmployeeEntity
import com.example.employee_api.db.repositories.EmployeesRepository
import jakarta.persistence.EntityNotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router

@Configuration
class EmployeeRouter(val employeesRepository: EmployeesRepository) {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(EmployeeRouter::class.java)
    }

    fun getAllEmployees(request: ServerRequest): ServerResponse {
        logger.info("Retrieving all employees.")
        return ServerResponse.ok().body(employeesRepository.findAll())
    }

    fun getEmployeeById(request: ServerRequest): ServerResponse {
        return try {
            val id = request.pathVariable("id").toLong()
            logger.info("A request to retrieve an employees is being processed for ID: $id")
            val employee = employeesRepository.findById(id)
            if (employee.isEmpty)
                ServerResponse.notFound().build()
            else
                ServerResponse.ok().body(employee)
        } catch (e: IllegalArgumentException) {
            logger.error("An error occurred when retrieving an employee: {}", e.message, e)
            ServerResponse.badRequest().body("An invalid id was used when retrieving an employee object.")
        }
    }

    fun createAnEmployee(request: ServerRequest): ServerResponse {
        return try {
            logger.info("Processing a request to create a new employee.")
            ServerResponse.ok().body(employeesRepository.save(request.body(EmployeeEntity::class.java)))
        } catch (e: Exception) {
            logger.error("An error occurred while attempting to save a new employee to the DB: {}", e.message, e)
            ServerResponse.badRequest().body("An invalid create employee request was sent. Please try again later.")
        }
    }

    fun updateAnEmployee(request: ServerRequest): ServerResponse {
        return try {
            logger.info("Processing a request to update an employee.")
            ServerResponse.ok().body(employeesRepository.save(request.body(EmployeeEntity::class.java)))
        } catch (e: Exception) {
            logger.error("An error occurred while attempting to update an employee: {}", e.message, e)
            ServerResponse.badRequest().body("An invalid update employee request was sent. Please try again later.")
        }
    }

    fun deleteAnEmployee(request: ServerRequest): ServerResponse {
        return try {
            val id = request.pathVariable("id").toLong()
            val deleted = employeesRepository.findById(id)

            deleted.ifPresentOrElse(
                { employeesRepository.delete(it) },
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

    fun routes(): RouterFunction<ServerResponse> = router {
        GET("/") { serverRequest -> getAllEmployees(serverRequest) }
        GET("/{id}") { serverRequest -> getEmployeeById(serverRequest) }
        POST("/create") { serverRequest: ServerRequest -> createAnEmployee(serverRequest) }
        PUT("/update") { serverRequest: ServerRequest -> updateAnEmployee(serverRequest) }
        DELETE("/{id}") { serverRequest: ServerRequest -> deleteAnEmployee(serverRequest) }
    }
}