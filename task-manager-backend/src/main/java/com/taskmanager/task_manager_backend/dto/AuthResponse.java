package com.taskmanager.task_manager_backend.dto;

// 📌 RÔLE : Réponse renvoyée après un register ou un login réussi.
// 📌 POINTS CLÉS :
//   - Contient le JWT que le client stockera et renverra à chaque requête
//   - Contient aussi les infos de base pour affichage (pas besoin de re-call)
// 📌 QUESTION POSSIBLE : "Faut-il exposer l'ID ?"
//     → Pas nécessaire, l'email identifie l'utilisateur. Moins on expose, mieux c'est.

public record AuthResponse(
        String token,
        String email,
        String fullName
) {}