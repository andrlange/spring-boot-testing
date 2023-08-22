package cool.cfapps.springboottesting.util;

import cool.cfapps.springboottesting.dto.CreateEmployeeDto;
import cool.cfapps.springboottesting.dto.EmployeeDto;
import cool.cfapps.springboottesting.entity.Employee;

import java.util.Optional;

public class EntityDtoUtil {

    public static EmployeeDto toDto(Employee employee) {
        return EmployeeDto.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .build();
    }

    public static Employee toEntity(EmployeeDto employeeDto) {
        return Employee.builder()
                .id(employeeDto.getId())
                .firstName(employeeDto.getFirstName())
                .lastName(employeeDto.getLastName())
                .email(employeeDto.getEmail())
                .build();
    }

    public static Optional<EmployeeDto> toDto(Optional<Employee> employee) {
        if (employee.isEmpty()) return Optional.empty();

        return Optional.of(
                EmployeeDto.builder()
                        .id(employee.get().getId())
                        .firstName(employee.get().getFirstName())
                        .lastName(employee.get().getLastName())
                        .email(employee.get().getEmail())
                        .build());
    }

    public static EmployeeDto toDto(CreateEmployeeDto createEmployeeDto) {
        return EmployeeDto.builder()
                .id(0L)
                .firstName(createEmployeeDto.getFirstName())
                .lastName(createEmployeeDto.getLastName())
                .email(createEmployeeDto.getEmail())
                .build();
    }
}
