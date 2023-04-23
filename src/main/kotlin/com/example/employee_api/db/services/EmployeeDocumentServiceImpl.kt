package com.example.employee_api.db.services

import com.example.employee_api.db.entities.EmployeeDocumentsEntity
import com.example.employee_api.db.repositories.EmployeeDocumentsRepository
import jakarta.persistence.EntityNotFoundException
import org.hibernate.service.spi.ServiceException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EmployeeDocumentServiceImpl @Autowired constructor(
    private val repository: EmployeeDocumentsRepository
) : EmployeeDocumentsBaseService {

    companion object {
        const val MESSAGE_NOT_FOUND = "There is no member found with id: "
    }

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @Throws(ServiceException::class)
    override fun create(entity: EmployeeDocumentsEntity): EmployeeDocumentsEntity? {
        return try {
            repository.save(entity)
        } catch (e: Exception) {
            logServiceRelatedError("An error occurred while saving the new employee", e)
            null
        }
    }

    @Throws(ServiceException::class)
    override fun update(id: Long, entity: EmployeeDocumentsEntity): EmployeeDocumentsEntity? {
        return try {
            entity.id = id
            repository.save(entity)
        } catch (e: Exception) {
            logServiceRelatedError("An error occurred while updating the employee", e)
            null
        }
    }

    @Throws(ServiceException::class)
    override fun findById(id: Long): EmployeeDocumentsEntity? {
        return try {
            findOriginalEntityOrThrowEntityNotFoundException(id)
        } catch (e: Exception) {
            logServiceRelatedError("An error occurred while locating an employee by ID -> $id", e)
            null
        }
    }

    @Throws(ServiceException::class)
    override fun findAll(): MutableList<EmployeeDocumentsEntity>? {
        return try {
            repository.findAll().toMutableList()
        } catch (e: Exception) {
            logServiceRelatedError("An error occurred while retrieving all users", e)
            null
        }
    }

    @Throws(ServiceException::class)
    override fun deleteById(id: Long): EmployeeDocumentsEntity? {
        return try {
            val originalDocument = findOriginalEntityOrThrowEntityNotFoundException(id)
            repository.deleteById(id)
            originalDocument
        } catch (e: Exception) {
            logServiceRelatedError("An error occurred while deleting an employee by ID: $id", e)
            null
        }
    }

    override fun logServiceRelatedError(error: String, e: Exception) {
        log.error("$error: {}", e.message, e)
    }

    override fun findOriginalEntityOrThrowEntityNotFoundException(id: Long): EmployeeDocumentsEntity? = run {
        repository.findById(id)
            .orElseThrow { EntityNotFoundException(MESSAGE_NOT_FOUND + id) }
    }
}