import axiosInstance from './axios.config';

export const pacienteService = {
  // Obtener paciente por ID
  obtenerPaciente: async (id) => {
    const response = await axiosInstance.get(`/pacientes/${id}`);
    return response.data;
  },

  // Actualizar paciente
  actualizarPaciente: async (id, pacienteData) => {
    const response = await axiosInstance.put(`/pacientes/${id}`, pacienteData);
    return response.data;
  },

  // Eliminar paciente
  eliminarPaciente: async (id) => {
    await axiosInstance.delete(`/pacientes/${id}`);
  },

  // Obtener turnos del paciente
  obtenerTurnos: async (id) => {
    const response = await axiosInstance.get(`/pacientes/${id}/turnos`);
    return response.data;
  },
};
