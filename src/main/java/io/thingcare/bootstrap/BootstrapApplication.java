package io.thingcare.bootstrap;

import io.thingcare.bootstrap.be.security.Role;
import io.thingcare.bootstrap.be.security.RoleRepository;
import io.thingcare.bootstrap.be.security.User;
import io.thingcare.bootstrap.be.security.UserRepository;
import io.thingcare.bootstrap.be.security.type.RoleType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class)
public class BootstrapApplication implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public BootstrapApplication(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(BootstrapApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findAll().isEmpty()) {
            Arrays.stream(RoleType.values()).forEach(roleType -> {
                String name = roleType.name().toLowerCase();

                Role role = new Role();
                role.setType(roleType);
                roleRepository.save(role);

                User user = new User();
                user.setEmail(name + "@thingcare.io");
                user.setFirstName(name);
                user.setLastName(name);
                user.setPassword(passwordEncoder.encode("1234"));
                user.setEnabled(true);

                user.setRoles(Collections.singletonList(role));
                userRepository.save(user);
            });
        }
    }
}
