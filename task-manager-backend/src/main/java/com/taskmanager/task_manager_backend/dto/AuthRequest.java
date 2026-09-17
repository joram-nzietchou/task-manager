package com.taskmanager.task_manager_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// 📌 RÔLE : Représente ce que le client envoie pour se connecter.
// 📌 POINTS CLÉS :
//   - Pas de contrainte @Size sur le password : on ne veut pas donner d'indice sur la longueur
// 📌 QUESTION POSSIBLE : "Pourquoi pas de validation sur le password ici ?"
//     → Sécurité : ne pas indiquer au client les règles exactes côté login

public record AuthRequest(
        @NotBlank @Email String email,
        @NotBlank String password
) {}