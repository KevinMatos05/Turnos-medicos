import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { 
  Calendar, 
  Users, 
  UserCircle, 
  LogOut, 
  Home,
  Stethoscope,
  Bell,
  Settings
} from 'lucide-react';

const Navbar = () => {
  const { user, logout, isAdmin, isMedico, isPaciente } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const getNavLinks = () => {
    if (isAdmin) {
      return [
        { to: '/admin/dashboard', icon: Home, label: 'Dashboard' },
        { to: '/admin/medicos', icon: Stethoscope, label: 'Médicos' },
        { to: '/admin/pacientes', icon: Users, label: 'Pacientes' },
        { to: '/admin/especialidades', icon: Settings, label: 'Especialidades' },
      ];
    }
    if (isMedico) {
      return [
        { to: '/medico/dashboard', icon: Home, label: 'Dashboard' },
        { to: '/medico/agenda', icon: Calendar, label: 'Mi Agenda' },
        { to: '/medico/horarios', icon: Settings, label: 'Horarios' },
      ];
    }
    if (isPaciente) {
      return [
        { to: '/paciente/dashboard', icon: Home, label: 'Inicio' },
        { to: '/paciente/turnos', icon: Calendar, label: 'Mis Turnos' },
        { to: '/paciente/nuevo-turno', icon: Calendar, label: 'Solicitar Turno' },
      ];
    }
    return [];
  };

  return (
    <nav className="bg-blue-600 text-white shadow-lg">
      <div className="container mx-auto px-4">
        <div className="flex items-center justify-between h-16">
          {/* Logo */}
          <Link to="/" className="flex items-center space-x-2">
            <Calendar className="h-8 w-8" />
            <span className="text-xl font-bold">Turnos Médicos</span>
          </Link>

          {/* Navigation Links */}
          <div className="hidden md:flex items-center space-x-4">
            {getNavLinks().map((link) => (
              <Link
                key={link.to}
                to={link.to}
                className="flex items-center space-x-1 px-3 py-2 rounded-md hover:bg-blue-700 transition-colors"
              >
                <link.icon className="h-4 w-4" />
                <span>{link.label}</span>
              </Link>
            ))}
          </div>

          {/* User Menu */}
          <div className="flex items-center space-x-4">
            <button className="p-2 rounded-full hover:bg-blue-700 transition-colors">
              <Bell className="h-5 w-5" />
            </button>
            
            <div className="flex items-center space-x-2">
              <UserCircle className="h-6 w-6" />
              <span className="hidden md:inline text-sm">
                {user?.nombre} {user?.apellido}
              </span>
            </div>

            <button
              onClick={handleLogout}
              className="flex items-center space-x-1 px-3 py-2 rounded-md hover:bg-red-600 transition-colors"
            >
              <LogOut className="h-4 w-4" />
              <span className="hidden md:inline">Salir</span>
            </button>
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
