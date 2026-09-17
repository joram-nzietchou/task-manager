package com.taskmanager.task_manager_backend.dto;

import com.taskmanager.task_manager_backend.entity.Task;
import com.taskmanager.task_manager_backend.entity.TaskStatus;

import java.time.LocalDateTime;

// 📌 RÔLE : Représente une tâche renvoyée au client.
// 📌 POINTS CLÉS :
//   - Ne contient PAS l'entité User (évite le chargement LAZY + fuite de données)
//   - Méthode statique from() pour convertir Entity → DTO
// 📌 QUESTION POSSIBLE : "Pourquoi une méthode statique from() ?"
//     → Centralise la conversion au même endroit, plus facile à maintenir

public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static TaskResponse from(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}