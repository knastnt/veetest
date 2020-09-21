package ru.knastnt.veeroute_test.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue
    private Integer id;
    private String firstName;
    private String lastName;
    private String middleName;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDate dateOfBirth;


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @CreationTimestamp /* задается время при первом сохранении в базу; пишет в базу мануально присвоенные значения */
    @Column(updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date dateAdded;

//    @Column(insertable = false, updatable = false, columnDefinition = "timestamp default now()")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @UpdateTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date dateUpdated;

    private Integer accountNumber;

    private boolean isDeleted;

    public Employee() {
    }

    public Employee(String firstName, String lastName, String middleName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
    }

    public Employee(Employee employee) {
        this.id = employee.id;
        this.firstName = employee.firstName;
        this.lastName = employee.lastName;
        this.middleName = employee.middleName;
        this.dateOfBirth = employee.dateOfBirth;
        this.dateAdded = employee.dateAdded;
        this.dateUpdated = employee.dateUpdated;
        this.accountNumber = employee.accountNumber;
        this.isDeleted = employee.isDeleted;
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public boolean isDeleted() {
        return isDeleted;
    }





    public void setId(Integer id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
