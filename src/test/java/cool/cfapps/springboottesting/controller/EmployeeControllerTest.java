package cool.cfapps.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cool.cfapps.springboottesting.dto.CreateEmployeeDto;
import cool.cfapps.springboottesting.dto.EmployeeDto;
import cool.cfapps.springboottesting.service.EmployeeService;
import cool.cfapps.springboottesting.util.EntityDtoUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.given;

import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.CoreMatchers.is;

@WebMvcTest
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    // JUnit Test for createEmployee method
    @Test
    @DisplayName("JUnit Test for createEmployee method")
    public void givenEmployeeDto_whenCreateEmployee_thenReturnCreatedEmployeeDto() throws Exception {
        // given - precondition ot setup
        CreateEmployeeDto createEmployeeDto = CreateEmployeeDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@gmail.com")
                .build();

        EmployeeDto employeeDto = EntityDtoUtil.toDto(createEmployeeDto);
        employeeDto.setId(100L);

        given(employeeService.createEmployee(ArgumentMatchers.any(EmployeeDto.class)))
                .willReturn(employeeDto);
        //.willAnswer((invocation -> invocation.getArgument(0)));

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createEmployeeDto)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employeeDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employeeDto.getLastName())))
                .andExpect(jsonPath("$.email", is(employeeDto.getEmail())))
                .andExpect(jsonPath("$.id", is(100)));
    }

    // JUnit Test for Get All Employees method
    @Test
    @DisplayName("JUnit Test for Get All Employees method")
    public void givenListOfEmployees_whenCallGetAllEmployees_thenReturnAllEmployees() throws Exception {
        // given - precondition ot setup
        List<EmployeeDto> employeeDtoList = List.of(
                EmployeeDto.builder()
                        .id(100L)
                        .firstName("John")
                        .lastName("Doe")
                        .email("john.doe@gmail.com")
                        .build(),
                EmployeeDto.builder()
                        .id(101L)
                        .firstName("Jane")
                        .lastName("Doe")
                        .email("jane.doe@gmail.com")
                        .build()
        );

        given(employeeService.readAllEmployees()).willReturn(employeeDtoList);

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/employees"));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(employeeDtoList.size())))
                .andExpect(jsonPath("$[0].firstName", is(employeeDtoList.get(0).getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(employeeDtoList.get(0).getLastName())))
                .andExpect(jsonPath("$[0].email", is(employeeDtoList.get(0).getEmail())))
                .andExpect(jsonPath("$[0].id", is(100)))
                .andExpect(jsonPath("$[1].firstName", is(employeeDtoList.get(1).getFirstName())))
                .andExpect(jsonPath("$[1].lastName", is(employeeDtoList.get(1).getLastName())))
                .andExpect(jsonPath("$[1].email", is(employeeDtoList.get(1).getEmail())))
                .andExpect(jsonPath("$[1].id", is(101)));
    }

    // JUnit Test for Get Employee By Id method
    @Test
    @DisplayName("JUnit Test for Get Employee By Id method")
    public void givenEmployeeDto_whenCallGetEmployeeById_thenReturnEmployeeDto() throws Exception {
        // given - precondition ot setup
        EmployeeDto employeeDto = EmployeeDto.builder()
                .id(100L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@gmail.com")
                .build();

        given(employeeService.readEmployee(100L)).willReturn(Optional.of(employeeDto));
        given(employeeService.readEmployee(200L)).willReturn(Optional.empty());

        // Positive Scenario
        // when - action or the behaviour that we are going to test
        ResultActions response100 = mockMvc.perform(get("/api/v1/employees/{id}",employeeDto.getId()));

        // then - verify the output
        response100.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.firstName", is(employeeDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employeeDto.getLastName())))
                .andExpect(jsonPath("$.email", is(employeeDto.getEmail())));

        // Negative Scenario
        // when - action or the behaviour that we are going to test
        ResultActions response200 = mockMvc.perform(get("/api/v1/employees/200"));

        // then - verify the output
        response200.andDo(print())
                .andExpect(status().isNotFound());

    }

    // JUnit Test for Update Employee method
    @Test
    @DisplayName("JUnit Test for Update Employee method")
    public void givenEmployeeDto_whenUpdateEmployee_thenReturnUpdatedEmployeeDto() throws Exception {
        // given - precondition ot setup
        EmployeeDto employeeDto = EmployeeDto.builder()
               .id(100L)
               .firstName("John")
               .lastName("Doe")
               .email("john.doe@gmail.com")
               .build();

        EmployeeDto updatedEmployeeDto = employeeDto.copyOf();
        updatedEmployeeDto.setEmail("john@doe.com");

        given(employeeService.updateEmployee(ArgumentMatchers.any(EmployeeDto.class))).willReturn(updatedEmployeeDto);

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/v1/employees")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(updatedEmployeeDto)));

        // then - verify the output
        response.andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.firstName", is(updatedEmployeeDto.getFirstName())))
               .andExpect(jsonPath("$.lastName", is(updatedEmployeeDto.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployeeDto.getEmail())));

    }

    // JUnit Test for Delete Employee method
    @Test
    @DisplayName("JUnit Test for Delete Employee method")
    public void givenEmployeeDto_whenDeleteEmployee_thenReturnNoContent() throws Exception {
        // given - precondition ot setup
        long employeeId = 100L;
        willDoNothing().given(employeeService).deleteEmployee(employeeId);

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/v1/employees/{id}", employeeId));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk());
    }

    // JUnit Test for
    //@Test
    //public void given_when_then() {
        // given - precondition ot setup

        // when - action or the behaviour that we are going to test

        // then - verify the output

    //}
}
