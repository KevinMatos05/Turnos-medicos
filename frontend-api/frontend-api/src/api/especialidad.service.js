import axiosInstance from './axios.config';

export const especialidadService = {
  // Listar todas las especialidades
  listarEspecialidades: async () => {
    const response = await axiosInstance.get('/especialidades');
    return response.data;
  },

  // Crear especialidad
  crearEspecialidad: async (especialidadData) => {
    const response = await axiosInstance.post('/especialidades', especialidadData);
    return response.data;
  },

  // Actualizar especialidad
  actualizarEspecialidad: async (id, especialidadData) => {
    const response = await axiosInstance.put(`/especialidades/${id}`, especialidadData);
    return response.data;
  },

  // Eliminar especialidad
  eliminarEspecialidad: async (id) => {
    await axiosInstance.delete(`/especialidades/${id}`);
  },
};
