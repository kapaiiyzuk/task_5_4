package org.example.task_5_1;

import org.example.task_5_1.entity.Role;
import org.example.task_5_1.entity.User;
import org.example.task_5_1.repository.RoleRepository;
import org.example.task_5_1.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import java.util.Set;

@SpringBootApplication
public class Task51Application {

    public static void main(String[] args) {

        SpringApplication.run(Task51Application.class, args);
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

    @Bean
    public CommandLineRunner init(UserRepository userRepository,
                                  RoleRepository roleRepository,
                                  PasswordEncoder passwordEncoder) {
        return args -> {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

            userRepository.findByUsername("admin").orElseGet(() -> {
                User u = new User();
                u.setUsername("admin");
                u.setPassword(passwordEncoder.encode("admin")); // пароль admin
                u.setFirstName("Admin");
                u.setLastName("Admin");
                u.setEmail("admin@gmail.com");
                u.setRoles(Set.of(adminRole, userRole));
                return userRepository.save(u);
            });
        };
    }
}
