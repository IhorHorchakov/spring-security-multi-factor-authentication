package com.example.repository;

import com.example.repository.entity.User;
import com.example.util.SecurityUtil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Integer, User> storage = new HashMap<>();

    public InMemoryUserRepository() {
        User johndoe = User.builder()
                .id(0)
                .username("johndoe")
                .password(SecurityUtil.encodePassword("johndoepass"))
                .phoneNumber("+380951234567")
                .build();
        User fairyprincess = User.builder()
                .id(1)
                .username("fairyprincess")
                .password(SecurityUtil.encodePassword("fairyprincesspass"))
                .phoneNumber("+380951234560")
                .build();
        this.storage.put(johndoe.getId(), johndoe);
        this.storage.put(fairyprincess.getId(), fairyprincess);
    }

    @Override
    public Optional<User> getById(int id) {
        return Optional.ofNullable(this.storage.get(id));
    }

    @Override
    public Optional<User> getByUserName(String username) {
        return this.storage.values().stream().filter(user -> user.getUsername().equals(username)).findFirst();
    }
}
