package com.example.employee_api.routers

import com.example.employee_api.db.entities.EmployeeEntity
import com.example.employee_api.db.repositories.EmployeesRepository
import com.example.employee_api.dto.StandardResponse
import com.example.employee_api.utils.LocalDateTypeAdapter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.io.File
import java.io.FileReader
import java.time.LocalDate

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AbstractEmployeeRouteControllerTest
@Autowired constructor(private var mockMvc: MockMvc, private var employeesRepository: EmployeesRepository) {

    companion object {
        private val gson = GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter())
            .create()

        lateinit var testUsers: Array<EmployeeEntity>
        lateinit var testUsersJson: String

        @JvmStatic
        @BeforeAll
        fun readTestUsers(): Unit {
            val arrayOfEmployeesType = object : TypeToken<Array<EmployeeEntity>>() {}.type
            val testUsersFile: File = ClassPathResource("test_users/test_users.json").file
            testUsersJson = FileReader(testUsersFile).readText()

            testUsers = gson.fromJson(testUsersJson, arrayOfEmployeesType)
        }
    }

    @AfterEach
    fun removeEntities() {
        employeesRepository.deleteAll()
    }

    @Test
    fun `Given there are no employees in the db, when a get all employees request is made, an empty employee array is returned`() {
        mockMvc.perform(
            get("/api/employees")
                .with(httpBasic("user", "password"))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.entity").isArray)
            .andExpect(jsonPath("$.entity").isEmpty)
    }

    @Test
    fun `Given there are no users in the DB, when a find user by id request is sent, then it responds with an empty response`() {
        mockMvc.perform(
            get("/api/employees/1")
                .with(httpBasic("user", "password"))
        )
            .andExpect(status().isOk)
            .andExpect(content().json(Companion.gson.toJson(StandardResponse(null, "No entity for ID: 1 could be located."))))
    }

    @Test
    fun `Given a user is loaded into the DB, when a find user by id request is made with a valid id, then the user is returned`() {
        val testUserToAdd = testUsers[0]
        employeesRepository.save(testUserToAdd)

        mockMvc.perform(get("/api/employees/1")
            .with(httpBasic("user", "password"))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isMap)
    }

    @Test
    fun `Given there is at least one employee in the db, when a get all employees request is made, an array with one employee is returned`() {
        val testUserToAdd = testUsers[0]
        employeesRepository.save(testUserToAdd)

        mockMvc.perform(
            get("/api/employees")
                .with(httpBasic("user", "password"))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.entity[0].lastName").value(testUserToAdd.lastName))
    }

    @Test
    fun `Given an employee entity, when a create employee request is made, then the api responds with a success response message`() {
        val testUserToAdd = testUsers[0]

        mockMvc.perform(
            post("/api/employees/create")
                .with(httpBasic("user", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(testUserToAdd))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.entity.email").value(testUserToAdd.email))
    }

    @Test
    fun `Given an employee entity exists in the db, when an update employee request is made with a valid employee, then the api responds with an updated entity message`() {
        val testUserToUpdate = testUsers[0]
        employeesRepository.save(testUserToUpdate)

        val updatedFirstName = "NewFirstName"
        testUserToUpdate.firstName = updatedFirstName

        mockMvc.perform(
            put("/api/employees/update")
                .with(httpBasic("user", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(testUserToUpdate))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.entity.firstName").value(updatedFirstName))
    }

    @Test
    fun `Given there are no employees in the db, when a batch create request is made, then the api adds all employees to the db`() {
        val testEmployeeToCheck = testUsers[1]

        mockMvc.perform(
            post("/api/employees/batch/create")
                .with(httpBasic("user", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUsersJson)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.entity[1].email").value(testEmployeeToCheck.email))
    }

    @Test
    fun `Given an employee in the DB, when a partial update request is made, partially updates the entity`() {
        employeesRepository.save(testUsers[0])
        val originalEmployee = employeesRepository.findById(1)
        val partialEmployeeUpdate = EmployeeEntity()
        partialEmployeeUpdate.id = originalEmployee.get().id
        partialEmployeeUpdate.firstName = "NewFirstName"

        mockMvc.perform(
            patch("/api/employees/update")
                .with(httpBasic("user", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(partialEmployeeUpdate))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.entity.email").value(originalEmployee.get().email))
    }

    @Test
    fun `Given there are employees in the DB, when a batch update request is made, the API responds with updated entities`() {
        employeesRepository.saveAll(testUsers.toMutableList())
        val testEmployeesFromDb: MutableList<EmployeeEntity> = employeesRepository.findAll()
        val employee1 = EmployeeEntity()
        employee1.id = testEmployeesFromDb[0].id
        employee1.email = testEmployeesFromDb[1].email

        val employee2 = EmployeeEntity()
        employee2.id = testEmployeesFromDb[1].id
        employee2.email = testEmployeesFromDb[0].email

        val employeeUpdateDTOs = mutableListOf(employee1, employee2)
        val testEmployeeToCheck = testUsers[0]

        mockMvc.perform(
            patch("/api/employees/batch/update")
                .with(httpBasic("user", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(employeeUpdateDTOs))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.entity[1].email").value(testEmployeeToCheck.email))

        val updatedEmployee = employee1.id?.let { employeesRepository.findById(it) }
        assertEquals(testUsers[0].firstName, updatedEmployee?.get()?.firstName)
    }

    @Test
    fun batchUpdateEntities() {
    }

    @Test
    fun deleteAnEntity() {
    }

    @Test
    fun batchDeleteEntities() {
    }
}