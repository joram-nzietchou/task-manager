// 📌 RÔLE : Affiche une tâche sous forme de carte avec les actions.
// 📌 POINTS CLÉS :
//   - Affiche titre, description, statut, dates
//   - Boutons Éditer et Supprimer
//   - Couleurs dynamiques selon le statut

import type { Task, TaskStatus } from '../types';

interface TaskCardProps {
  task: Task;
  onEdit: (task: Task) => void;
  onDelete: (id: number) => void;
}

const statusConfig: Record<TaskStatus, { label: string; color: string }> = {
  TODO: { label: 'À faire', color: 'bg-gray-100 text-gray-700' },
  IN_PROGRESS: { label: 'En cours', color: 'bg-yellow-100 text-yellow-700' },
  DONE: { label: 'Terminée', color: 'bg-green-100 text-green-700' },
};

export function TaskCard({ task, onEdit, onDelete }: TaskCardProps) {
  const status = statusConfig[task.status];

  return (
    <div className="bg-white rounded-lg shadow-md p-4 hover:shadow-lg transition-shadow">
      <div className="flex justify-between items-start mb-2">
        <h3 className="text-lg font-semibold text-gray-800 flex-1">{task.title}</h3>
        <span className={`px-2 py-1 rounded-full text-xs font-medium ${status.color}`}>
          {status.label}
        </span>
      </div>

      {task.description && (
        <p className="text-gray-600 text-sm mb-3">{task.description}</p>
      )}

      <div className="flex justify-between items-center mt-3 pt-3 border-t border-gray-100">
        <span className="text-xs text-gray-400">
          {new Date(task.createdAt).toLocaleDateString('fr-FR')}
        </span>
        <div className="flex gap-2">
          <button
            onClick={() => onEdit(task)}
            className="text-blue-600 hover:text-blue-800 text-sm font-medium"
          >
            Éditer
          </button>
          <button
            onClick={() => onDelete(task.id)}
            className="text-red-600 hover:text-red-800 text-sm font-medium"
          >
            Supprimer
          </button>
        </div>
      </div>
    </div>
  );
}