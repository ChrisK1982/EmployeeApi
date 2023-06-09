package com.example.employee_api.db.entities

import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicUpdate

@Entity
@DynamicUpdate
@Table(name = "employee_documents")
class EmployeeDocumentsEntity : BaseEntity() {
    @ElementCollection
    var documents: Map<Long, String>? = null
}