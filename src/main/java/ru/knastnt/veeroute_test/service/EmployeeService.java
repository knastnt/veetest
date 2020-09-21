package ru.knastnt.veeroute_test.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestParam;
import ru.knastnt.veeroute_test.exceptions.NotFoundException;
import ru.knastnt.veeroute_test.model.Employee;
import ru.knastnt.veeroute_test.repository.EmployeeRepository;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public Page<Employee> findAll(Pageable pageable) {
        return repository.findAllByIsDeletedIs(pageable, false);
    }

    public Employee getById(Integer id) {
        return repository.findByIdAndIsDeletedIs(id, false).orElseThrow(NotFoundException::new);
    }

    public Employee create(Employee employee){
        Assert.isNull(employee.getId(), "Employee Id must be null");
        return repository.save(employee);
    }

    @Transactional
    public void update(Employee employee){
        getById(employee.getId());
        repository.save(employee);
    }

    @Transactional
    public void deleteById(Integer id) {
        getById(id);
        repository.safeDeleteById(id);
    }
}
