package com.taskmanager.task_manager_backend.controller;

import com.taskmanager.task_manager_backend.dto.TaskRequest;
import com.taskmanager.task_manager_backend.dto.TaskResponse;
import com.taskmanager.task_manager_backend.entity.TaskStatus;
import com.taskmanager.task_manager_backend.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 📌 RÔLE : Expose les endpoints CRUD des tâches.
// 📌 POINTS CLÉS :
//   - @AuthenticationPrincipal → injecte l'utilisateur connecté (depuis le JWT)
//   - Filtres optionnels via query params (status, search)
//   - Toutes les routes nécessitent un JWT valide (config dans SecurityConfig)
// 📌 QUESTION POSSIBLE : "Comment sait-on que c'est le bon utilisateur ?"
//     → Le JWT contient l'email. Le filtre JWT authentifie et injecte le UserDetails.

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Tâches", description = "CRUD des tâches de l'utilisateur connecté")
@SecurityRequirement(name = "bearerAuth")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    @Operation(summary = "Liste les tâches (filtrage par statut et recherche)")
    public ResponseEntity<List<TaskResponse>> list(
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) String search) {

        List<TaskResponse> tasks = taskService.list(principal.getUsername(), status, search)
                .stream()
                .map(TaskResponse::from)
                .toList();
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    @Operation(summary = "Créer une tâche")
    public ResponseEntity<TaskResponse> create(
            @AuthenticationPrincipal UserDetails principal,
            @Valid @RequestBody TaskRequest request) {
        return ResponseEntity.ok(TaskResponse.from(taskService.create(principal.getUsername(), request)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une tâche")
    public ResponseEntity<TaskResponse> update(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request) {
        return ResponseEntity.ok(TaskResponse.from(taskService.update(principal.getUsername(), id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une tâche")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long id) {
        taskService.delete(principal.getUsername(), id);
        return ResponseEntity.noContent().build();
    }
}