package cool.cfapps.springboottesting.service;

import cool.cfapps.springboottesting.dto.EmployeeDto;
import cool.cfapps.springboottesting.entity.Employee;
import cool.cfapps.springboottesting.exception.ResourceNotFoundException;
import cool.cfapps.springboottesting.repository.EmployeeRepository;
import cool.cfapps.springboottesting.util.EntityDtoUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.BDDMockito.given;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// Static import to improve code length
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeDto employeeDto;
    private EmployeeDto employeeTwoDto;
    private EmployeeDto savedEmployeeDto;

    @BeforeEach
    public void setUp() {
        // using @Mock will replace this mock
        //employeeRepository = Mockito.mock(EmployeeRepository.class);
        // using @InjectMocks will replace this instantiation
        //employeeService = new EmployeeService(employeeRepository);

        // create mocked items
        employeeDto = EmployeeDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@doe.com")
                .build();

        savedEmployeeDto = employeeDto.copyOf();
        savedEmployeeDto.setId(1L);

        employeeTwoDto = EmployeeDto.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane@doe.com")
                .build();

    }

    // JUnit Test for createEmployee method
    @Test
    @DisplayName("JUnit Test for createEmployee method")
    public void givenEmployeeObject_whenCreated_thenReturnNewEmployeeObject() {
        // given - precondition ot setup
        // stub this method
        given(employeeRepository.save(EntityDtoUtil.toEntity(employeeDto))).willReturn(EntityDtoUtil.toEntity(savedEmployeeDto));

        // when - action or the behaviour that we are going to test
        EmployeeDto newEmployeeDto = employeeService.createEmployee(employeeDto);


        System.out.println("EmployeeRepository: " + employeeRepository.getClass());
        System.out.println("EmployeeService: " + employeeService.getClass());
        System.out.println("EmployeeDto: " + newEmployeeDto);
        // then - verify the output
        assertThat(newEmployeeDto).isNotNull();
        assertThat(newEmployeeDto.getId()).isGreaterThan(0);
    }

    // JUnit Test for updateEmployee method
    @Test
    @DisplayName("JUnit Test for updateEmployee method")
    public void givenEmployeeObject_whenUpdated_thenReturnNewEmployeeObject() {
        // given - precondition ot setup
        EmployeeDto updateEmployeeDto = savedEmployeeDto.copyOf();
        updateEmployeeDto.setFirstName("Jonny");
        updateEmployeeDto.setEmail("john.doe@gmail.com");

        given(employeeRepository.save(EntityDtoUtil.toEntity(updateEmployeeDto))).willReturn(EntityDtoUtil.toEntity(updateEmployeeDto));
        given(employeeRepository.findById(1L)).willReturn(Optional.of(EntityDtoUtil.toEntity(savedEmployeeDto)));

        // when - action or the behaviour that we are going to test
        EmployeeDto updatedEmployeeDto = employeeService.updateEmployee(updateEmployeeDto);

        // then - verify the output
        assertThat(updatedEmployeeDto).isNotNull();
        assertThat(updatedEmployeeDto.getId()).isGreaterThan(0);
        assertThat(updatedEmployeeDto.getFirstName()).isEqualTo("Jonny");
        assertThat(updatedEmployeeDto.getEmail()).isEqualTo("john.doe@gmail.com");
    }

    // JUnit Test for readAllEmployees method (positive)
    @Test
    @DisplayName("JUnit Test for readAllEmployees method (positive)")
    public void givenListOfEmployees_whenReadAll_thenReturnListOfEmployeeObjects() {
        // given - precondition ot setup
        List<Employee> result = List.of(
                EntityDtoUtil.toEntity(savedEmployeeDto),
                EntityDtoUtil.toEntity(employeeTwoDto));

        given(employeeRepository.findAll()).willReturn(result);

        // when - action or the behaviour that we are going to test
        List<EmployeeDto> employeesDto = employeeService.readAllEmployees();

        // then - verify the output
        assertThat(employeesDto).isNotEmpty();
        assertThat(employeesDto.size()).isEqualTo(2);
    }

    // JUnit Test for readAllEmployees method (negative)
    @Test
    @DisplayName("JUnit Test for readAllEmployees method (negative)")
    public void givenEmptyListOfEmployees_whenReadAll_thenReturnEmptyListObjects() {
        // given - precondition ot setup

        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        // when - action or the behaviour that we are going to test
        List<EmployeeDto> employeesDto = employeeService.readAllEmployees();

        // then - verify the output
        assertThat(employeesDto).isEmpty();
        assertThat(employeesDto.size()).isEqualTo(0);
    }

    // JUnit Test for updateEmployees method with none existing ID
    @Test
    @DisplayName("JUnit Test for updateEmployees method with none existing ID")
    public void givenEmployeeWithWrongId_whenUpdate_thenExpectEceptionThrown() {
        // given - precondition ot setup
        EmployeeDto wrongEmployeeDto = savedEmployeeDto.copyOf();
        wrongEmployeeDto.setId(100L);

        given(employeeRepository.findById(wrongEmployeeDto.getId())).willReturn(Optional.empty());

        // when - action or the behaviour that we are going to test
        Assertions.assertThrows(ResourceNotFoundException.class, () -> employeeService.updateEmployee(wrongEmployeeDto));

    }

    // JUnit Test for readEmployee by id operation
    @Test
    public void givenEmployeeId_whenReadingEmployee_thenReturnEmployeeObject() {
        // given - precondition ot setup
        long employeeId = 1L;

        // when - action or the behaviour that we are going to test
        given(employeeRepository.findById(employeeId)).willReturn(Optional.of(EntityDtoUtil.toEntity(savedEmployeeDto)));
        Optional<EmployeeDto> returnedEmployeeDto = employeeService.readEmployee(employeeId);

        // then - verify the output
        assertThat(returnedEmployeeDto).isNotNull();
        assertThat(returnedEmployeeDto.isPresent()).isTrue();
        assertThat(returnedEmployeeDto.get().getId()).isEqualTo(1L);
    }

    // JUnit Test for
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenDoNothing() {
        // given - precondition ot setup
        long employeeId = 1L;

        // when - action or the behaviour that we are going to test
        // mock return employee true before deletion (first invocation)
        given(employeeRepository.findById(employeeId)).willReturn(Optional.of(EntityDtoUtil.toEntity(savedEmployeeDto)));
        willDoNothing().given(employeeRepository).deleteById(employeeId);

        employeeService.deleteEmployee(employeeId);
        // mock return employee false after deletion (second invocation)
        given(employeeRepository.findById(employeeId)).willReturn(Optional.empty());

        // then - verify the output
        assertThat(employeeService.readEmployee(employeeId).isPresent()).isFalse();
        // then - verify the output how many invocations -> expected 2 invocations
        verify(employeeRepository, times(2)).findById(employeeId);


    }
}
