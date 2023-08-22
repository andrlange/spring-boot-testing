package cool.cfapps.springboottesting.controller;

import cool.cfapps.springboottesting.dto.CreateEmployeeDto;
import cool.cfapps.springboottesting.dto.EmployeeDto;
import cool.cfapps.springboottesting.service.EmployeeService;
import cool.cfapps.springboottesting.util.EntityDtoUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDto createEmployee(@RequestBody CreateEmployeeDto createEmployeeDto) {
        EmployeeDto employeeDto = EntityDtoUtil.toDto(createEmployeeDto);
        return employeeService.createEmployee(employeeDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeDto> readAllEmployees() {
        return employeeService.readAllEmployees();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> readAllEmployees(@PathVariable Long id) {
        return ResponseEntity.of(employeeService.readEmployee(id));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public EmployeeDto updateEmployee(@RequestBody EmployeeDto employeeDto) {
        return employeeService.updateEmployee(employeeDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }
}
