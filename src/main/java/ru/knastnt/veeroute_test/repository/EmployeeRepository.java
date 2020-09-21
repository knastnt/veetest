package ru.knastnt.veeroute_test.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.knastnt.veeroute_test.model.Employee;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Page<Employee> findAllByIsDeletedIs(Pageable pageable, Boolean isDeleted);
    Optional<Employee> findByIdAndIsDeletedIs(Integer id, Boolean isDeleted);

    @Transactional
    @Modifying
    @Query("UPDATE Employee e SET isDeleted = true WHERE e.id=:id")
    void safeDeleteById(Integer id);
}
