package com.example.employee_api.routers

import com.example.employee_api.dto.StandardResponse
import com.fasterxml.jackson.annotation.JsonMerge
import jakarta.persistence.EntityNotFoundException
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Async
import org.springframework.web.bind.annotation.*
import java.util.*

@Async
abstract class AbstractEmployeeAPIRouteController<T : Any, R : JpaRepository<T, Long>>(private val repository: R) {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(AbstractEmployeeAPIRouteController::class.java)
    }

    @ExceptionHandler(java.lang.Exception::class)
    fun handleAllOtherErrors(e: Exception) : StandardResponse<Optional<T>> {
        return StandardResponse(Optional.empty(), "An error occurred while attempting to service your request: ${e.cause}")
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllEntities(): StandardResponse<Optional<List<T>>> {
        logger.info("Retrieving all entities.")
        return StandardResponse(Optional.of(repository.findAll()), "All available entities successfully retrieved.")
    }

    @GetMapping(path = ["/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
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

    @PostMapping(path = ["/create"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createAnEntity(@RequestBody @Valid entity: T): StandardResponse<Optional<T>> = run { saveEntity(entity) }

    @PostMapping(path = ["/batch/create"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun batchCreateEntities(@RequestBody @Valid entities: MutableList<T>): StandardResponse<Optional<List<T>>> =
        run { saveEntities(entities) }

    @PatchMapping(path = ["/update"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun updateAnEntity(@RequestBody @Valid @JsonMerge entity: T): StandardResponse<Optional<T>> = run {
        saveEntity(entity)
    }

    @PatchMapping(path = ["/batch/update"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun batchUpdateEntities(@RequestBody @Valid @JsonMerge entities: MutableList<T>): StandardResponse<Optional<List<T>>> =
        run { saveEntities(entities) }

    private fun saveEntities(entities: MutableList<T>): StandardResponse<Optional<List<T>>> {
        return try {
            StandardResponse(Optional.of(repository.saveAll(entities)), "New entities successfully saved.")
        } catch (e: Exception) {
            logger.error("An error occurred while saving entities to the DB: {}", e.message, e)
            StandardResponse(Optional.empty(), "An error occurred while attempted to save the entities to the DB.")
        }
    }

    private fun saveEntity(entity: T): StandardResponse<Optional<T>> {
        return try {
            logger.info("Processing a request to create a new entity.")
            StandardResponse(Optional.of(repository.save(entity)), "Your request was serviced succesfully.")
        } catch (e: Exception) {
            logger.error("An error occurred while attempting to save a new entity to the DB: {}", e.message, e)
            StandardResponse(Optional.empty(), "An invalid create employee request was sent. Please try again later.")
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

    @DeleteMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun batchDeleteEntities(@RequestBody entityIds: MutableList<Long>): StandardResponse<Optional<List<T>>> {
        return try {
            val entitiesToDelete = Optional.of(repository.findAllById(entityIds))
            repository.deleteAllById(entityIds)
            StandardResponse(entitiesToDelete, "Batch delete request was processed successfully.")
        } catch (e: Exception) {
            logger.error("An error occurred during a batch deletion operation: {}", e.message, e)
            StandardResponse(Optional.empty(), "An error occurred while processing the batch deletion request for IDs: $entityIds")
        }
    }
}