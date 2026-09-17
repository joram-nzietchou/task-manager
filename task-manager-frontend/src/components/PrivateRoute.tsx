// 📌 RÔLE : Protège les routes qui nécessitent une authentification.
// 📌 POINTS CLÉS :
//   - Si non authentifié → redirige vers /login
//   - Si chargement en cours → affiche un loader
// 📌 QUESTION POSSIBLE : "Pourquoi pas juste vérifier le token ?"
//     → On vérifie via useAuth() qui est la source de vérité unique.

import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import type { ReactNode } from 'react';

interface PrivateRouteProps {
  children: ReactNode;
}

export function PrivateRoute({ children }: PrivateRouteProps) {
  const { isAuthenticated, isLoading } = useAuth();

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-gray-500">Chargement...</div>
      </div>
    );
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return <>{children}</>;
}