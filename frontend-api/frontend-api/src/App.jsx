import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Layout from './components/layout/Layout';
import PrivateRoute from './components/common/PrivateRoute';

// Páginas de autenticación
import Login from './pages/auth/Login';
import Registro from './pages/auth/Registro';

// Páginas de paciente
import PacienteDashboard from './pages/paciente/PacienteDashboard';

// Páginas de médico
import MedicoDashboard from './pages/medico/MedicoDashboard';

// Páginas de admin
import AdminDashboard from './pages/admin/AdminDashboard';

import './App.css';

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          {/* Rutas públicas */}
          <Route path="/login" element={<Login />} />
          <Route path="/registro" element={<Registro />} />

          {/* Rutas protegidas con Layout */}
          <Route element={<Layout />}>
            {/* Rutas de paciente */}
            <Route
              path="/paciente/dashboard"
              element={
                <PrivateRoute requiredRole="PACIENTE">
                  <PacienteDashboard />
                </PrivateRoute>
              }
            />

            {/* Rutas de médico */}
            <Route
              path="/medico/dashboard"
              element={
                <PrivateRoute requiredRole="MEDICO">
                  <MedicoDashboard />
                </PrivateRoute>
              }
            />

            {/* Rutas de admin */}
            <Route
              path="/admin/dashboard"
              element={
                <PrivateRoute requiredRole="ADMIN">
                  <AdminDashboard />
                </PrivateRoute>
              }
            />
          </Route>

          {/* Ruta por defecto */}
          <Route path="/" element={<Navigate to="/login" replace />} />
          
          {/* Página de no autorizado */}
          <Route path="/unauthorized" element={
            <div className="min-h-screen flex items-center justify-center bg-gray-50">
              <div className="text-center">
                <h1 className="text-4xl font-bold text-gray-900 mb-4">Acceso No Autorizado</h1>
                <p className="text-gray-600 mb-8">No tienes permisos para acceder a esta página.</p>
                <a href="/login" className="text-blue-600 hover:text-blue-700 font-medium">
                  Volver al inicio
                </a>
              </div>
            </div>
          } />

          {/* 404 */}
          <Route path="*" element={
            <div className="min-h-screen flex items-center justify-center bg-gray-50">
              <div className="text-center">
                <h1 className="text-4xl font-bold text-gray-900 mb-4">404 - Página No Encontrada</h1>
                <p className="text-gray-600 mb-8">La página que buscas no existe.</p>
                <a href="/login" className="text-blue-600 hover:text-blue-700 font-medium">
                  Volver al inicio
                </a>
              </div>
            </div>
          } />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
