import axiosInstance from './axios.config';

export const medicoService = {
  // Crear médico
  crearMedico: async (medicoData) => {
    const response = await axiosInstance.post('/medicos', medicoData);
    return response.data;
  },

  // Listar todos los médicos
  listarMedicos: async () => {
    const response = await axiosInstance.get('/medicos');
    return response.data;
  },

  // Obtener médico por ID
  obtenerMedico: async (id) => {
    const response = await axiosInstance.get(`/medicos/${id}`);
    return response.data;
  },

  // Actualizar médico
  actualizarMedico: async (id, medicoData) => {
    const response = await axiosInstance.put(`/medicos/${id}`, medicoData);
    return response.data;
  },

  // Eliminar médico
  eliminarMedico: async (id) => {
    await axiosInstance.delete(`/medicos/${id}`);
  },

  // Bloquear día
  bloquearDia: async (id, bloqueoData) => {
    const response = await axiosInstance.post(`/medicos/${id}/bloquear-dia`, bloqueoData);
    return response.data;
  },

  // Obtener especialidades de un médico
  obtenerEspecialidades: async (id) => {
    const response = await axiosInstance.get(`/medicos/${id}/especialidades`);
    return response.data;
  },
};
