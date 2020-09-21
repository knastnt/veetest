package ru.knastnt.veeroute_test.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.knastnt.veeroute_test.exceptions.NotFoundException;
import ru.knastnt.veeroute_test.model.Employee;
import ru.knastnt.veeroute_test.service.EmployeeService;

import java.net.URI;

@RestController
@RequestMapping(EmployeeController.REST_URL)
public class EmployeeController {
    static final String REST_URL = "/employees";

    private final EmployeeService service;

    @Value("${app.rest.employee.count-on-page}") /* работает только на не статическом поле*/
    public int countOnPage;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping
//    public Page<Employee> getAll(Pageable pageable){
    public Page<Employee> getAll(@RequestParam(required = false) Integer page){
//        return service.findAll(pageable);
        if (page==null) {
            return service.findAll(Pageable.unpaged());
        }else{
            return service.findAll(PageRequest.of(page==null ? 0 : page-1, countOnPage));
        }
    }

    @GetMapping("/{id}")
    public Employee getById(@PathVariable int id){
        return service.getById(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Employee employee, @PathVariable int id){
        if (employee.getId() != id) {
            throw new IllegalArgumentException();
        }
        service.update(employee);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> add (@RequestBody Employee employee){
        Employee created = service.create(employee);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id){
        service.deleteById(id);
    }
}
