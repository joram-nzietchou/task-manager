package com.taskmanager.task_manager_backend.exception;

// 📌 RÔLE : Exception levée quand une ressource n'existe pas (404).
// 📌 POINTS CLÉS :
//   - RuntimeException → pas obligé de la déclarer dans throws
//   - Sera gérée par le GlobalExceptionHandler (qu'on créera plus tard)

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}