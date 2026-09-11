package com.rakesh.testApp.TestApp.services;

import com.rakesh.testApp.TestApp.TestContainerConfiguration;
import com.rakesh.testApp.TestApp.dto.EmployeeDto;
import com.rakesh.testApp.TestApp.entities.Employee;
import com.rakesh.testApp.TestApp.exceptions.ResourceNotFoundException;
import com.rakesh.testApp.TestApp.repositories.EmployeeRepository;
import com.rakesh.testApp.TestApp.services.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import org.modelmapper.ModelMapper;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Import(TestContainerConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeServiceImplTest {

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
        System.setProperty("user.timezone", "Asia/Kolkata");
    }

    @Mock
    private EmployeeRepository employeeRepository;

    @Spy
    private ModelMapper modelMapper;

   @InjectMocks
    private EmployeeServiceImpl employeeService;

   @InjectMocks
   private Employee mockEmployee;

   @InjectMocks
   private EmployeeDto mockEmployeeDto;


   @BeforeEach
   void setUp(){
       mockEmployee=Employee.builder()
               .id(1L)
               .name("Rakesh")
               .salary(100000L)
               .email("RakeshOfficial@gmail.com")
               .build();

       mockEmployeeDto=modelMapper.map(mockEmployee,EmployeeDto.class);
   }

    @Test
    void getEmployeeById_whenValidEmployeeIdIsPresent_thenReturnEmployeeDto(){
//      assign

        Long id=mockEmployee.getId();

        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee));

//        act
        EmployeeDto employeeDto=employeeService.getEmployeeById(id);

//        assert

        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getId()).isEqualTo(id);
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployee.getEmail());
//        verify(employeeRepository).save(null);
        verify(employeeRepository ,only()).findById(id);

    }

    @Test
    void getEmployeeById_whenEmployeeIsNotPresent_thenThrowException(){
//       arrange

        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

//        act  and     assert
        assertThatThrownBy(()->employeeService.getEmployeeById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: "+1);

        verify(employeeRepository).findById(1L);





    }

    @Test
    void createNewEmployee_whenValidEmployee_thenCreateNewEmployee(){

//        assign
        when(employeeRepository.findByEmail(anyString())).thenReturn(List.of());
        when(employeeRepository.save(any(Employee.class))).thenReturn(mockEmployee);


//        act
        EmployeeDto employeeDto=employeeService.createNewEmployee(mockEmployeeDto);



//        assert


        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployee.getEmail());
        ArgumentCaptor<Employee> employeeArgumentCaptor=ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(employeeArgumentCaptor.capture());

        Employee capturedEmployee=employeeArgumentCaptor.getValue();

        assertThat(capturedEmployee.getEmail()).isEqualTo(mockEmployee.getEmail());


    }

    @Test
    void testCreateNewEmployee_whenAttempingToCreateNewEmployeeWithExistingEmail_thenThrowException(){
//       arrange
        when(employeeRepository.findByEmail(mockEmployeeDto.getEmail())).thenReturn(List.of(mockEmployee));


//        act and  assert

        assertThatThrownBy(()->employeeService.createNewEmployee(mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee already exists with email: "+mockEmployeeDto.getEmail());

        verify(employeeRepository).findByEmail(mockEmployeeDto.getEmail());
        verify(employeeRepository ,never()).save(mockEmployee);



    }

    @Test
    void testUpdateEmployee_whenEmployeeDoesNotExist_thenThrowException(){

//       arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

//        act and assert
        assertThatThrownBy(()->employeeService.updateEmployee(1L,mockEmployeeDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: " + 1L);


        verify(employeeRepository).findById(1L);
        verify(employeeRepository ,never()).save(any());
    }

    @Test
    void testUpdateEmployee_whenWantToUpdateEmail_thenThrowException(){

//       arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(mockEmployee));
        mockEmployeeDto.setEmail("rakesh@gmail.com");
        mockEmployeeDto.setName("Rakesh Kumar Singh");


//        acr amd assert
        assertThatThrownBy(()->employeeService.updateEmployee(1L,mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("The email of the employee cannot be updated");

        verify(employeeRepository).findById(1L);
        verify(employeeRepository ,never()).save(any());




    }

    @Test
    void testUpdateEmployee_whenValidEmail_thenUpdateSuccessfully(){

//       arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(mockEmployee));

        mockEmployeeDto.setName("Rakesh Kumar Singh");
        mockEmployeeDto.setSalary(102030L);

        Employee newEmployee=modelMapper.map(mockEmployeeDto,Employee.class);
        when(employeeRepository.save(any(Employee.class))).thenReturn(newEmployee);



//        act

        EmployeeDto updatedEmployeeDto=employeeService.updateEmployee(mockEmployeeDto.getId(),mockEmployeeDto);


//assert
        assertThat(mockEmployeeDto).isNotNull();
        assertThat(updatedEmployeeDto.getEmail()).isEqualTo(mockEmployeeDto.getEmail());
        assertThat(updatedEmployeeDto).isEqualTo(mockEmployeeDto);
        ArgumentCaptor<Employee> employeeArgumentCaptor=ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(employeeArgumentCaptor.capture());

    }

    @Test
    void testDeleteEmployee_whenEmployeeDoesNotExist_thenThrowException(){
//       act
        when(employeeRepository.existsById(anyLong())).thenReturn(false);

//       act and assert
        assertThatThrownBy(()->employeeService.deleteEmployee(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: "+1);
        verify(employeeRepository ,never()).deleteById(anyLong());
    }
    @Test
    void testDeleteEmployee_whenEmployeeExistById_thenDeleteEmployee(){
//       act
        when(employeeRepository.existsById(anyLong())).thenReturn(true);


//       act and



        assertThatCode(()->employeeService.deleteEmployee(1L))
                .doesNotThrowAnyException();

        verify(employeeRepository).deleteById(anyLong());
    }

}