// 📌 RÔLE : Page principale — liste, filtres et CRUD des tâches.

import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { tasksApi } from '../api/tasks';
import { getErrorMessage } from '../api/client';
import { TaskCard } from '../components/TaskCard';
import { TaskForm } from '../components/TaskForm';
import type { Task, TaskRequest, TaskStatus } from '../types';

export function TasksPage() {
  const { user, logout } = useAuth();

  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [filterStatus, setFilterStatus] = useState<TaskStatus | ''>('');
  const [search, setSearch] = useState('');

  const [showForm, setShowForm] = useState(false);
  const [editingTask, setEditingTask] = useState<Task | null>(null);

  const loadTasks = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await tasksApi.list({
        status: filterStatus || undefined,
        search: search || undefined,
      });
      setTasks(data);
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadTasks();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [filterStatus]);

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    loadTasks();
  };

  const handleCreate = async (payload: TaskRequest) => {
    await tasksApi.create(payload);
    setShowForm(false);
    loadTasks();
  };

  const handleUpdate = async (payload: TaskRequest) => {
    if (!editingTask) return;
    await tasksApi.update(editingTask.id, payload);
    setEditingTask(null);
    loadTasks();
  };

  const handleDelete = async (id: number) => {
    if (!confirm('Supprimer cette tâche ?')) return;
    try {
      await tasksApi.delete(id);
      loadTasks();
    } catch (err) {
      setError(getErrorMessage(err));
    }
  };

  const handleEditClick = (task: Task) => {
    setEditingTask(task);
    setShowForm(false);
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow-sm border-b border-gray-200">
        <div className="max-w-4xl mx-auto px-4 py-4 flex justify-between items-center">
          <div>
            <h1 className="text-2xl font-bold text-gray-800">Task Manager</h1>
            <p className="text-sm text-gray-500">Bonjour, {user?.fullName}</p>
          </div>
          <button
            onClick={logout}
            className="text-gray-600 hover:text-gray-800 text-sm font-medium"
          >
            Déconnexion
          </button>
        </div>
      </header>

      <main className="max-w-4xl mx-auto px-4 py-6">
        {/* Erreur */}
        {error && (
          <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-md mb-4">
            {error}
          </div>
        )}

        {/* Barre de filtres */}
        <div className="bg-white rounded-lg shadow-md p-4 mb-6">
          <div className="flex flex-wrap gap-3 items-end">
            <div className="flex-1 min-w-[200px]">
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Recherche
              </label>
              <form onSubmit={handleSearch} className="flex gap-2">
                <input
                  type="text"
                  value={search}
                  onChange={(e) => setSearch(e.target.value)}
                  placeholder="Rechercher par titre..."
                  className="flex-1 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
                <button
                  type="submit"
                  className="bg-gray-200 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-300"
                >
                  Chercher
                </button>
              </form>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Statut
              </label>
              <select
                value={filterStatus}
                onChange={(e) => setFilterStatus(e.target.value as TaskStatus | '')}
                className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Tous</option>
                <option value="TODO">À faire</option>
                <option value="IN_PROGRESS">En cours</option>
                <option value="DONE">Terminées</option>
              </select>
            </div>

            <button
              onClick={() => {
                setShowForm(true);
                setEditingTask(null);
              }}
              className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 font-medium"
            >
              + Nouvelle tâche
            </button>
          </div>
        </div>

        {/* Formulaire de création */}
        {showForm && !editingTask && (
          <TaskForm
            onSubmit={handleCreate}
            onCancel={() => setShowForm(false)}
          />
        )}

        {/* Formulaire d'édition */}
        {editingTask && (
          <TaskForm
            initialTask={editingTask}
            onSubmit={handleUpdate}
            onCancel={() => setEditingTask(null)}
          />
        )}

        {/* Liste des tâches */}
        {loading ? (
          <div className="text-center py-10 text-gray-500">Chargement...</div>
        ) : tasks.length === 0 ? (
          <div className="text-center py-10 bg-white rounded-lg shadow-md">
            <p className="text-gray-500 mb-2">Aucune tâche pour l'instant</p>
            <p className="text-sm text-gray-400">
              Cliquez sur "Nouvelle tâche" pour commencer
            </p>
          </div>
        ) : (
          <div className="grid gap-4 sm:grid-cols-2">
            {tasks.map((task) => (
              <TaskCard
                key={task.id}
                task={task}
                onEdit={handleEditClick}
                onDelete={handleDelete}
              />
            ))}
          </div>
        )}
      </main>
    </div>
  );
}