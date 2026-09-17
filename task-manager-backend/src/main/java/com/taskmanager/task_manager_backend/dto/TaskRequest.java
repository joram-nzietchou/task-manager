package com.taskmanager.task_manager_backend.dto;

import com.taskmanager.task_manager_backend.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// 📌 RÔLE : Ce que le client envoie pour créer ou modifier une tâche.
// 📌 POINTS CLÉS :
//   - status optionnel : si null → TODO par défaut
//   - description optionnelle
// 📌 QUESTION POSSIBLE : "Pourquoi ne pas valider le status ?"
//     → Si fourni et invalide, Jackson lèvera une erreur de désérialisation

public record TaskRequest(
        @NotBlank(message = "Le titre est obligatoire")
        @Size(max = 200, message = "Le titre ne doit pas dépasser 200 caractères")
        String title,

        @Size(max = 2000, message = "La description ne doit pas dépasser 2000 caractères")
        String description,

        TaskStatus status
) {}