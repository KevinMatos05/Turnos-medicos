import { useAuth } from '../../context/AuthContext';
import { Calendar, Clock, CheckCircle, XCircle } from 'lucide-react';

const PacienteDashboard = () => {
  const { user } = useAuth();

  const stats = [
    { 
      title: 'Turnos Pendientes', 
      value: '2', 
      icon: Clock, 
      color: 'bg-yellow-500',
      textColor: 'text-yellow-600'
    },
    { 
      title: 'Turnos Confirmados', 
      value: '1', 
      icon: CheckCircle, 
      color: 'bg-green-500',
      textColor: 'text-green-600'
    },
    { 
      title: 'Próximo Turno', 
      value: '15 Ene', 
      icon: Calendar, 
      color: 'bg-blue-500',
      textColor: 'text-blue-600'
    },
  ];

  return (
    <div>
      {/* Encabezado */}
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900">
          Bienvenido, {user?.nombre}!
        </h1>
        <p className="text-gray-600 mt-2">
          Aquí puedes ver un resumen de tus turnos médicos
        </p>
      </div>

      {/* Tarjetas de estadísticas */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        {stats.map((stat) => (
          <div key={stat.title} className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600 font-medium">{stat.title}</p>
                <p className={`text-3xl font-bold ${stat.textColor} mt-2`}>{stat.value}</p>
              </div>
              <div className={`${stat.color} p-3 rounded-full`}>
                <stat.icon className="h-6 w-6 text-white" />
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Acciones rápidas */}
      <div className="bg-white rounded-lg shadow-md p-6">
        <h2 className="text-xl font-bold text-gray-900 mb-4">Acciones Rápidas</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <button className="p-4 border-2 border-blue-200 rounded-lg hover:bg-blue-50 transition-colors text-left">
            <Calendar className="h-8 w-8 text-blue-600 mb-2" />
            <h3 className="font-semibold text-gray-900">Solicitar Nuevo Turno</h3>
            <p className="text-sm text-gray-600">Agenda una cita con tu médico</p>
          </button>
          <button className="p-4 border-2 border-green-200 rounded-lg hover:bg-green-50 transition-colors text-left">
            <CheckCircle className="h-8 w-8 text-green-600 mb-2" />
            <h3 className="font-semibold text-gray-900">Ver Mis Turnos</h3>
            <p className="text-sm text-gray-600">Revisa tus citas programadas</p>
          </button>
        </div>
      </div>

      {/* Próximos turnos */}
      <div className="bg-white rounded-lg shadow-md p-6 mt-6">
        <h2 className="text-xl font-bold text-gray-900 mb-4">Próximos Turnos</h2>
        <div className="space-y-4">
          <div className="flex items-center justify-between p-4 border border-gray-200 rounded-lg">
            <div className="flex items-center space-x-4">
              <div className="bg-blue-100 p-3 rounded-full">
                <Calendar className="h-6 w-6 text-blue-600" />
              </div>
              <div>
                <h3 className="font-semibold text-gray-900">Dr. García - Cardiología</h3>
                <p className="text-sm text-gray-600">15 de Enero, 2026 - 10:00 AM</p>
              </div>
            </div>
            <span className="px-3 py-1 bg-yellow-100 text-yellow-800 rounded-full text-sm font-medium">
              Pendiente
            </span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default PacienteDashboard;
