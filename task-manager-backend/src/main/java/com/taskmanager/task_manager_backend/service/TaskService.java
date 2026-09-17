package com.taskmanager.task_manager_backend.service;

import com.taskmanager.task_manager_backend.dto.TaskRequest;
import com.taskmanager.task_manager_backend.entity.Task;
import com.taskmanager.task_manager_backend.entity.TaskStatus;
import com.taskmanager.task_manager_backend.entity.User;
import com.taskmanager.task_manager_backend.exception.ResourceNotFoundException;
import com.taskmanager.task_manager_backend.repository.TaskRepository;
import com.taskmanager.task_manager_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 📌 RÔLE : Logique métier des tâches (CRUD + filtres).
// 📌 POINTS CLÉS :
//   - Toutes les méthodes reçoivent l'email de l'utilisateur connecté
//   - Isolation par utilisateur : un user ne voit que ses tâches
//   - @Transactional : gestion automatique des transactions BDD
// 📌 QUESTION POSSIBLE : "Pourquoi @Transactional sur certaines méthodes ?"
//     → Garantit qu'une opération réussit ou échoue en bloc.
//     → readOnly=true optimise les SELECT (pas de flush Hibernate).

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private User getCurrentUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));
    }

    @Transactional(readOnly = true)
    public List<Task> list(String email, TaskStatus status, String search) {
        User user = getCurrentUser(email);

        if (search != null && !search.isBlank()) {
            return taskRepository.findByUserIdAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(
                    user.getId(), search);
        }
        if (status != null) {
            return taskRepository.findByUserIdAndStatusOrderByCreatedAtDesc(user.getId(), status);
        }
        return taskRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    @Transactional
    public Task create(String email, TaskRequest req) {
        User user = getCurrentUser(email);

        Task task = Task.builder()
                .title(req.title())
                .description(req.description())
                .status(req.status() != null ? req.status() : TaskStatus.TODO)
                .user(user)
                .build();

        return taskRepository.save(task);
    }

    @Transactional
    public Task update(String email, Long id, TaskRequest req) {
        User user = getCurrentUser(email);

        Task task = taskRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Tâche introuvable"));

        task.setTitle(req.title());
        task.setDescription(req.description());
        if (req.status() != null) {
            task.setStatus(req.status());
        }

        return taskRepository.save(task);
    }

    @Transactional
    public void delete(String email, Long id) {
        User user = getCurrentUser(email);

        Task task = taskRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Tâche introuvable"));

        taskRepository.delete(task);
    }
}