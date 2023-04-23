package com.example.employee_api

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest
class EmployeeApiApplicationTests(@Autowired val mockMvc: MockMvc) {

    @Test
    @WithMockUser(username = "user", password = "password", roles = ["EmployeeAPIUser"])
    fun `When the root endpoint is called with valid user credentials, then the api responds with 'OK'`() {
        mockMvc.perform(get("/api"))
            .andExpect(status().isOk)
            .andExpect(content().string("OK"))
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = ["InvalidAPIUser"])
    fun `When the root endpoint is called with invalid user role, then the api responds with 'Forbidden'`() {
        mockMvc.perform(get("/api"))
            .andExpect(status().isForbidden)
    }

    @Test
    fun `When the api is called without an included user, then the api responds with an 401 Unauthorized Response`() {
        mockMvc.perform(get("/api"))
            .andExpect(status().isUnauthorized)
    }
}
