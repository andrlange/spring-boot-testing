package cool.cfapps.springboottesting.repository;

import cool.cfapps.springboottesting.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);

    // define custom query using Java Persistence Query Language (JQPL) with index parameters
    @Query("SELECT e FROM Employee e WHERE e.firstName = ?1 AND e.lastName = ?2")
    Optional<Employee> findByJPQL(String firstName, String lastName);

    // define custom query using Java Persistence Query Language (JQPL) with named parameters
    @Query("SELECT e FROM Employee e WHERE e.firstName = :firstName AND e.lastName = :lastName")
    Optional<Employee> findByJPQLNamedParams(String firstName, String lastName);

    // define custom query using Java Persistence Query Language (JQPL) with index parameters
    @Query(value = "SELECT * FROM employees e WHERE e.first_name = ?1 AND e.last_name = ?2", nativeQuery = true)
    Optional<Employee> findByNativeSql(String firstName, String lastName);

    // define custom query using Java Persistence Query Language (JQPL) with named parameters
    @Query(value = "SELECT * FROM employees e WHERE e.first_name = :firstName AND e.last_name = :lastName",
            nativeQuery = true)
    Optional<Employee> findByNativeSqlNamedParams(String firstName, String lastName);
}
