export const formatDate = (dateString) => {
  const date = new Date(dateString);
  return date.toLocaleDateString('es-ES', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  });
};

export const formatTime = (timeString) => {
  return timeString ? timeString.slice(0, 5) : '';
};

export const formatDateTime = (dateTimeString) => {
  const date = new Date(dateTimeString);
  return date.toLocaleString('es-ES', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });
};

export const getEstadoColor = (estado) => {
  const colores = {
    PENDIENTE: 'bg-yellow-100 text-yellow-800',
    CONFIRMADO: 'bg-blue-100 text-blue-800',
    COMPLETADO: 'bg-green-100 text-green-800',
    CANCELADO: 'bg-red-100 text-red-800',
    AUSENTE: 'bg-gray-100 text-gray-800',
  };
  return colores[estado] || 'bg-gray-100 text-gray-800';
};

export const getEstadoTexto = (estado) => {
  const textos = {
    PENDIENTE: 'Pendiente',
    CONFIRMADO: 'Confirmado',
    COMPLETADO: 'Completado',
    CANCELADO: 'Cancelado',
    AUSENTE: 'Ausente',
  };
  return textos[estado] || estado;
};
