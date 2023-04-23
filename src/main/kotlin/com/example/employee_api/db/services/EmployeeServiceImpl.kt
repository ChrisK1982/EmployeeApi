package com.example.employee_api.db.services

import com.example.employee_api.db.entities.EmployeeEntity
import com.example.employee_api.db.repositories.EmployeesRepository
import jakarta.persistence.EntityNotFoundException
import org.hibernate.service.spi.ServiceException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EmployeeServiceImpl @Autowired constructor(
    private val repository: EmployeesRepository
) : EmployeeBaseService {

    companion object {
        const val MESSAGE_NOT_FOUND = "There is no member found with id: "
    }

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun create(entity: EmployeeEntity): EmployeeEntity? {
        return try {
            repository.save(entity)
        } catch (e: Exception) {
            logServiceRelatedError("An error occurred while saving the new employee", e)
            null
        }
    }

    override fun update(id: Long, entity: EmployeeEntity): EmployeeEntity? {
        return try {
            repository.save(entity)
        } catch (e: Exception) {
            logServiceRelatedError("An error occurred while updating the employee", e)
            null
        }
    }

    override fun findById(id: Long): EmployeeEntity? {
        return try {
            findOriginalEntityOrThrowEntityNotFoundException(id)
        } catch (e: Exception) {
            logServiceRelatedError("An error occurred while locating an employee by ID -> $id", e)
            null
        }
    }

    override fun findAll(): MutableList<EmployeeEntity>? {
        return try {
            repository.findAll().toMutableList()
        } catch (e: Exception) {
            logServiceRelatedError("An error occurred while retrieving all users", e)
            null
        }
    }

    override fun deleteById(id: Long): EmployeeEntity? {
        return try {
            val originalEntity = findOriginalEntityOrThrowEntityNotFoundException(id)
            repository.deleteById(id)
            originalEntity
        } catch (e: Exception) {
            logServiceRelatedError("An error occurred while deleting an employee by ID: $id", e)
            null
        }
    }

    override fun findOriginalEntityOrThrowEntityNotFoundException(id: Long): EmployeeEntity = run {
        repository.findById(id)
            .orElseThrow { EntityNotFoundException(MESSAGE_NOT_FOUND + id) }
    }

    override fun logServiceRelatedError(error: String, e: Exception) {
        log.error("$error: {}", e.message, e)
    }
}