import axiosInstance from './axios.config';

export const authService = {
  // Login
  login: async (email, password) => {
    const response = await axiosInstance.post('/auth/login', { email, password });
    return response.data;
  },

  // Registro de paciente
  registerPaciente: async (userData) => {
    const response = await axiosInstance.post('/auth/register/paciente', userData);
    return response.data;
  },

  // Registro de médico
  registerMedico: async (userData) => {
    const response = await axiosInstance.post('/auth/register/medico', userData);
    return response.data;
  },

  // Logout
  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },
};
