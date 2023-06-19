package Payroll.application.Order;

import Payroll.domain.Employee.Employee;
import Payroll.domain.Employee.EmployeeNotFoundException;
import Payroll.domain.Order.Order;
import Payroll.domain.Order.OrderNotFoundException;
import Payroll.domain.Order.OrderRepository;
import Payroll.domain.Order.Status;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/orders")
    public List<Order> findAllOrders() {

        return orderRepository.findAll();
    }

    @GetMapping("/orders/{id}")
    Order findOrderById(@PathVariable Long id) {

        return orderRepository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @PostMapping("/orders")
    Order insertOrder(@RequestBody Order order) {

        order.setStatus(Status.IN_PROGRESS);

        return orderRepository.save(order);
    }

    @DeleteMapping("/orders/{id}/cancel")
    ResponseEntity<?> cancelOrder(@PathVariable Long id) throws Exception {

        Order order = orderRepository.findById(id) //
            .orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getStatus() == Status.IN_PROGRESS) {

            order.setStatus(Status.CANCELLED);

            return ResponseEntity.ok(orderRepository.save(order));
        }

        return ResponseEntity
            .status(HttpStatus.METHOD_NOT_ALLOWED)
            .body("You can't cancel an order that is in the " + order.getStatus() + " status");
    }

    @PutMapping("/orders/{id}/complete")
    ResponseEntity<?> completeOrder(@PathVariable Long id) throws Exception {

        Order order = orderRepository.findById(id) //
            .orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getStatus() == Status.IN_PROGRESS) {

            order.setStatus(Status.COMPLETED);

            return ResponseEntity.ok(orderRepository.save(order));
        }

        return ResponseEntity
            .status(HttpStatus.METHOD_NOT_ALLOWED)
            .body("You can't complete an order that is in the " + order.getStatus() + " status");
    }
}
