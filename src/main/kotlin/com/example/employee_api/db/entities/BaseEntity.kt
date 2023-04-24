package com.example.employee_api.db.entities

import jakarta.persistence.*
import org.hibernate.annotations.DynamicUpdate
import java.io.Serializable
import java.time.LocalDate


@DynamicUpdate
@MappedSuperclass
open class BaseEntity : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, insertable = false)
    var id: Long? = null

    @Column(name = "created_date", updatable = false)
    var createdData: LocalDate? = LocalDate.now()

    @Column(name = "modified_date", updatable = true)
    var modificationDate: LocalDate? = LocalDate.now()
}