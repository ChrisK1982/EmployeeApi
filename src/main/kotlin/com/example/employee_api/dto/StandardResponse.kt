package com.example.employee_api.dto

data class StandardResponse<T>(
    val entity: T,
    val message: String
)
