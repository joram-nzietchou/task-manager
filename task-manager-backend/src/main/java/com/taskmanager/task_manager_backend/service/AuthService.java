package com.taskmanager.task_manager_backend.service;

import com.taskmanager.task_manager_backend.dto.AuthRequest;
import com.taskmanager.task_manager_backend.dto.AuthResponse;
import com.taskmanager.task_manager_backend.dto.RegisterRequest;
import com.taskmanager.task_manager_backend.entity.User;
import com.taskmanager.task_manager_backend.repository.UserRepository;
import com.taskmanager.task_manager_backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// 📌 RÔLE : Logique métier d'authentification (inscription + connexion).
// 📌 POINTS CLÉS :
//   - register() : hash le password, sauvegarde le user, retourne un JWT
//   - login() : délègue à AuthenticationManager, puis génère un JWT
//   - Utilise BCrypt via PasswordEncoder (jamais de mot de passe en clair)
// 📌 QUESTION POSSIBLE : "Pourquoi AuthenticationManager pour login ?"
//     → Il gère la vérification email/password selon la config Spring Security
//       et lance BadCredentialsException si KO.

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Cet email est déjà utilisé");
        }

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .build();

        userRepository.save(user);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities("USER")
                .build();

        String token = jwtService.generateToken(userDetails);
        return new AuthResponse(token, user.getEmail(), user.getFullName());
    }

    public AuthResponse login(AuthRequest request) {
        // Lève BadCredentialsException si email/password incorrects
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Identifiants invalides"));

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities("USER")
                .build();

        String token = jwtService.generateToken(userDetails);
        return new AuthResponse(token, user.getEmail(), user.getFullName());
    }
}