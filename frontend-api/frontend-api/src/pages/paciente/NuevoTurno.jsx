import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { turnoService } from '../../api/turno.service';
import { medicoService } from '../../api/medico.service';
import { especialidadService } from '../../api/especialidad.service';
import { Calendar, AlertCircle } from 'lucide-react';

const NuevoTurno = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    medicoId: '',
    especialidadId: '',
    fecha: '',
    horaInicio: '',
    observaciones: '',
  });
  const [medicos, setMedicos] = useState([]);
  const [especialidades, setEspecialidades] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    cargarDatos();
  }, []);

  const cargarDatos = async () => {
    try {
      const [medicosData, especialidadesData] = await Promise.all([
        medicoService.listarMedicos(),
        especialidadService.listarEspecialidades(),
      ]);
      
      console.log('Médicos cargados:', medicosData);
      console.log('Especialidades cargadas:', especialidadesData);
      
      setMedicos(medicosData || []);
      setEspecialidades(especialidadesData || []);
      
      if ((!medicosData || medicosData.length === 0) && (!especialidadesData || especialidadesData.length === 0)) {
        setError('No hay médicos ni especialidades disponibles. Contacte al administrador.');
      } else if (!medicosData || medicosData.length === 0) {
        setError('No hay médicos disponibles en este momento.');
      } else if (!especialidadesData || especialidadesData.length === 0) {
        setError('No hay especialidades disponibles en este momento.');
      }
    } catch (err) {
      console.error('Error al cargar datos:', err);
      setError('Error al conectar con el servidor. Por favor, intente nuevamente.');
    }
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      await turnoService.crearTurno(formData);
      alert('Turno solicitado exitosamente');
      navigate('/paciente/turnos');
    } catch (err) {
      setError(err.response?.data?.message || 'Error al solicitar el turno. Intenta nuevamente.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Solicitar Nuevo Turno</h1>
        <p className="text-gray-600 mt-2">Completa el formulario para solicitar tu cita médica</p>
      </div>

      <div className="max-w-2xl bg-white rounded-lg shadow-md p-8">
        {error && (
          <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-md flex items-start">
            <AlertCircle className="h-5 w-5 text-red-600 mr-2 flex-shrink-0 mt-0.5" />
            <p className="text-sm text-red-600">{error}</p>
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label htmlFor="especialidadId" className="block text-sm font-medium text-gray-700 mb-2">
              Especialidad
            </label>
            <select
              id="especialidadId"
              name="especialidadId"
              required
              value={formData.especialidadId}
              onChange={handleChange}
              className="w-full px-4 py-3 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none"
            >
              <option value="">Seleccionar especialidad</option>
              {especialidades.map((esp) => (
                <option key={esp.id} value={esp.id}>
                  {esp.nombre}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label htmlFor="medicoId" className="block text-sm font-medium text-gray-700 mb-2">
              Médico
            </label>
            <select
              id="medicoId"
              name="medicoId"
              required
              value={formData.medicoId}
              onChange={handleChange}
              className="w-full px-4 py-3 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none"
            >
              <option value="">Seleccionar médico</option>
              {medicos.map((medico) => (
                <option key={medico.id} value={medico.id}>
                  Dr./Dra. {medico.nombre} {medico.apellido}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label htmlFor="fecha" className="block text-sm font-medium text-gray-700 mb-2">
              Fecha
            </label>
            <input
              id="fecha"
              name="fecha"
              type="date"
              required
              value={formData.fecha}
              onChange={handleChange}
              min={new Date().toISOString().split('T')[0]}
              className="w-full px-4 py-3 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none"
            />
          </div>

          <div>
            <label htmlFor="horaInicio" className="block text-sm font-medium text-gray-700 mb-2">
              Hora
            </label>
            <input
              id="horaInicio"
              name="horaInicio"
              type="time"
              required
              value={formData.horaInicio}
              onChange={handleChange}
              className="w-full px-4 py-3 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none"
            />
          </div>

          <div>
            <label htmlFor="observaciones" className="block text-sm font-medium text-gray-700 mb-2">
              Observaciones (opcional)
            </label>
            <textarea
              id="observaciones"
              name="observaciones"
              rows="4"
              value={formData.observaciones}
              onChange={handleChange}
              className="w-full px-4 py-3 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none"
              placeholder="Motivo de la consulta o información adicional..."
            />
          </div>

          <div className="flex space-x-4">
            <button
              type="submit"
              disabled={loading}
              className="flex-1 bg-blue-600 text-white py-3 px-6 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-colors disabled:bg-blue-400 disabled:cursor-not-allowed font-medium"
            >
              {loading ? 'Solicitando...' : 'Solicitar Turno'}
            </button>
            <button
              type="button"
              onClick={() => navigate('/paciente/dashboard')}
              className="flex-1 bg-gray-200 text-gray-700 py-3 px-6 rounded-md hover:bg-gray-300 transition-colors font-medium"
            >
              Cancelar
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default NuevoTurno;
