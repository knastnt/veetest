package ru.knastnt.veeroute_test;

import ru.knastnt.veeroute_test.model.Employee;

import java.time.LocalDate;

public class EmployeesTestData {
    public static final int EMPLOYEE1_ID = 1;
    public static final long EMPLOYEE1_CREATE_AND_UPDATE_DATE = 1600650961000L;
    public static final Employee EMPLOYEE1 = new Employee("Иван", "Иванов", "Иванович") {{
        setId(EMPLOYEE1_ID);
        setAccountNumber(999);
        setDateOfBirth(LocalDate.of(1995, 2, 17));
    }};

    public static final int EMPLOYEE4_ID = 4;
    public static final Employee EMPLOYEE4 = new Employee("Евгений", "Поляков", "Юрьевич") {{
        setId(EMPLOYEE4_ID);
        setAccountNumber(31556);
        setDateOfBirth(LocalDate.of(2006, 8, 25));
    }};

    public static final int EMPLOYEE5_ID = 5;
    public static final Employee EMPLOYEE5 = new Employee("Олег", "Калинин", "Семёнович") {{
        setId(EMPLOYEE5_ID);
        setAccountNumber(1587);
        setDateOfBirth(LocalDate.of(1969, 2, 28));
    }};

    public static final int DELETED_EMPLOYEE_ID = 3;


    public static Employee getNew() {
        Employee employee = new Employee("Новый1", "Новый2", "Новый3");
        employee.setAccountNumber(9876);
        employee.setDateOfBirth(LocalDate.of(2000, 1, 1));
        return employee;
    }

}
