package io.thingcare.bootstrap;

import io.thingcare.bootstrap.be.security.Role;
import io.thingcare.bootstrap.be.security.RoleRepository;
import io.thingcare.bootstrap.be.security.User;
import io.thingcare.bootstrap.be.security.UserRepository;
import io.thingcare.bootstrap.be.security.type.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class)
public class BootstrapApplication implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(BootstrapApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findAll().isEmpty()) {
            Arrays.stream(RoleType.values()).forEach(x -> {
                String name = x.name().split("_")[1].toLowerCase();

                Role role = new Role();
                role.setType(x);
                roleRepository.save(role);

                User user = new User();
                user.setUsername(name);
                user.setPassword(passwordEncoder.encode("1234"));

                user.setRoles(Collections.singletonList(role));
                userRepository.save(user);
            });
        }
    }
}
