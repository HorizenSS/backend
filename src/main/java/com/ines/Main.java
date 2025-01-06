package com.ines;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.ines.model.Customer;
import com.ines.model.Gender;
import com.ines.repository.CustomerRepository;
import com.ines.s3.S3Buckets;
import com.ines.s3.S3Service;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner runner(
            CustomerRepository customerRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            createDefaultAdminUser(customerRepository, passwordEncoder);
            createRandomCustomer(customerRepository, passwordEncoder);
        };
    }

    private static void createDefaultAdminUser(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        String adminEmail = "admin@example.com";

        if (!customerRepository.existsCustomerByEmail(adminEmail)) {
            Customer admin = Customer.builder()
                    .name("admin")
                    .email(adminEmail)
                    .password(passwordEncoder.encode("admin")) // Use a secure default password
                    .age(30)
                    .gender(Gender.MALE)
                    .role(Customer.Role.ADMIN) // Explicitly set the admin role
                    .build();
            customerRepository.save(admin);
            System.out.println("Admin user created with email: " + adminEmail);
        } else {
            System.out.println("Admin user already exists with email: " + adminEmail);
        }
    }

    private static void createRandomCustomer(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        var faker = new Faker();
        Random random = new Random();
        Name name = faker.name();
        String firstName = name.firstName();
        String lastName = name.lastName();
        int age = random.nextInt(16, 99);
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
        String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@ines.com";

        Customer customer = Customer.builder()
                .name(firstName + " " + lastName)
                .email(email)
                .password(passwordEncoder.encode("password"))
                .age(age)
                .gender(gender)
                .role(Customer.Role.USER) // Assign the default role
                .build();
        customerRepository.save(customer);
        System.out.println("Random customer created with email: " + email);
    }

}

