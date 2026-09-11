package com.rakesh.testApp.TestApp;

import com.rakesh.testApp.TestApp.services.DataService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication
public class TestAppApplication implements CommandLineRunner {

//	private final DataService dataService;

	@Value("${my.variable}")
	private String myVariable;

	public static void main(String[] args) {
		SpringApplication.run(TestAppApplication.class, args);
	}

	@Override
	public void run(String @NonNull ... args)throws Exception{
		System.out.println("My Variable: " + myVariable);
//		System.out.println("the Data is Data "+ dataService.getData());
	}

}
