package com.example.employee_api.db.services

import com.example.employee_api.db.entities.BaseEntity
import jakarta.persistence.EntityNotFoundException
import org.hibernate.service.spi.ServiceException
import org.springframework.dao.DataIntegrityViolationException
import java.io.Serializable

interface BaseService<E : BaseEntity> : Serializable {

    fun create(entity: E): E?

    fun update(id: Long, entity: E): E?

    fun findById(id: Long): E?

    fun findAll(): MutableList<E>?

    fun deleteById(id: Long): E?

    fun logServiceRelatedError(error: String, e: Exception)
    fun findOriginalEntityOrThrowEntityNotFoundException(id: Long): E?
}