package org.anil.testcontainers;

import org.anil.testcontainers.test1.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class TestContainersApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestContainersApplication.class, args);
    }


    @Autowired
    DepartmentRepository departmentRepository;

    @Bean
    public CommandLineRunner demo() {
        return (args) -> {
            for (var department : departmentRepository.findAll()) {
                System.out.println(department.getEmployees());
            }
        };
    }
}
