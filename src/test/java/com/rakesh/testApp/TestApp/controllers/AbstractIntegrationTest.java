package com.rakesh.testApp.TestApp.controllers;

import com.rakesh.testApp.TestApp.TestContainerConfiguration;
import com.rakesh.testApp.TestApp.dto.EmployeeDto;
import com.rakesh.testApp.TestApp.entities.Employee;
import com.rakesh.testApp.TestApp.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.TimeZone;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "100000")
@Import(TestContainerConfiguration.class)
public class AbstractIntegrationTest {
    static {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
        System.setProperty("user.timezone", "Asia/Kolkata");
    }
    @Autowired
     WebTestClient webTestClient;



    Employee testEmployee=Employee.builder()

            .name("Rakesh")
                .salary(100000L)
                .email("RakeshOfficial@gmail.com")
                .build();
    EmployeeDto testEmployeeDto=EmployeeDto.builder()

            .name("Rakesh")
                 .salary(100000L)
                 .email("RakeshOfficial@gmail.com")
                 .build();


}
