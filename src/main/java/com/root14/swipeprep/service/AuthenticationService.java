package com.root14.swipeprep.service;

import com.root14.swipeprep.dto.UserDto;
import com.root14.swipeprep.dto.UserRequest;
import com.root14.swipeprep.dto.UserResponse;
import com.root14.swipeprep.entity.User;
import com.root14.swipeprep.enums.Role;
import com.root14.swipeprep.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public UserResponse save(UserDto userDto) {
        User user = User.builder().username(userDto.getUsername())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .role(Role.USER).build();

        userRepository.save(user);

        var token = jwtService.generateToken(user);

        return UserResponse.builder().token(token).build();
    }

    public UserResponse auth(UserRequest userRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));
        User user = userRepository.findByUsername(userRequest.getUsername()).orElseThrow();
        String token = jwtService.generateToken(user);
        return UserResponse.builder().token(token).build();
    }
}
