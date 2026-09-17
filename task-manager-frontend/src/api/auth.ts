// 📌 RÔLE : Fonctions pour appeler les endpoints d'authentification.
// 📌 POINTS CLÉS :
//   - register() et login() retournent un AuthResponse (token + infos)
//   - Typé avec les DTOs TypeScript

import { apiClient } from './client';
import type { AuthResponse } from '../types';

export interface RegisterPayload {
  email: string;
  password: string;
  fullName: string;
}

export interface LoginPayload {
  email: string;
  password: string;
}

export const authApi = {
  register: async (payload: RegisterPayload): Promise<AuthResponse> => {
    const { data } = await apiClient.post<AuthResponse>('/api/auth/register', payload);
    return data;
  },

  login: async (payload: LoginPayload): Promise<AuthResponse> => {
    const { data } = await apiClient.post<AuthResponse>('/api/auth/login', payload);
    return data;
  },
};