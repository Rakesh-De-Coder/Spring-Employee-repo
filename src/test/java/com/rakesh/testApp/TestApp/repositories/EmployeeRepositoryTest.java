package com.rakesh.testApp.TestApp.repositories;

import com.rakesh.testApp.TestApp.TestContainerConfiguration;
import com.rakesh.testApp.TestApp.entities.Employee;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;


import java.util.List;
import java.util.TimeZone;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@Slf4j
@DataJpaTest
@Import(TestContainerConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepositoryTest {
    static {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
        System.setProperty("user.timezone", "Asia/Kolkata");
    }

    @Autowired
    private EmployeeRepository repository;


    private Employee employee;

    @BeforeEach
    void setUp(){
         employee=Employee.builder()
//                .id(2L)
                .email("rakesh@Gmail.com")
                 .name("Rakesh")
                .salary(1000L)
                .build();
    }

    @Test
    void testFindByEmail_whenEmailIsPresent_thenReturnEmployee() {
        //arrange given
        repository.save(employee);


//        act when

       List<Employee> employees= repository.findByEmail("rakesh@Gmail.com");

//       assert then

     assertThat(employees).isNotNull();
     Assertions.assertThat(employees).isNotEmpty();
        assertThat(employees.getFirst().getEmail()).isEqualTo(employee.getEmail());

        log.info("the id is :"+employee.getId());



    }

    @Test
    void testFindByEmail_whenEmailIsNotFound_thenReturnEmptyEmployeeList() {
        //arrange
        String email="not-present@gmail.com";

        //        act when

        List<Employee> employees= repository.findByEmail("rakesh@Gmail.com");

        //       assert then

        assertThat(employees).isNotNull();
        Assertions.assertThat(employees).isEmpty();






    }

    @Test
    void printTimezone() {
        System.out.println("USER TIMEZONE = " + System.getProperty("user.timezone"));
        System.out.println("DEFAULT TIMEZONE = " +
                java.util.TimeZone.getDefault().getID());
    }

}