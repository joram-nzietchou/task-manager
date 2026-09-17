// 📌 RÔLE : Types TypeScript partagés dans toute l'application.
// 📌 POINTS CLÉS :
//   - Types alignés avec les DTOs du backend Spring Boot
//   - TaskStatus = union de strings (au lieu d'enum) → plus simple en TS

export interface User {
  email: string;
  fullName: string;
}

export interface AuthResponse {
  token: string;
  email: string;
  fullName: string;
}

export type TaskStatus = 'TODO' | 'IN_PROGRESS' | 'DONE';

export interface Task {
  id: number;
  title: string;
  description?: string;
  status: TaskStatus;
  createdAt: string;
  updatedAt: string;
}

export interface TaskRequest {
  title: string;
  description?: string;
  status?: TaskStatus;
}

export interface ApiError {
  timestamp: string;
  status: number;
  message: string;
  errors?: Record<string, string>;
}