package com.taskmanager.task_manager_backend.security;

import com.taskmanager.task_manager_backend.entity.User;
import com.taskmanager.task_manager_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

// 📌 RÔLE : Indique à Spring Security comment charger un utilisateur depuis la BDD.
// 📌 POINTS CLÉS :
//   - Implémente UserDetailsService (interface Spring Security)
//   - loadUserByUsername() : Spring l'appelle à chaque authentification
//   - Retourne un objet UserDetails (adapté du User JPA)
// 📌 QUESTION POSSIBLE : "Pourquoi ne pas retourner directement User ?"
//     → UserDetails est le contrat Spring Security. Il faut une classe qui l'implémente.

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()  // pas de rôles pour l'instant
        );
    }
}