package com.example.ai_mvc.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public void run(String... args) throws Exception {
        // Add some sample data
        taskRepository.save(new Task("Complete project", "Finish the Spring MVC demo project", LocalDate.now().plusDays(7), false));
        taskRepository.save(new Task("Buy groceries", "Milk, eggs, bread, and vegetables", LocalDate.now().plusDays(2), false));
        taskRepository.save(new Task("Call dentist", "Schedule a check-up appointment", LocalDate.now().plusDays(5), true));

        System.out.println("Sample data initialized!");
    }
}