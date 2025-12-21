package com.yusurg.backend_yusurg.service;

import com.yusurg.backend_yusurg.dto.AuthResponse;
import com.yusurg.backend_yusurg.dto.LoginRequest;
import com.yusurg.backend_yusurg.dto.SignupRequest;
import com.yusurg.backend_yusurg.model.Player;
import com.yusurg.backend_yusurg.model.User;
import com.yusurg.backend_yusurg.repository.UserRepository;
import com.yusurg.backend_yusurg.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists: " + request.getUsername());
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }

        Player player = new Player(request.getUsername());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_USER");
        user.setPlayer(player);

        User savedUser = userRepository.save(user);

        String token = jwtUtil.generateToken(
                savedUser.getId(),
                savedUser.getPlayer().getId(),
                savedUser.getUsername()
        );

        return new AuthResponse(
                token,
                savedUser.getId(),
                savedUser.getPlayer().getId(),
                savedUser.getUsername(),
                savedUser.getEmail()
        );
    }

    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            User user = (User) authentication.getPrincipal();

            if (user.getPlayer() != null) {
                user.getPlayer().setLastLogin(java.time.LocalDateTime.now());
            }

            userRepository.save(user);

            String token = jwtUtil.generateToken(
                    user.getId(),
                    user.getPlayer() != null ? user.getPlayer().getId() : null,
                    user.getUsername()
            );

            return new AuthResponse(
                    token,
                    user.getId(),
                    user.getPlayer() != null ? user.getPlayer().getId() : null,
                    user.getUsername(),
                    user.getEmail()
            );

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid username or password");
        } catch (AuthenticationException e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }
}
