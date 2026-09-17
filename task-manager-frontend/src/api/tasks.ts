// 📌 RÔLE : Fonctions pour appeler les endpoints CRUD des tâches.
// 📌 POINTS CLÉS :
//   - Chaque méthode correspond à un endpoint du backend
//   - Filtres optionnels passés en query params

import { apiClient } from './client';
import type { Task, TaskRequest, TaskStatus } from '../types';

export const tasksApi = {
  list: async (params?: { status?: TaskStatus; search?: string }): Promise<Task[]> => {
    const { data } = await apiClient.get<Task[]>('/api/tasks', { params });
    return data;
  },

  create: async (payload: TaskRequest): Promise<Task> => {
    const { data } = await apiClient.post<Task>('/api/tasks', payload);
    return data;
  },

  update: async (id: number, payload: TaskRequest): Promise<Task> => {
    const { data } = await apiClient.put<Task>(`/api/tasks/${id}`, payload);
    return data;
  },

  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`/api/tasks/${id}`);
  },
};