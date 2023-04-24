package com.example.employee_api.routers

import com.example.employee_api.dto.StandardResponse
import jakarta.persistence.EntityNotFoundException
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Async
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*
import kotlin.math.log

@Async
abstract class AbstractEmployeeRouter<T : Any, R : JpaRepository<T, Long>>(private val repository: R) {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(AbstractEmployeeRouter::class.java)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllEntities(): List<T> {
        logger.info("Retrieving all entities.")
        return repository.findAll()
    }

    @GetMapping(path = ["/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getEntityById(@PathVariable id: Long): StandardResponse<Optional<T>> {
        return try {
            logger.info("A request to retrieve an entity is being processed for ID: $id")
            val employee = repository.findById(id)
            if (employee.isEmpty)
                throw EntityNotFoundException("No entity for ID: $id could be located.")
            else
                StandardResponse(employee, "An entity for ID: $id was located.")
        } catch (e: EntityNotFoundException) {
            logger.error("An error occurred while attempting to locate an entity for ID -> $id: {}", e.message, e)
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "An error occurred while attempting to locate entity for ID -> $id: ${e.message}"
            )
        } catch (e: IllegalArgumentException) {
            logger.error("An error occurred when retrieving an entity: {}", e.message, e)
            throw ResponseStatusException(
                HttpStatus.BAD_GATEWAY,
                "An error occurred while attempting to retrieve your entity with ID: $id"
            )
        }
    }

    @PostMapping(path = ["/create"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createAnEntity(@RequestBody @Valid entity: T): StandardResponse<Optional<T>> = run { addNewEntity(entity) }

    @PostMapping(path = ["/batch/create"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun batchCreateEntities(@RequestBody @Valid entities: List<T>): StandardResponse<Optional<List<T>>> =
        run { saveEntities(entities) }

    @PutMapping(path = ["/update"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun updateAnEntity(@RequestBody @Valid entity: T): StandardResponse<Optional<T>> = run { addNewEntity(entity) }

    @PostMapping(path = ["/batch/update"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun batchUpdateEntities(@RequestBody @Valid entities: List<T>): StandardResponse<Optional<List<T>>> =
        run { saveEntities(entities) }

    private fun saveEntities(entities: List<T>): StandardResponse<Optional<List<T>>> {
        return try {
            StandardResponse(Optional.of(repository.saveAll(entities)), "New entities successfully saved.")
        } catch (e: Exception) {
            logger.error("An error occurred while saving entities to the DB: {}", e.message, e)
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An error occurred while attempted to save entities to the DB."
            )
        }
    }

    private fun addNewEntity(entity: T): StandardResponse<Optional<T>> {
        return try {
            logger.info("Processing a request to create a new entity.")
            StandardResponse(Optional.of(repository.save(entity)), "Your request was serviced successfully.")
        } catch (e: IllegalArgumentException) {
            logger.error("An error occurred while attempting to save a new entity to the DB: {}", e.message, e)
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "An error occurred while attempting to save your entity. ${e.message}"
            )
        } catch (e: Exception) {
            logger.error("An error occurred while saving an entity to the DB: {}", e.message, e)
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An error occurred while attempting to save an entity to the DB."
            )
        }
    }

    @DeleteMapping(path = ["/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteAnEntity(@PathVariable id: Long): StandardResponse<Optional<T>> {
        return try {
            val deleted = repository.findById(id)

            if (deleted.isEmpty)
                throw EntityNotFoundException("There was no entity found for ID: $id")
            else
                repository.deleteById(id)

            StandardResponse(deleted, "An entity for ID: $id was successfully deleted.")
        } catch (e: EntityNotFoundException) {
            logger.error("An error occurred while attempting to locate an entity for deletion: {}", e.message, e)
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "An error occurred while attempting to locate the entity for deletion: ${e.message}"
            )
        } catch (e: IllegalArgumentException) {
            logger.error(
                "An illegal argument exception occurred while attempting to delete an entity: {}",
                e.message,
                e
            )
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "An invalid argument was passed as part of a delete entity request: ${e.message}"
            )
        } catch (e: Exception) {
            logger.error("An error occurred while attempting to delete an entity: {}", e.message, e)
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An error occurred while attempting to delete the entity for ID: $id. Please try again later."
            )
        }
    }

    @DeleteMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun batchDeleteEntities(@RequestBody entityIds: List<Long>): StandardResponse<Optional<List<T>>> {
        return try {
            val entitiesToDelete = Optional.of(repository.findAllById(entityIds))
            repository.deleteAllById(entityIds)
            StandardResponse(entitiesToDelete, "Batch delete request was processed successfully.")
        } catch (e: EntityNotFoundException) {
            logger.error("An error occurred while attempting to locate an entities for deletion: {}", e.message, e)
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "An error occurred while attempting to locate entities for deletion: ${e.message}"
            )
        } catch (e: IllegalArgumentException) {
            logger.error("An illegal argument exception occurred while attempting to some entities: {}", e.message, e)
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "An invalid argument was passed as part of a batch delete entity request: ${e.message}"
            )
        } catch (e: Exception) {
            logger.error("An error occurred while attempting to delete an entity: {}", e.message, e)
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An error occurred while attempting to batch delete the entities. Please try again later."
            )
        }
    }
}