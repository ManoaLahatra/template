package com.api.template.controller;

import com.api.template.dto.AuthenticationDto;
import com.api.template.models.auth.User;
import com.api.template.security.JwtService;
import com.api.template.services.impl.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private AuthenticationManager authenticationManager;
    private CustomUserDetailsService userService;
    private JwtService jwtService;

    @PostMapping(path = "register")
    public void register(@RequestBody User user) {
        this.userService.createUser(user);
    }

    @PostMapping(path = "login")
    public Map<String, String> login(@RequestBody AuthenticationDto authenticationDto) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationDto.email(),
                        authenticationDto.password()
                )
        );

        if (authenticate.isAuthenticated()) {
            return this.jwtService.generate(authenticationDto.email());
        }

        return null;
    }

}