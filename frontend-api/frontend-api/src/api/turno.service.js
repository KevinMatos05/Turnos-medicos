import axiosInstance from './axios.config';

export const turnoService = {
  // Crear turno
  crearTurno: async (turnoData) => {
    const response = await axiosInstance.post('/turnos', turnoData);
    return response.data;
  },

  // Obtener turno por ID
  obtenerTurno: async (id) => {
    const response = await axiosInstance.get(`/turnos/${id}`);
    return response.data;
  },

  // Listar todos los turnos
  listarTurnos: async () => {
    const response = await axiosInstance.get('/turnos');
    return response.data;
  },

  // Cancelar turno
  cancelarTurno: async (id) => {
    await axiosInstance.delete(`/turnos/${id}`);
  },

  // Confirmar turno
  confirmarTurno: async (id) => {
    const response = await axiosInstance.put(`/turnos/${id}/confirmar`);
    return response.data;
  },

  // Marcar asistencia
  marcarAsistencia: async (id) => {
    const response = await axiosInstance.put(`/turnos/${id}/marcar-asistencia`);
    return response.data;
  },

  // Marcar inasistencia
  marcarInasistencia: async (id) => {
    const response = await axiosInstance.put(`/turnos/${id}/marcar-inasistencia`);
    return response.data;
  },
};
