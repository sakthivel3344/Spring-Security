package com.example.spring_jwt.service;

import com.example.spring_jwt.dto.AccessAndRefreshToken;
import com.example.spring_jwt.dto.TokenDto;
import com.example.spring_jwt.model.AuthenticationResponse;
import com.example.spring_jwt.model.User;
import com.example.spring_jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsServiceImpl userDetailsService;



    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    public AccessAndRefreshToken register(User request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user);

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new AccessAndRefreshToken(accessToken, refreshToken);
    }

    public AccessAndRefreshToken authenticate(User request) {
        authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
                  request.getUsername(),
                  request.getPassword()
          )
        );

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new AccessAndRefreshToken(accessToken, refreshToken);
    }


    public ResponseEntity<AccessAndRefreshToken> refreshToken(TokenDto tokenDto) {
        String token = tokenDto.getToken();
        String userName = jwtService.extractUsername(token);
        User user = userRepository.findByUsername(userName).orElse(null);

        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        boolean isTokenValid = jwtService.isValid(token, userDetails);


        if(user != null && isTokenValid) {
            String accessToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            return ResponseEntity.ok(new AccessAndRefreshToken(accessToken, refreshToken));
        } else {
            return ResponseEntity.status(401).body(new AccessAndRefreshToken("error","error"));
        }
    }
}
