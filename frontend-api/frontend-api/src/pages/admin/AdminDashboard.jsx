import { useAuth } from '../../context/AuthContext';
import { Users, Stethoscope, Calendar, TrendingUp } from 'lucide-react';

const AdminDashboard = () => {
  const { user } = useAuth();

  const stats = [
    { 
      title: 'Total Pacientes', 
      value: '156', 
      icon: Users, 
      color: 'bg-blue-500',
      textColor: 'text-blue-600',
      change: '+12%'
    },
    { 
      title: 'Total Médicos', 
      value: '24', 
      icon: Stethoscope, 
      color: 'bg-green-500',
      textColor: 'text-green-600',
      change: '+3%'
    },
    { 
      title: 'Turnos Hoy', 
      value: '45', 
      icon: Calendar, 
      color: 'bg-yellow-500',
      textColor: 'text-yellow-600',
      change: '+8%'
    },
    { 
      title: 'Tasa de Ocupación', 
      value: '87%', 
      icon: TrendingUp, 
      color: 'bg-purple-500',
      textColor: 'text-purple-600',
      change: '+5%'
    },
  ];

  return (
    <div>
      {/* Encabezado */}
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900">
          Panel de Administración
        </h1>
        <p className="text-gray-600 mt-2">
          Bienvenido, {user?.nombre} - Vista general del sistema
        </p>
      </div>

      {/* Tarjetas de estadísticas */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        {stats.map((stat) => (
          <div key={stat.title} className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow">
            <div className="flex items-center justify-between mb-4">
              <div className={`${stat.color} p-3 rounded-full`}>
                <stat.icon className="h-6 w-6 text-white" />
              </div>
              <span className="text-sm font-medium text-green-600">{stat.change}</span>
            </div>
            <p className="text-sm text-gray-600 font-medium">{stat.title}</p>
            <p className={`text-3xl font-bold ${stat.textColor} mt-2`}>{stat.value}</p>
          </div>
        ))}
      </div>

      {/* Gráficos y actividad reciente */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
        {/* Actividad Reciente */}
        <div className="bg-white rounded-lg shadow-md p-6">
          <h2 className="text-xl font-bold text-gray-900 mb-4">Actividad Reciente</h2>
          <div className="space-y-4">
            <div className="flex items-center space-x-3 pb-3 border-b border-gray-100">
              <div className="bg-green-100 p-2 rounded-full">
                <Users className="h-4 w-4 text-green-600" />
              </div>
              <div className="flex-1">
                <p className="text-sm font-medium text-gray-900">Nuevo paciente registrado</p>
                <p className="text-xs text-gray-500">Hace 5 minutos</p>
              </div>
            </div>
            <div className="flex items-center space-x-3 pb-3 border-b border-gray-100">
              <div className="bg-blue-100 p-2 rounded-full">
                <Calendar className="h-4 w-4 text-blue-600" />
              </div>
              <div className="flex-1">
                <p className="text-sm font-medium text-gray-900">Turno confirmado</p>
                <p className="text-xs text-gray-500">Hace 15 minutos</p>
              </div>
            </div>
            <div className="flex items-center space-x-3 pb-3 border-b border-gray-100">
              <div className="bg-purple-100 p-2 rounded-full">
                <Stethoscope className="h-4 w-4 text-purple-600" />
              </div>
              <div className="flex-1">
                <p className="text-sm font-medium text-gray-900">Nuevo médico agregado</p>
                <p className="text-xs text-gray-500">Hace 1 hora</p>
              </div>
            </div>
          </div>
        </div>

        {/* Especialidades Más Solicitadas */}
        <div className="bg-white rounded-lg shadow-md p-6">
          <h2 className="text-xl font-bold text-gray-900 mb-4">Especialidades Más Solicitadas</h2>
          <div className="space-y-4">
            <div>
              <div className="flex items-center justify-between mb-2">
                <span className="text-sm font-medium text-gray-700">Cardiología</span>
                <span className="text-sm font-semibold text-gray-900">45 turnos</span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-2">
                <div className="bg-blue-600 h-2 rounded-full" style={{ width: '90%' }}></div>
              </div>
            </div>
            <div>
              <div className="flex items-center justify-between mb-2">
                <span className="text-sm font-medium text-gray-700">Traumatología</span>
                <span className="text-sm font-semibold text-gray-900">38 turnos</span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-2">
                <div className="bg-green-600 h-2 rounded-full" style={{ width: '76%' }}></div>
              </div>
            </div>
            <div>
              <div className="flex items-center justify-between mb-2">
                <span className="text-sm font-medium text-gray-700">Pediatría</span>
                <span className="text-sm font-semibold text-gray-900">32 turnos</span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-2">
                <div className="bg-yellow-600 h-2 rounded-full" style={{ width: '64%' }}></div>
              </div>
            </div>
            <div>
              <div className="flex items-center justify-between mb-2">
                <span className="text-sm font-medium text-gray-700">Dermatología</span>
                <span className="text-sm font-semibold text-gray-900">25 turnos</span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-2">
                <div className="bg-purple-600 h-2 rounded-full" style={{ width: '50%' }}></div>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Acciones rápidas */}
      <div className="bg-white rounded-lg shadow-md p-6">
        <h2 className="text-xl font-bold text-gray-900 mb-4">Acciones Rápidas</h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <button className="p-4 border-2 border-blue-200 rounded-lg hover:bg-blue-50 transition-colors text-left">
            <Users className="h-8 w-8 text-blue-600 mb-2" />
            <h3 className="font-semibold text-gray-900">Gestionar Pacientes</h3>
            <p className="text-sm text-gray-600">Ver y administrar pacientes</p>
          </button>
          <button className="p-4 border-2 border-green-200 rounded-lg hover:bg-green-50 transition-colors text-left">
            <Stethoscope className="h-8 w-8 text-green-600 mb-2" />
            <h3 className="font-semibold text-gray-900">Gestionar Médicos</h3>
            <p className="text-sm text-gray-600">Ver y administrar médicos</p>
          </button>
          <button className="p-4 border-2 border-purple-200 rounded-lg hover:bg-purple-50 transition-colors text-left">
            <Calendar className="h-8 w-8 text-purple-600 mb-2" />
            <h3 className="font-semibold text-gray-900">Ver Todos los Turnos</h3>
            <p className="text-sm text-gray-600">Gestionar agenda completa</p>
          </button>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;
