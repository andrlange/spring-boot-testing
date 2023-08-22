package cool.cfapps.springboottesting.service;

import cool.cfapps.springboottesting.dto.EmployeeDto;
import cool.cfapps.springboottesting.entity.Employee;
import cool.cfapps.springboottesting.exception.ResourceNotFoundException;
import cool.cfapps.springboottesting.repository.EmployeeRepository;
import cool.cfapps.springboottesting.util.EntityDtoUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeDto createEmployee(EmployeeDto employeeDto) {

        Employee employee = EntityDtoUtil.toEntity(employeeDto);
        assert employee != null;
        assert employee.getFirstName() != null;
        assert employee.getLastName() != null;
        assert employee.getId() == 0;

        return EntityDtoUtil.toDto(employeeRepository.save(employee));
    }

    public EmployeeDto updateEmployee(EmployeeDto employeeDto) {
        Employee employee = EntityDtoUtil.toEntity(employeeDto);
        assert employee != null;
        assert employee.getFirstName() != null;
        assert employee.getLastName() != null;
        assert employee.getId() != 0;

        Optional<Employee> existingEmployee = employeeRepository.findById(employee.getId());
        if (existingEmployee.isPresent()) {
            return EntityDtoUtil.toDto(employeeRepository.save(employee));
        } else {
            throw new ResourceNotFoundException("Employee with id " + employee.getId() + " not found");
        }
    }

    public Optional<EmployeeDto> readEmployee(Long id) {
        return EntityDtoUtil.toDto(employeeRepository.findById(id));
    }

    public void deleteEmployee(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            employeeRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Employee with id " + id + " not found");
        }
    }

    public List<EmployeeDto> readAllEmployees() {
        List<EmployeeDto> dtoList = new ArrayList<>();
        for (Employee employee : employeeRepository.findAll()) {
            dtoList.add(EntityDtoUtil.toDto(employee));
        }
        return dtoList;
    }
}
