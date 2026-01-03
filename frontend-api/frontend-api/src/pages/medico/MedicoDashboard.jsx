import { useAuth } from '../../context/AuthContext';
import { Calendar, Users, Clock, CheckCircle } from 'lucide-react';

const MedicoDashboard = () => {
  const { user } = useAuth();

  const stats = [
    { 
      title: 'Turnos Hoy', 
      value: '8', 
      icon: Calendar, 
      color: 'bg-blue-500',
      textColor: 'text-blue-600'
    },
    { 
      title: 'Pacientes Atendidos', 
      value: '5', 
      icon: Users, 
      color: 'bg-green-500',
      textColor: 'text-green-600'
    },
    { 
      title: 'Próximo Turno', 
      value: '14:00', 
      icon: Clock, 
      color: 'bg-yellow-500',
      textColor: 'text-yellow-600'
    },
    { 
      title: 'Turnos Semana', 
      value: '42', 
      icon: CheckCircle, 
      color: 'bg-purple-500',
      textColor: 'text-purple-600'
    },
  ];

  return (
    <div>
      {/* Encabezado */}
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900">
          Bienvenido, Dr./Dra. {user?.nombre}!
        </h1>
        <p className="text-gray-600 mt-2">
          Panel de control médico
        </p>
      </div>

      {/* Tarjetas de estadísticas */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
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

      {/* Agenda del día */}
      <div className="bg-white rounded-lg shadow-md p-6">
        <h2 className="text-xl font-bold text-gray-900 mb-4">Agenda de Hoy</h2>
        <div className="space-y-4">
          <div className="flex items-center justify-between p-4 border-l-4 border-blue-600 bg-blue-50 rounded">
            <div>
              <h3 className="font-semibold text-gray-900">María González</h3>
              <p className="text-sm text-gray-600">10:00 AM - Consulta General</p>
            </div>
            <span className="px-3 py-1 bg-green-100 text-green-800 rounded-full text-sm font-medium">
              Confirmado
            </span>
          </div>
          <div className="flex items-center justify-between p-4 border-l-4 border-yellow-600 bg-yellow-50 rounded">
            <div>
              <h3 className="font-semibold text-gray-900">Carlos Pérez</h3>
              <p className="text-sm text-gray-600">11:30 AM - Control</p>
            </div>
            <span className="px-3 py-1 bg-yellow-100 text-yellow-800 rounded-full text-sm font-medium">
              Pendiente
            </span>
          </div>
          <div className="flex items-center justify-between p-4 border-l-4 border-green-600 bg-green-50 rounded">
            <div>
              <h3 className="font-semibold text-gray-900">Ana Martínez</h3>
              <p className="text-sm text-gray-600">14:00 PM - Consulta Especializada</p>
            </div>
            <span className="px-3 py-1 bg-blue-100 text-blue-800 rounded-full text-sm font-medium">
              Próximo
            </span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MedicoDashboard;
