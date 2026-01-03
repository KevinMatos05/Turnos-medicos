import { createContext, useContext, useState, useEffect } from 'react';
import { jwtDecode } from 'jwt-decode';
import { authService } from '../api/auth.service';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Verificar si hay un token guardado al cargar la app
    const token = localStorage.getItem('token');
    const savedUser = localStorage.getItem('user');
    
    if (token && savedUser) {
      try {
        const decoded = jwtDecode(token);
        // Verificar si el token no ha expirado
        if (decoded.exp * 1000 > Date.now()) {
          setUser(JSON.parse(savedUser));
        } else {
          // Token expirado
          localStorage.removeItem('token');
          localStorage.removeItem('user');
        }
      } catch (error) {
        console.error('Error al decodificar token:', error);
        localStorage.removeItem('token');
        localStorage.removeItem('user');
      }
    }
    setLoading(false);
  }, []);

  const login = async (email, password) => {
    try {
      const response = await authService.login(email, password);
      
      // Guardar token y usuario
      localStorage.setItem('token', response.token);
      localStorage.setItem('user', JSON.stringify(response));
      setUser(response);
      
      return response;
    } catch (error) {
      throw error;
    }
  };

  const register = async (userData, tipo) => {
    try {
      let response;
      if (tipo === 'paciente') {
        response = await authService.registerPaciente(userData);
      } else if (tipo === 'medico') {
        response = await authService.registerMedico(userData);
      }
      
      // Guardar token y usuario
      localStorage.setItem('token', response.token);
      localStorage.setItem('user', JSON.stringify(response));
      setUser(response);
      
      return response;
    } catch (error) {
      throw error;
    }
  };

  const logout = () => {
    authService.logout();
    setUser(null);
  };

  const value = {
    user,
    login,
    register,
    logout,
    loading,
    isAuthenticated: !!user,
    isAdmin: user?.rol === 'ADMIN',
    isMedico: user?.rol === 'MEDICO',
    isPaciente: user?.rol === 'PACIENTE',
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth debe ser usado dentro de un AuthProvider');
  }
  return context;
};
