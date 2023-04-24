package com.example.employee_api.routers

import com.example.employee_api.dto.StandardResponse
import jakarta.persistence.EntityNotFoundException
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.function.ServerResponse
import java.util.*

abstract class AbstractEmployeeRouter<T : Any, R : JpaRepository<T, Long>>(private val repository: R) {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(AbstractEmployeeRouter::class.java)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllEntities(): List<T> {
        logger.info("Retrieving all entities.")
        return repository.findAll()
    }

    @GetMapping(path = ["/{id}"],produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getEntityById(@PathVariable id: Long): StandardResponse<Optional<T>> {
        return try {
            logger.info("A request to retrieve an entity is being processed for ID: $id")
            val employee = repository.findById(id)
            if (employee.isEmpty)
                StandardResponse(Optional.empty(), "No entity for ID: $id could be located.")
            else
                StandardResponse(employee, "An entity for ID: $id was located.")
        } catch (e: IllegalArgumentException) {
            logger.error("An error occurred when retrieving an entity: {}", e.message, e)
            StandardResponse(Optional.empty(), "An error occurred while attempting to retrieve your entity.")
        }
    }

    @PostMapping(path = ["/create"],produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createAnEntity(@RequestBody @Valid entity: T): StandardResponse<Optional<T>> = run { addNewEntity(entity) }

    @PutMapping(path = ["/update"],produces = [MediaType.APPLICATION_JSON_VALUE])
    fun updateAnEntity(@RequestBody @Valid entity: T): StandardResponse<Optional<T>> = run { addNewEntity(entity) }

    private fun addNewEntity(entity: T): StandardResponse<Optional<T>> {
        return try {
            logger.info("Processing a request to create a new entity.")
            StandardResponse(Optional.of(repository.save(entity)), "Your request was serviced succesfully.")
        } catch (e: Exception) {
            logger.error("An error occurred while attempting to save a new entity to the DB: {}", e.message, e)
            StandardResponse(Optional.empty(), "An invalid create employee request was sent. Please try again later.")
        }
    }

    @DeleteMapping(path = ["/{id}"],produces = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteAnEntity(@PathVariable id: Long): StandardResponse<Optional<T>> {
        return try {
            val deleted = repository.findById(id)

            if (deleted.isEmpty)
                throw EntityNotFoundException("There was no entity found for ID: $id")
            else
                repository.deleteById(id)

            StandardResponse(deleted, "An entity for ID: $id was successfully deleted.")
        } catch (e: EntityNotFoundException) {
            logger.error("${e.message}")
            StandardResponse(Optional.empty(), "${e.message}")
        } catch (e: Exception) {
            logger.error("An error occurred while attempting to delete an entity: {}", e.message, e)
            StandardResponse(
                Optional.empty(),
                "An error occurred while attempting to delete the entity for ID: $id. Please try again later."
            )
        }
    }
}