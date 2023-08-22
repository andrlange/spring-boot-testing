package cool.cfapps.springboottesting.repository;

import cool.cfapps.springboottesting.entity.Employee;
// Static import to improve code length
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;


@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    /*
    Warning: OpenJDK 64-Bit Server VM warning: Sharing is only supported for
    bootloader classes because bootstrap classpath has been appended

    Add jvm options -Xshare:off
     */

    private Employee employeeOne;

    @BeforeEach
    public void setUp() {
        employeeOne = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@doe.com")
                .build();

        // check if employees table is empty before each test
        assertThat(employeeRepository.count()).isEqualTo(0);
    }


    // JUnit Test for save Employee operation (BDD Style given when then)
    @DisplayName("JUnit Test for save Employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {

        // given - precondition ot setup
        // Employee employeeOne = Employee.builder()
        //         .firstName("John")
        //         .lastName("Doe")
        //         .email("john@doe.com")
        //         .build();

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.save(employeeOne);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    // JUnit Test for get all employees operation (BDD Style given when then)
    @DisplayName("JUnit Test for get all employees operation")
    @Test
    public void givenThreeEmployeeObjects_whenFindAll_thenRetrieveThreeEmployees() {
        // given - precondition ot setup
        // Employee employeeOne = Employee.builder()
        //         .firstName("John")
        //         .lastName("Doe")
        //         .email("john@doe.com")
        //         .build();

        Employee employeeTwo = Employee.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane@doe.com")
                .build();

        Employee employeeThree = Employee.builder()
                .firstName("Alfred")
                .lastName("Neumann")
                .email("alfred@doe.com")
                .build();

        // when - action or the behaviour that we are going to test
        employeeRepository.save(employeeOne);
        employeeRepository.save(employeeTwo);
        employeeRepository.save(employeeThree);

        List<Employee> employees = employeeRepository.findAll();

        // then - verify the output
        assertThat(employees).isNotNull();
        assertThat(employees).hasSize(3);
    }

    // JUnit Test for get Employee by ID operation (BDD Style given when then)
    @DisplayName("JUnit Test for get Employee by ID operation")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnTheEmployeeObject() {
        // given - precondition ot setup
        // Employee employeeOne = Employee.builder()
        //         .firstName("John")
        //         .lastName("Doe")
        //         .email("john@doe.com")
        //         .build();

        employeeRepository.save(employeeOne);

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findById(employeeOne.getId()).get();

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isEqualTo(employeeOne.getId());
    }

    // JUnit Test for get Employee by Email operation (BDD Style given when then)
    @DisplayName("JUnit Test for get Employee by Email operation")
    @Test
    public void givenEmployeeObject_whenFindByEmail_thenReturnTheEmployeeObject() {
        // given - precondition ot setup
        // Employee employeeOne = Employee.builder()
        //        .firstName("John")
        //        .lastName("Doe")
        //        .email("john@doe.com")
        //        .build();

        employeeRepository.save(employeeOne);

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByEmail(employeeOne.getEmail()).get();

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getEmail()).isEqualTo(employeeOne.getEmail());
    }

    // JUnit Test for update Employee operation
    @DisplayName("JUnit Test for update Employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() {
        // given - precondition ot setup
        Employee employeeOne = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@doe.com")
                .build();

        employeeRepository.save(employeeOne);

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByEmail(employeeOne.getEmail()).get();
        savedEmployee.setEmail("john.doe@gmail.com");
        savedEmployee.setFirstName("Jonny");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        // then - verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo("john.doe@gmail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Jonny");
    }

    // JUnit Test for delete Employee operation
    @DisplayName("JUnit Test for delete Employee operation")
    @Test
    public void givenEmployeeObject_whenDeleteEmployee_thenRemoveEmployeeObject() {
        // given - precondition ot setup
        Employee employeeOne = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@doe.com")
                .build();

        employeeRepository.save(employeeOne);

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByEmail(employeeOne.getEmail()).get();
        long employeeId = savedEmployee.getId();
        employeeRepository.deleteById(employeeId);
        Optional<Employee> deletedEmployee = employeeRepository.findById(employeeId);

        // then - verify the output
        assertThat(deletedEmployee.isEmpty()).isTrue();
    }

    // JUnit Test for findByJPQL Employee operation index params
    @DisplayName("JUnit Test for findByJPQL Employee operation index params")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject() {
        // given - precondition ot setup
        // Employee employeeOne = Employee.builder()
        //         .firstName("John")
        //         .lastName("Doe")
        //         .email("john@doe.com")
        //         .build();

        employeeRepository.save(employeeOne);

        String firstName = "John";
        String lastName = "Doe";

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByJPQL(firstName, lastName).get();

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    // JUnit Test for findByJPQL Employee operation named params
    @DisplayName("JUnit Test for findByJPQL Employee operation named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject() {
        // given - precondition ot setup
        // Employee employeeOne = Employee.builder()
        //         .firstName("John")
        //         .lastName("Doe")
        //         .email("john@doe.com")
        //         .build();

        employeeRepository.save(employeeOne);

        String firstName = "John";
        String lastName = "Doe";

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByJPQLNamedParams(firstName, lastName).get();

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    // JUnit Test for findByNativeQuery operation index params
    @DisplayName("JUnit Test for findByNativeQuery operation index params")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeQuery_thenReturnEmployeeObject() {
        // given - precondition ot setup
        // Employee employeeOne = Employee.builder()
        //         .firstName("John")
        //         .lastName("Doe")
        //         .email("john@doe.com")
        //         .build();

        employeeRepository.save(employeeOne);

        String firstName = "John";
        String lastName = "Doe";

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByNativeSql(firstName, lastName).get();

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    // JUnit Test for findByNativeQuery operation named params
    @DisplayName("JUnit Test for findByNativeQuery operation named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeQueryNamedParams_thenReturnEmployeeObject() {
        // given - precondition ot setup
        // Employee employeeOne = Employee.builder()
        //         .firstName("John")
        //         .lastName("Doe")
        //         .email("john@doe.com")
        //         .build();

        employeeRepository.save(employeeOne);

        String firstName = "John";
        String lastName = "Doe";

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByNativeSqlNamedParams(firstName, lastName).get();

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

}