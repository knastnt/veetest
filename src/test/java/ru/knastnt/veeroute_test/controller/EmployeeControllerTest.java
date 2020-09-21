package ru.knastnt.veeroute_test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.A;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.knastnt.veeroute_test.EmployeesTestData;
import ru.knastnt.veeroute_test.model.Employee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.knastnt.veeroute_test.EmployeesTestData.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
class EmployeeControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getAll() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(EmployeeController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.content", hasSize(4)));
    }

    @Test
    void getAllPaged() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(EmployeeController.REST_URL + "?page=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    void getById() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(EmployeeController.REST_URL + "/" + EMPLOYEE1_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(EMPLOYEE1_ID)));
    }

    @Test
    void getByIdNotExist() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(EmployeeController.REST_URL + "/" + DELETED_EMPLOYEE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void update() throws Exception {
        Employee updated = new Employee(EMPLOYEE4);
        updated.setFirstName("Измененное имя");
        mvc.perform(MockMvcRequestBuilders.put(EmployeeController.REST_URL + "/" + EMPLOYEE4_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)));

        mvc.perform(MockMvcRequestBuilders.get(EmployeeController.REST_URL + "/" + EMPLOYEE4_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(EMPLOYEE4_ID)))
                .andExpect(jsonPath("$.firstName", is("Измененное имя")));
    }


    @Test
    void updateIncorrect() throws Exception {
        Employee updated = new Employee(EMPLOYEE4);
        mvc.perform(MockMvcRequestBuilders.put(EmployeeController.REST_URL + "/" + EMPLOYEE1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
        .andExpect(status().isBadRequest());
    }

    @Test
    void add() throws Exception {
        Employee newEmployee = getNew();
        ResultActions action = mvc.perform(MockMvcRequestBuilders.post(EmployeeController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newEmployee)))
                .andExpect(status().isCreated());

        Employee returned = objectMapper.readValue(action.andReturn().getResponse().getContentAsString(), Employee.class);

        newEmployee.setId(returned.getId());
        assertThat(newEmployee)
                .isEqualToIgnoringGivenFields(returned, "dateAdded", "dateUpdated");
    }

    @Test
    void delete() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(EmployeeController.REST_URL + "/" + EMPLOYEE1_ID)
                .contentType(MediaType.APPLICATION_JSON));

        mvc.perform(MockMvcRequestBuilders.get(EmployeeController.REST_URL + "/" + EMPLOYEE1_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        mvc.perform(MockMvcRequestBuilders.get(EmployeeController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(3)));
    }

    @Test
    void badUrl() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/noExist"))
                .andExpect(status().isNotFound());
    }
}