package com.taskmanager.task_manager_backend.repository;

import com.taskmanager.task_manager_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// 📌 RÔLE : Accès aux données de la table "users".
// 📌 POINTS CLÉS :
//   - JpaRepository<User, Long> : User = entité, Long = type de l'ID
//   - Spring Data génère TOUT le code SQL automatiquement
//   - findByEmail : méthode dérivée → Spring génère "SELECT * FROM users WHERE email = ?"
//   - existsByEmail : "SELECT COUNT(*) > 0 FROM users WHERE email = ?"
// 📌 QUESTION POSSIBLE : "Comment Spring Data sait-il générer le SQL ?"
//     → Par convention de nommage : findByXxx → WHERE xxx = ?
//     → existsByXxx → SELECT COUNT > 0 WHERE xxx = ?

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}