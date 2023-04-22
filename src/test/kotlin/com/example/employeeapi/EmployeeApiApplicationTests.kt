package com.example.employeeapi

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
@AutoConfigureMockMvc
class EmployeeApiApplicationTests(@Autowired val mockMvc: MockMvc) {

    @Test
    @WithMockUser(username = "test", password = "test", roles = ["USER"])
    fun `When the root endpoint is called, then the api responds with 'OK'`() {
        mockMvc.perform(get("/api"))
                .andExpect(status().isOk)
                .andExpect(content().string("OK"))
    }

    @Test
    fun `When the api is called without an included user, then the api responds with an 401 Unauthorized Response`() {
        mockMvc.perform(get("/api"))
                .andExpect(status().isUnauthorized)
    }
}
