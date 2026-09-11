package com.rakesh.testApp.TestApp.controllers;


import com.rakesh.testApp.TestApp.dto.EmployeeDto;
import com.rakesh.testApp.TestApp.entities.Employee;
import com.rakesh.testApp.TestApp.repositories.EmployeeRepository;
import com.rakesh.testApp.TestApp.services.impl.EmployeeServiceImpl;


import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;




class EmployeeControllerTestIT extends  AbstractIntegrationTest {




   @Autowired
    private EmployeeRepository employeeRepository;



    @BeforeEach
    void setUp(){
        employeeRepository.deleteAll();
    }





    @Test
    void testGetEmployeeById_Success(){
        Employee savedEmployee=employeeRepository.save(testEmployee);
        testEmployeeDto.setId(savedEmployee.getId());
        webTestClient.get()
                .uri("/employees/{id}",savedEmployee.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(savedEmployee.getId())
                .jsonPath("$.email").isEqualTo(savedEmployee.getEmail());
//                .value(employeeDto ->{
//                    assertThat(employeeDto.getEmail()).equals(savedEmployee.getEmail());
//                    assertThat(employeeDto.getId()).isEqualTo(savedEmployee.getId());
//                });



    }


    @Test
    void testGetEmployeeById_Failure(){
        webTestClient.get()
                .uri("/employees/2")
                .exchange()
                .expectStatus().isNotFound();

    }
    @Test
    void testCreateNewEmployee_whenEmployeeAlreadyExist_thenThrowException(){
        Employee savedEmployee=employeeRepository.save(testEmployee);
        webTestClient.post()
                .uri("/employees")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().is5xxServerError();

    }

    @Test
    void testCreateNewEmployee_whenEmployeeNotExist_thenCreateSuccessfully(){
        webTestClient.post()
                .uri("/employees")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.email").isEqualTo(testEmployeeDto.getEmail());

    }
    @Test
    void testUpdateEmployee_whenEmployeeExist_thenUpdateSuccessfully(){
        Employee savedEmployee=employeeRepository.save(testEmployee);
        testEmployeeDto.setName("Rakesh Kumar Singh");
        webTestClient.put()
                .uri("/employees/{id}",savedEmployee.getId())
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.email").isEqualTo(testEmployeeDto.getEmail())
                .jsonPath("$.name").isEqualTo(testEmployeeDto.getName());
    }
    @Test
    void testUpdateEmployee_whenEmployeeNotExist_thenThrowException(){
        webTestClient.put()
                .uri("/employees/1")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isNotFound();
    }
    @Test
    void testUpdateEmployee_whenAttempingToUpdateTheEmail_ThenThrowException(){
        Employee savedEmployee=employeeRepository.save(testEmployee);
        testEmployeeDto.setEmail("rakesh@gmail.com");
        webTestClient.put()
                .uri("/employees/{id}",savedEmployee.getId())
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().is5xxServerError();

    }
    @Test
    void testDeleteEmployee_whenEmployeeExist_thenDeleteSuccessfully(){
        Employee savedEmployee=employeeRepository.save(testEmployee);
        webTestClient.delete()
                .uri("/employees/{id}",savedEmployee.getId())
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class);

        webTestClient.delete()
                .uri("/employees/1")
                .exchange()
                .expectStatus().isNotFound();
    }
    @Test
    void testDeleteEmployee_whenEmployeeNotExist_thenThrowException(){
        webTestClient.delete()
                .uri("/employees/1")
                .exchange()
                .expectStatus().isNotFound();
    }



}