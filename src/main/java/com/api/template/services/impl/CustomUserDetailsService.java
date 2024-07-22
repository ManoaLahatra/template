package com.api.template.services.impl;

import com.api.template.models.auth.Permission;
import com.api.template.models.auth.Role;
import com.api.template.models.auth.User;
import com.api.template.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public void createUser(User user) {

        if (!user.getEmail().contains("@") || !user.getEmail().contains(".")) {
            throw new RuntimeException("Invalid e-mail.");
        }
        if (user.getPassword().length() < 8) {
            throw new RuntimeException("Password must contain 8 characters minimum.");
        }

        Optional<User> userOptional = userRepository.findUserByEmail(user.getEmail());
        if (userOptional.isPresent()) {
            throw new RuntimeException("E-mail already registered.");
        }

        // Encode user password and make this as new password that saving in databaseopenapi: 3.0.3
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        Role userRole = new Role();
        userRole.setRoleName(Permission.CLIENT);
        user.setRole(userRole);

        user = this.userRepository.save(user);
    }

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return this
                .userRepository
                .findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user match."));
    }

}