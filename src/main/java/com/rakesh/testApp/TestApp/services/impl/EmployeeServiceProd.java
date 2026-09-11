package com.rakesh.testApp.TestApp.services.impl;

import com.rakesh.testApp.TestApp.services.DataService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
public class EmployeeServiceProd implements DataService {
    @Override
    public String getData() {
        return "Prod Data";
    }
}
