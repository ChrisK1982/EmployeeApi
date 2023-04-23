package com.example.employee_api.db.entities

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import java.io.Serializable
import java.time.LocalDateTime

@MappedSuperclass
open class BaseEntity: Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, insertable = false)
    var id: Long? = null

    @Column(name = "created_date", updatable = false)
    var createdData: LocalDateTime? = null

    @Column(name = "modified_date", updatable = true)
    var modificationDate: LocalDateTime? = LocalDateTime.now()
}