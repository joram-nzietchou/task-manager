package com.taskmanager.task_manager_backend.controller;

import com.taskmanager.task_manager_backend.dto.AuthRequest;
import com.taskmanager.task_manager_backend.dto.AuthResponse;
import com.taskmanager.task_manager_backend.dto.RegisterRequest;
import com.taskmanager.task_manager_backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 📌 RÔLE : Expose les endpoints d'authentification.
// 📌 POINTS CLÉS :
//   - @Valid déclenche la validation des DTOs
//   - Retourne 200 avec AuthResponse (token + email + fullName)
// 📌 QUESTION POSSIBLE : "Pourquoi 200 et pas 201 sur register ?"
//     → 201 serait plus correct pour une création. 200 est acceptable, mais on peut
//       utiliser ResponseEntity.status(HttpStatus.CREATED) pour être rigoureux.

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "Inscription et connexion")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Inscription d'un nouvel utilisateur")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Connexion et obtention d'un JWT")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}