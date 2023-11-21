package com.example.service;

import com.example.repository.UserRepository;
import com.example.repository.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.security.core.userdetails.User.*;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getByUserName(username);
        return builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(List.of())
                .build();
    }

    public User getByUserName(String username) {
        Optional<User> optionalUser = userRepository.getByUserName(username);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User %s does not exist".formatted(username));
        } else {
            return optionalUser.get();
        }
    }
}
