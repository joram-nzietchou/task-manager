// 📌 RÔLE : Client HTTP centralisé (axios) pour appeler l'API Spring Boot.

import axios, { type AxiosError, type InternalAxiosRequestConfig } from 'axios';

// En prod (Vercel) → utilise VITE_API_URL
// En local → utilise localhost:8080 par défaut
const BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

export const apiClient = axios.create({
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 30000, // ⚠️ 30s (Render peut mettre du temps à démarrer)
});

// Intercepteur de requête : injecte le token JWT
apiClient.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = localStorage.getItem('token');
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Intercepteur de réponse : gère les erreurs globales
apiClient.interceptors.response.use(
  (response) => response,
  (error: AxiosError) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      if (window.location.pathname !== '/login') {
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export function getErrorMessage(error: unknown): string {
  if (axios.isAxiosError(error)) {
    const apiError = error.response?.data as { message?: string; errors?: Record<string, string> };
    if (apiError?.errors) {
      return Object.values(apiError.errors).join(', ');
    }
    if (apiError?.message) {
      return apiError.message;
    }
    return error.message;
  }
  if (error instanceof Error) {
    return error.message;
  }
  return 'Une erreur est survenue';
}