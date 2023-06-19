package Payroll.application;

import Payroll.domain.Employee;
import Payroll.domain.EmployeeNotFoundException;
import Payroll.domain.EmployeeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

    private final EmployeeRepository repository;

    public EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/employees")
    public List<Employee> findAll() {

        return repository.findAll();
    }

    @PostMapping("/employees")
    Employee insert(@RequestBody Employee employee) {

        return repository.save(employee);
    }

    @GetMapping("/employees/{id}")
    Employee findById(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @PutMapping("/employees/{id}")
    Employee update(@RequestBody Employee newEmployee, @PathVariable Long id) {

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
    void deleteById(@PathVariable Long id) {

        repository.deleteById(id);
    }
}
