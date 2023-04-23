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

    @Throws(ServiceException::class)
    override fun create(entity: EmployeeEntity): EmployeeEntity? {
        try {
            return repository.save(entity)
        } catch (e: Exception) {
            logAndThrowServiceException("An error occurred while saving the new employee", e)
        }
        return null
    }

    @Throws(ServiceException::class)
    override fun update(id: Long, entity: EmployeeEntity): EmployeeEntity? {
        try {
            return repository.save(entity)
        } catch (e: Exception) {
            logAndThrowServiceException("An error occurred while updating the employee", e)
        }
        return null
    }

    @Throws(ServiceException::class)
    override fun findById(id: Long): EmployeeEntity? {
        try {
            return repository.findById(id).orElseThrow { EntityNotFoundException(MESSAGE_NOT_FOUND + id) }
        } catch (e: Exception) {
            logAndThrowServiceException("An error occurred while locating an employee by ID -> $id", e)
        }
        return null
    }

    @Throws(ServiceException::class)
    override fun findAll(): MutableList<EmployeeEntity>? {
        try {
            return repository.findAll().toMutableList()
        } catch (e: Exception) {
            logAndThrowServiceException("An error occurred while retrieving all users", e)
        }
        return null
    }

    @Throws(ServiceException::class)
    override fun deleteById(id: Long): EmployeeEntity? {
        try {
            val originalEntity = repository.findById(id)
                .orElseThrow { EntityNotFoundException(MESSAGE_NOT_FOUND + id) }
            repository.deleteById(id)
            return originalEntity
        } catch (e: Exception) {
            logAndThrowServiceException("An error occurred while deleting an employee by ID: $id", e)
        }
        return null
    }

    @Throws(ServiceException::class)
    fun logAndThrowServiceException(error: String, e: Exception) {
        log.error("$error: {}", e.message, e)
        throw ServiceException(error, e)
    }
}