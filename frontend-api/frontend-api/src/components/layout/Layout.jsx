import { Outlet } from 'react-router-dom';
import Navbar from './Navbar';

const Layout = () => {
  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      <Navbar />
      <main className="container mx-auto px-4 py-8 flex-1">
        <Outlet />
      </main>
      <footer className="bg-gray-900 text-white py-4">
        <div className="container mx-auto px-4 text-center">
          <p>&copy; 2026 Turnos Médicos. Todos los derechos reservados.</p>
        </div>
      </footer>
    </div>
  );
};

export default Layout;
