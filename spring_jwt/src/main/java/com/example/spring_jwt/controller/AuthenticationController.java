package com.example.spring_jwt.controller;

import com.example.spring_jwt.dto.AccessAndRefreshToken;
import com.example.spring_jwt.dto.TokenDto;
import com.example.spring_jwt.dto.UpdateUserDetailsDto;
import com.example.spring_jwt.model.AuthenticationResponse;
import com.example.spring_jwt.model.User;
import com.example.spring_jwt.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AccessAndRefreshToken> register(@RequestBody User request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AccessAndRefreshToken> login(@RequestBody User request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @GetMapping("/demo")
    public ResponseEntity<String> demo() {
        return ResponseEntity.ok("Hello from secured url");
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<AccessAndRefreshToken> refreshToken(@RequestBody TokenDto tokenDto) {
        return authService.refreshToken(tokenDto);
    }

    @GetMapping("/admin")
    public String adminCall() {
        return "Response for admin";
    }

    @GetMapping("/user")
    public String userCall() {
        return "Response for user/admin";
    }

    @PostMapping("/update_user_details")
    public String updateUserDetails(@RequestBody UpdateUserDetailsDto updateUserDetailsDto) {
        System.out.println("updateUserDetailsDto: " + updateUserDetailsDto.getUserName());
        return "Update user details response";
    }
}
