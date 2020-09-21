package ru.knastnt.veeroute_test.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import ru.knastnt.veeroute_test.exceptions.NotFoundException;
import ru.knastnt.veeroute_test.model.Employee;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static ru.knastnt.veeroute_test.EmployeesTestData.*;

@SpringBootTest
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
class EmployeeServiceTest {

    @Autowired
    private EmployeeService service;

    @Test
    void findAll() {
        List<Employee> employeeList = service.findAll(Pageable.unpaged()).getContent();
        assertThat(employeeList)
                .hasSize(4);
    }

    @Test
    void findAllPaged() {
        List<Employee> employeeList = service.findAll(PageRequest.of(1, 2)).getContent();
        assertThat(employeeList.stream().map(Employee::getId))
                        .containsOnly(4, 5);
    }

    @Test
    void getById() {
        Employee employee = service.getById(EMPLOYEE1_ID);
        assertThat(employee)
                .isEqualToIgnoringGivenFields(EMPLOYEE1, "dateAdded", "dateUpdated");
    }

    @Test
    void getByIdNotExists() {
        assertThrows(NotFoundException.class, () -> service.getById(DELETED_EMPLOYEE_ID));

    }

    @Test
    void addedAndUpdatedDates() {
        Employee updated = new Employee(EMPLOYEE1);
        updated.setFirstName(updated.getFirstName() + " updated");
        service.update(updated);

        Employee recived = service.getById(updated.getId());

        assertThat(recived.getDateAdded().getTime())
                .isEqualTo(EMPLOYEE1_CREATE_AND_UPDATE_DATE);
        assertThat(recived.getDateUpdated().getTime())
                .isGreaterThan(EMPLOYEE1_CREATE_AND_UPDATE_DATE);
    }

    @Test
    void create(){
        Employee created = service.create(getNew());
        int newId = created.getId();

        Employee newEmployee = getNew();
        newEmployee.setId(newId);

        assertThat(created)
                .isEqualToIgnoringGivenFields(newEmployee, "dateAdded", "dateUpdated");
        assertThat(service.getById(newId))
                .isEqualToIgnoringGivenFields(newEmployee, "dateAdded", "dateUpdated");
    }

    @Test
    void updateDeleted() {
        Employee deleted = service.create(getNew());
        deleted.setId(DELETED_EMPLOYEE_ID);

        assertThrows(NotFoundException.class, () -> service.update(deleted));

        deleted.setId(100000);

        assertThrows(NotFoundException.class, () -> service.update(deleted));

    }

    @Test
    void delete() {
        assertThat(service.getById(EMPLOYEE1_ID))
                .isNotNull();

        service.deleteById(EMPLOYEE1_ID);

        assertThrows(NotFoundException.class, () -> service.getById(EMPLOYEE1_ID));

    }
}