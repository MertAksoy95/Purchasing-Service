package com.emlakjet.purchasing.config;


import com.emlakjet.purchasing.entity.User;
import com.emlakjet.purchasing.repository.UserRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * This is our class that adds user to the database for testing when the application first boots up.
 */
@Component
public class InitialConfig {

    private final UserRepository userRepo;

    public InitialConfig(UserRepository userRepo) {
        this.userRepo = userRepo;
    }


    @PostConstruct
    private void initTestUser() {
        if (userRepo.findByEmail("john@doe.com") == null) {
            User user = new User();
            user.setEmail("john@doe.com");
            user.setFirstName("John");
            user.setLastName("Doe");
            user.setPassword("test123");

            userRepo.save(user);
        }
    }

}
