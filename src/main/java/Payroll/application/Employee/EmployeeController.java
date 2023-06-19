package Payroll.application.Employee;

import Payroll.domain.Employee.Employee;
import Payroll.domain.Employee.EmployeeNotFoundException;
import Payroll.domain.Employee.EmployeeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

    private final EmployeeRepository repository;

    public EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/employees")
    public List<Employee> findAllEmployees() {

        return repository.findAll();
    }

    @PostMapping("/employees")
    Employee insertEmployee(@RequestBody Employee employee) {

        return repository.save(employee);
    }

    @GetMapping("/employees/{id}")
    Employee findEmployeeById(@PathVariable Long id) {

        return repository.findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @PutMapping("/employees/{id}")
    Employee updateEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

        return repository.findById(id)
            .map(employee -> {
                employee.setName(newEmployee.getName());
                employee.setRole(newEmployee.getRole());

                return repository.save(employee);
            })
            .orElseGet(() -> {
                newEmployee.setId(id);

                return repository.save(newEmployee);
            });
    }

    @DeleteMapping("/employees/{id}")
    void deleteEmployeeById(@PathVariable Long id) {

        repository.deleteById(id);
    }
}
