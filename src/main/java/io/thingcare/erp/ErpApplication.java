package io.thingcare.erp;

import io.thingcare.erp.be.security.Role;
import io.thingcare.erp.be.security.User;
import io.thingcare.erp.be.security.UserRepository;
import io.thingcare.erp.be.security.type.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class)
public class ErpApplication implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(ErpApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Arrays.stream(RoleType.values()).forEach(x -> {
            String name = x.name().split("_")[1].toLowerCase();

            User user = new User();
            user.setUsername(name);
            user.setPassword(passwordEncoder.encode("1234"));

            Role role = new Role();
            role.setType(x);
            role.setUser(user);

            user.setRoles(Arrays.asList(role));
            userRepository.save(user);
        });
    }
}
