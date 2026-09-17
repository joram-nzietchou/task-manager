package com.taskmanager.task_manager_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// 📌 RÔLE : Représente ce que le client envoie lors de l'inscription.
// 📌 POINTS CLÉS :
//   - record Java = immuable, concis, parfait pour un DTO
//   - @NotBlank : refuse null, vide et espaces
//   - @Email : vérifie le format email
//   - @Size : contraint la longueur
// 📌 QUESTION POSSIBLE : "Pourquoi @NotBlank et pas @NotNull ?"
//     → @NotNull accepte "   " (espaces), @NotBlank le refuse

public record RegisterRequest(
        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "Format d'email invalide")
        String email,

        @NotBlank(message = "Le mot de passe est obligatoire")
        @Size(min = 6, max = 100, message = "Le mot de passe doit faire entre 6 et 100 caractères")
        String password,

        @NotBlank(message = "Le nom complet est obligatoire")
        @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
        String fullName
) {}