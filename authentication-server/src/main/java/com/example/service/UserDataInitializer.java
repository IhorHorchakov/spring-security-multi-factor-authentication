package com.example.service;

import com.example.repository.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
public class UserDataInitializer {
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void populateUsersOnApplicationStartUp() {
        log.info("Populating the test users");
        User johndoe = User.builder()
                .id(0)
                .username("johndoe")
                .password(passwordEncoder.encode("johndoepass"))
                .phoneNumber("+380951234567")
                .authorities(Arrays.asList())
                .build();
        User fairyprincess = User.builder()
                .id(1)
                .username("fairyprincess")
                .password(passwordEncoder.encode("fairyprincesspass"))
                .phoneNumber("+380951234560")
                .authorities(Arrays.asList())
                .build();
        userService.save(johndoe);
        userService.save(fairyprincess);
    }
}
