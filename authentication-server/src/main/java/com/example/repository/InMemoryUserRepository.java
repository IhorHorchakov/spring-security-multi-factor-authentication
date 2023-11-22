package com.example.repository;

import com.example.repository.entity.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Integer, User> storage = new HashMap<>();

    @Override
    public void save(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("Failed to save user, the id is null");
        }
        this.storage.put(user.getId(), user);
    }

    @Override
    public Optional<User> getByUserName(String username) {
        return this.storage.values().stream().filter(user -> user.getUsername().equals(username)).findFirst();
    }
}
