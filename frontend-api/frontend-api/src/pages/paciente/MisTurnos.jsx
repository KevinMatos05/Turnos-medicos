import { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { pacienteService } from '../../api/paciente.service';
import { turnoService } from '../../api/turno.service';
import { Calendar, Clock, MapPin, AlertCircle } from 'lucide-react';
import { formatDate, formatTime, getEstadoColor, getEstadoTexto } from '../../utils/helpers';

const MisTurnos = () => {
  const { user } = useAuth();
  const [turnos, setTurnos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    cargarTurnos();
  }, []);

  const cargarTurnos = async () => {
    try {
      setLoading(true);
      // Aquí deberías tener el ID del paciente desde el contexto de usuario
      const data = await pacienteService.obtenerTurnos(user.id);
      setTurnos(data);
    } catch (err) {
      setError('Error al cargar los turnos');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleCancelar = async (turnoId) => {
    if (!window.confirm('¿Estás seguro de que deseas cancelar este turno?')) {
      return;
    }

    try {
      await turnoService.cancelarTurno(turnoId);
      alert('Turno cancelado exitosamente');
      cargarTurnos();
    } catch (err) {
      alert('Error al cancelar el turno: ' + (err.response?.data?.message || err.message));
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  return (
    <div>
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Mis Turnos</h1>
        <p className="text-gray-600 mt-2">Gestiona tus citas médicas</p>
      </div>

      {error && (
        <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-md flex items-start">
          <AlertCircle className="h-5 w-5 text-red-600 mr-2 flex-shrink-0 mt-0.5" />
          <p className="text-sm text-red-600">{error}</p>
        </div>
      )}

      {turnos.length === 0 ? (
        <div className="bg-white rounded-lg shadow-md p-8 text-center">
          <Calendar className="h-16 w-16 text-gray-400 mx-auto mb-4" />
          <h3 className="text-xl font-semibold text-gray-900 mb-2">No tienes turnos programados</h3>
          <p className="text-gray-600 mb-6">Solicita tu primer turno médico</p>
          <a
            href="/paciente/nuevo-turno"
            className="inline-block bg-blue-600 text-white px-6 py-3 rounded-md hover:bg-blue-700 transition-colors"
          >
            Solicitar Turno
          </a>
        </div>
      ) : (
        <div className="space-y-4">
          {turnos.map((turno) => (
            <div key={turno.id} className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow">
              <div className="flex items-start justify-between">
                <div className="flex-1">
                  <div className="flex items-center space-x-3 mb-3">
                    <h3 className="text-xl font-semibold text-gray-900">
                      {turno.medico?.nombre} {turno.medico?.apellido}
                    </h3>
                    <span className={`px-3 py-1 rounded-full text-sm font-medium ${getEstadoColor(turno.estado)}`}>
                      {getEstadoTexto(turno.estado)}
                    </span>
                  </div>

                  <div className="space-y-2 text-gray-600">
                    <div className="flex items-center">
                      <Calendar className="h-4 w-4 mr-2" />
                      <span>{formatDate(turno.fecha)}</span>
                    </div>
                    <div className="flex items-center">
                      <Clock className="h-4 w-4 mr-2" />
                      <span>{formatTime(turno.horaInicio)} - {formatTime(turno.horaFin)}</span>
                    </div>
                    {turno.especialidad && (
                      <div className="flex items-center">
                        <MapPin className="h-4 w-4 mr-2" />
                        <span>{turno.especialidad.nombre}</span>
                      </div>
                    )}
                  </div>

                  {turno.observaciones && (
                    <div className="mt-3 p-3 bg-gray-50 rounded-md">
                      <p className="text-sm text-gray-700">
                        <strong>Observaciones:</strong> {turno.observaciones}
                      </p>
                    </div>
                  )}
                </div>

                <div className="ml-4">
                  {(turno.estado === 'PENDIENTE' || turno.estado === 'CONFIRMADO') && (
                    <button
                      onClick={() => handleCancelar(turno.id)}
                      className="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700 transition-colors text-sm"
                    >
                      Cancelar
                    </button>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default MisTurnos;
