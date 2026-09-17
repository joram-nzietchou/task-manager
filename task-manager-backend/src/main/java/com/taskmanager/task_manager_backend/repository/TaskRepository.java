package com.taskmanager.task_manager_backend.repository;

import com.taskmanager.task_manager_backend.entity.Task;
import com.taskmanager.task_manager_backend.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// 📌 RÔLE : Accès aux données de la table "tasks".
// 📌 POINTS CLÉS :
//   - Toutes les méthodes filtrent par userId (sécurité : un user ne voit que ses tâches)
//   - OrderByCreatedAtDesc : tri décroissant (les plus récentes en premier)
//   - findByIdAndUserId : vérifie que la tâche appartient bien à l'utilisateur
// 📌 QUESTION POSSIBLE : "Pourquoi filtrer par userId dans le repository ?"
//     → Sécurité : empêche un user de modifier/supprimer la tâche d'un autre

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Task> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, TaskStatus status);

    List<Task> findByUserIdAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(Long userId, String title);

    Optional<Task> findByIdAndUserId(Long id, Long userId);
}