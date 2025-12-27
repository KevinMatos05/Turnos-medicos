# 🧪 Guía Completa de Pruebas - Sistema de Turnos Médicos

## 📋 Índice
1. [Preparación del Entorno](#preparación-del-entorno)
2. [Autenticación](#autenticación)
3. [Pruebas por Rol](#pruebas-por-rol)
4. [Colección Completa de Endpoints](#colección-completa-de-endpoints)
5. [Preparación para Frontend](#preparación-para-frontend)

---

## 🚀 Preparación del Entorno

### Paso 1: Verificar Docker y PostgreSQL
```powershell
# Verificar que Docker esté corriendo
docker ps

# Si no está corriendo PostgreSQL, iniciar el contenedor
docker-compose up -d
```

### Paso 2: Iniciar la Aplicación
```powershell
# Desde la carpeta backend-api
mvn spring-boot:run
```

**Esperar a ver este mensaje:**
```
Started BackendApiApplication in X.XXX seconds
```

### Paso 3: Acceder a Swagger UI
Abrir navegador en: `http://localhost:8080/swagger-ui.html`

---

## 🔐 Autenticación

### Usuarios Precargados (desde DataLoader)

| Email | Contraseña | Rol |
|-------|-----------|-----|
| admin@turnosmedicos.com | admin123 | ADMIN |
| paciente@test.com | paciente123 | PACIENTE |
| medico@test.com | medico123 | MEDICO |

### Obtener Token JWT

**Endpoint:** `POST http://localhost:8080/api/auth/login`

**Body (JSON):**
```json
{
  "email": "paciente@test.com",
  "password": "paciente123"
}
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwYWNpZW50...",
  "tipo": "Bearer",
  "email": "paciente@test.com",
  "rol": "PACIENTE",
  "nombre": "Juan",
  "apellido": "Pérez"
}
```

**⚠️ IMPORTANTE:** Copiar el token y usarlo en todas las peticiones con el header:
```
Authorization: Bearer {token_aqui}
```

---

## 🧑‍⚕️ PRUEBAS POR ROL

## 🔵 ROL: PACIENTE

### 1. Registrarse como Nuevo Paciente
```http
POST http://localhost:8080/api/auth/register/paciente
Content-Type: application/json

{
  "email": "maria.garcia@gmail.com",
  "password": "maria123",
  "nombre": "María",
  "apellido": "García",
  "documento": "87654321",
  "telefono": "1156789012",
  "direccion": "Calle Falsa 456",
  "obraSocial": "OSDE"
}
```

### 2. Ver Médicos Disponibles
```http
GET http://localhost:8080/api/medicos
Authorization: Bearer {tu_token}
```

### 3. Filtrar Médicos por Especialidad
```http
GET http://localhost:8080/api/medicos?especialidad=1
Authorization: Bearer {tu_token}
```

### 4. Ver Disponibilidad de un Médico
```http
GET http://localhost:8080/api/agenda/medico/1?fechaInicio=2025-01-15&fechaFin=2025-01-20
Authorization: Bearer {tu_token}
```

**Respuesta esperada:**
```json
[
  {
    "medicoId": 1,
    "fecha": "2025-01-15",
    "horaInicio": "09:00",
    "horaFin": "09:30",
    "disponible": true
  },
  {
    "medicoId": 1,
    "fecha": "2025-01-15",
    "horaInicio": "09:30",
    "horaFin": "10:00",
    "disponible": true
  }
]
```

### 5. Crear un Turno
```http
POST http://localhost:8080/api/turnos
Authorization: Bearer {tu_token}
Content-Type: application/json

{
  "medicoId": 1,
  "pacienteId": 1,
  "fecha": "2025-01-15",
  "horaInicio": "09:00",
  "tipoConsulta": "PRIMERA_VEZ",
  "motivoConsulta": "Consulta de control anual"
}
```

### 6. Ver Mis Turnos
```http
GET http://localhost:8080/api/turnos/mis-turnos
Authorization: Bearer {tu_token}
```

### 7. Ver Historial Completo (como Paciente)
```http
GET http://localhost:8080/api/pacientes/1/turnos
Authorization: Bearer {tu_token}
```

### 8. Cancelar un Turno (con más de 24hs de anticipación)
```http
DELETE http://localhost:8080/api/turnos/1/paciente
Authorization: Bearer {tu_token}
Content-Type: application/json

{
  "motivoCancelacion": "Tengo un compromiso ineludible"
}
```

**⚠️ NOTA:** Si faltan menos de 24 horas, recibirás un error:
```json
{
  "mensaje": "No se puede cancelar el turno con menos de 24 horas de anticipación",
  "detalles": "Turno programado para: 2025-01-15 09:00"
}
```

---

## 🟢 ROL: MEDICO

### 1. Login como Médico
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "medico@test.com",
  "password": "medico123"
}
```

### 2. Ver Mis Turnos Asignados
```http
GET http://localhost:8080/api/turnos/mis-turnos
Authorization: Bearer {token_medico}
```

### 3. Ver Mi Agenda de un Día Específico
```http
GET http://localhost:8080/api/turnos/agenda/medico?fecha=2025-01-15
Authorization: Bearer {token_medico}
```

### 4. Confirmar un Turno
```http
PUT http://localhost:8080/api/turnos/1/confirmar
Authorization: Bearer {token_medico}
```

**Cambio de estado:** `PENDIENTE` → `CONFIRMADO`

### 5. Marcar Asistencia (con observaciones médicas)
```http
PUT http://localhost:8080/api/turnos/1/marcar-asistencia
Authorization: Bearer {token_medico}
Content-Type: application/json

{
  "observaciones": "Paciente presenta síntomas leves. Se recomienda reposo y seguimiento en 7 días."
}
```

**Cambio de estado:** `CONFIRMADO` → `ASISTIDO`

### 6. Marcar Inasistencia
```http
PUT http://localhost:8080/api/turnos/2/marcar-inasistencia
Authorization: Bearer {token_medico}
```

**Cambio de estado:** `CONFIRMADO` → `NO_ASISTIDO`

### 7. Cancelar un Turno (sin restricción de 24hs)
```http
DELETE http://localhost:8080/api/turnos/3/medico
Authorization: Bearer {token_medico}
Content-Type: application/json

{
  "motivoCancelacion": "Emergencia médica personal"
}
```

### 8. Bloquear un Día (vacaciones, capacitación, etc.)
```http
POST http://localhost:8080/api/medicos/1/bloquear-dia
Authorization: Bearer {token_medico}
Content-Type: application/json

{
  "medicoId": 1,
  "fecha": "2025-02-10",
  "motivo": "Vacaciones",
  "activo": true
}
```

### 9. Ver Mis Días Bloqueados
```http
GET http://localhost:8080/api/medicos/1/dias-bloqueados
Authorization: Bearer {token_medico}
```

### 10. Desbloquear un Día
```http
DELETE http://localhost:8080/api/medicos/bloqueos/1
Authorization: Bearer {token_medico}
```

---

## 🔴 ROL: ADMIN

### 1. Login como Admin
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "admin@turnosmedicos.com",
  "password": "admin123"
}
```

### 2. Ver Todos los Médicos
```http
GET http://localhost:8080/api/admin/medicos
Authorization: Bearer {token_admin}
```

### 3. Crear Nuevo Médico
```http
POST http://localhost:8080/api/admin/medicos
Authorization: Bearer {token_admin}
Content-Type: application/json

{
  "email": "dr.martin@hospital.com",
  "password": "martin123",
  "nombre": "Carlos",
  "apellido": "Martín",
  "dni": "23456789",
  "telefono": "1145678901",
  "matricula": "MP-9876",
  "especialidadId": 2,
  "sucursalId": 1
}
```

### 4. Actualizar Información de un Médico
```http
PUT http://localhost:8080/api/admin/medicos/1
Authorization: Bearer {token_admin}
Content-Type: application/json

{
  "nombre": "Juan Carlos",
  "apellido": "López",
  "telefono": "1198765432",
  "especialidadId": 2,
  "sucursalId": 1
}
```

### 5. Eliminar un Médico (soft delete)
```http
DELETE http://localhost:8080/api/admin/medicos/1
Authorization: Bearer {token_admin}
```

### 6. Ver Todas las Especialidades
```http
GET http://localhost:8080/api/admin/especialidades
Authorization: Bearer {token_admin}
```

### 7. Crear Nueva Especialidad
```http
POST http://localhost:8080/api/admin/especialidades
Authorization: Bearer {token_admin}
Content-Type: application/json

{
  "nombre": "Dermatología",
  "descripcion": "Especialidad enfocada en enfermedades de la piel",
  "activo": true
}
```

### 8. Actualizar Especialidad
```http
PUT http://localhost:8080/api/admin/especialidades/1
Authorization: Bearer {token_admin}
Content-Type: application/json

{
  "nombre": "Cardiología Clínica",
  "descripcion": "Especialidad actualizada",
  "activo": true
}
```

### 9. Desactivar Especialidad
```http
DELETE http://localhost:8080/api/admin/especialidades/1
Authorization: Bearer {token_admin}
```

### 10. Ver Todas las Sucursales
```http
GET http://localhost:8080/api/admin/sucursales
Authorization: Bearer {token_admin}
```

### 11. Crear Nueva Sucursal
```http
POST http://localhost:8080/api/admin/sucursales
Authorization: Bearer {token_admin}
Content-Type: application/json

{
  "nombre": "Sucursal Palermo",
  "direccion": "Av. Santa Fe 3500",
  "telefono": "1147896523",
  "email": "palermo@turnosmedicos.com",
  "ciudad": "Buenos Aires",
  "activo": true
}
```

### 12. Actualizar Sucursal
```http
PUT http://localhost:8080/api/admin/sucursales/1
Authorization: Bearer {token_admin}
Content-Type: application/json

{
  "nombre": "Sucursal Centro - CABA",
  "direccion": "Av. Corrientes 1234",
  "telefono": "1145678910",
  "ciudad": "CABA",
  "activo": true
}
```

### 13. Desactivar Sucursal
```http
DELETE http://localhost:8080/api/admin/sucursales/1
Authorization: Bearer {token_admin}
```

---

## 📊 COLECCIÓN COMPLETA DE ENDPOINTS

### Autenticación (Sin token)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/auth/register/paciente` | Registrar nuevo paciente |
| POST | `/api/auth/register/medico` | Registrar nuevo médico |
| POST | `/api/auth/login` | Iniciar sesión |

### Turnos (Requiere autenticación)
| Método | Endpoint | Descripción | Rol |
|--------|----------|-------------|-----|
| GET | `/api/turnos/mis-turnos` | Ver mis turnos | Todos |
| GET | `/api/turnos/agenda/medico?fecha={fecha}` | Ver agenda del día | MEDICO |
| GET | `/api/turnos/{id}` | Ver detalle de un turno | Todos |
| POST | `/api/turnos` | Crear nuevo turno | PACIENTE |
| PUT | `/api/turnos/{id}/confirmar` | Confirmar turno | MEDICO |
| PUT | `/api/turnos/{id}/marcar-asistencia` | Marcar asistencia | MEDICO |
| PUT | `/api/turnos/{id}/marcar-inasistencia` | Marcar inasistencia | MEDICO |
| DELETE | `/api/turnos/{id}/paciente` | Cancelar turno (24hs) | PACIENTE |
| DELETE | `/api/turnos/{id}/medico` | Cancelar turno (sin límite) | MEDICO |

### Médicos
| Método | Endpoint | Descripción | Rol |
|--------|----------|-------------|-----|
| GET | `/api/medicos` | Listar médicos | Todos |
| GET | `/api/medicos?especialidad={id}` | Filtrar por especialidad | Todos |
| GET | `/api/medicos/{id}` | Ver detalle médico | Todos |
| POST | `/api/medicos/{id}/bloquear-dia` | Bloquear fecha | MEDICO |
| GET | `/api/medicos/{id}/dias-bloqueados` | Ver días bloqueados | MEDICO |
| DELETE | `/api/medicos/bloqueos/{id}` | Desbloquear día | MEDICO |

### Pacientes
| Método | Endpoint | Descripción | Rol |
|--------|----------|-------------|-----|
| GET | `/api/pacientes` | Listar pacientes | ADMIN/MEDICO |
| GET | `/api/pacientes/{id}` | Ver detalle paciente | ADMIN/MEDICO |
| GET | `/api/pacientes/{id}/turnos` | Ver historial turnos | Todos |
| PUT | `/api/pacientes/{id}` | Actualizar paciente | PACIENTE/ADMIN |
| DELETE | `/api/pacientes/{id}` | Eliminar paciente | ADMIN |

### Agenda (Pública)
| Método | Endpoint | Descripción | Rol |
|--------|----------|-------------|-----|
| GET | `/api/agenda/medico/{id}?fechaInicio={fecha}&fechaFin={fecha}` | Ver disponibilidad | Todos |

### Admin (Solo ADMIN)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/admin/medicos` | Listar todos los médicos |
| POST | `/api/admin/medicos` | Crear médico |
| PUT | `/api/admin/medicos/{id}` | Actualizar médico |
| DELETE | `/api/admin/medicos/{id}` | Eliminar médico |
| GET | `/api/admin/especialidades` | Listar especialidades |
| POST | `/api/admin/especialidades` | Crear especialidad |
| PUT | `/api/admin/especialidades/{id}` | Actualizar especialidad |
| DELETE | `/api/admin/especialidades/{id}` | Desactivar especialidad |
| GET | `/api/admin/sucursales` | Listar sucursales |
| POST | `/api/admin/sucursales` | Crear sucursal |
| PUT | `/api/admin/sucursales/{id}` | Actualizar sucursal |
| DELETE | `/api/admin/sucursales/{id}` | Desactivar sucursal |

**Total:** 36 Endpoints

---

## 🧪 Secuencia de Prueba Completa

### Escenario: Flujo Completo de un Turno

```
1. [PACIENTE] Registro
   POST /api/auth/register/paciente

2. [PACIENTE] Login
   POST /api/auth/login

3. [PACIENTE] Ver especialidades disponibles
   GET /api/medicos

4. [PACIENTE] Ver médicos de cardiología
   GET /api/medicos?especialidad=1

5. [PACIENTE] Ver disponibilidad del Dr. López
   GET /api/agenda/medico/1?fechaInicio=2025-01-15&fechaFin=2025-01-20

6. [PACIENTE] Reservar turno
   POST /api/turnos

7. [MEDICO] Login
   POST /api/auth/login

8. [MEDICO] Ver mis turnos del día
   GET /api/turnos/agenda/medico?fecha=2025-01-15

9. [MEDICO] Confirmar el turno
   PUT /api/turnos/1/confirmar

10. [MEDICO] Marcar asistencia después de la consulta
    PUT /api/turnos/1/marcar-asistencia

11. [PACIENTE] Ver mi historial
    GET /api/pacientes/1/turnos

12. [ADMIN] Ver estadísticas
    GET /api/admin/medicos
```

---

## 🔧 Herramientas Recomendadas

### Opción 1: Postman
1. Descargar: https://www.postman.com/downloads/
2. Importar colección desde Swagger
3. Configurar variable de entorno para el token

### Opción 2: Thunder Client (VSCode Extension)
1. Instalar extensión en VSCode
2. Crear requests directamente en el editor

### Opción 3: Swagger UI (Recomendado para principiantes)
1. Abrir: http://localhost:8080/swagger-ui.html
2. Hacer clic en "Authorize"
3. Pegar el token: `Bearer {tu_token}`
4. Probar endpoints directamente

### Opción 4: cURL (Línea de comandos)
```bash
# Ejemplo completo
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"paciente@test.com","password":"paciente123"}'
```

---

## ✅ Checklist de Funcionalidades

### Autenticación y Seguridad
- [ ] Registro de pacientes
- [ ] Registro de médicos
- [ ] Login con JWT
- [ ] Validación de roles (ADMIN, MEDICO, PACIENTE)
- [ ] Token expira en 24 horas

### Gestión de Turnos
- [ ] Crear turno
- [ ] Ver mis turnos
- [ ] Ver detalle de turno
- [ ] Confirmar turno (MEDICO)
- [ ] Marcar asistencia con observaciones (MEDICO)
- [ ] Marcar inasistencia (MEDICO)
- [ ] Cancelar turno con 24hs anticipación (PACIENTE)
- [ ] Cancelar turno sin restricción (MEDICO)
- [ ] Ver agenda del día (MEDICO)

### Disponibilidad
- [ ] Ver disponibilidad por médico
- [ ] Ver disponibilidad por rango de fechas
- [ ] Filtrar médicos por especialidad
- [ ] Bloquear días (vacaciones, capacitación)
- [ ] Ver días bloqueados
- [ ] Desbloquear días

### Panel de Administración
- [ ] CRUD completo de médicos
- [ ] CRUD completo de especialidades
- [ ] CRUD completo de sucursales
- [ ] Ver historial de pacientes

### Notificaciones (Automáticas)
- [ ] Recordatorio 24hs antes del turno (scheduler 9:00 AM)
- [ ] Notificación al crear turno
- [ ] Notificación al confirmar turno
- [ ] Notificación al cancelar turno

---

## 🎨 PREPARACIÓN PARA FRONTEND

### Tecnologías Recomendadas

#### Opción 1: React + Vite (Recomendado)
```bash
npm create vite@latest frontend-turnos -- --template react
cd frontend-turnos
npm install axios react-router-dom @mui/material @emotion/react @emotion/styled
npm install date-fns react-hook-form yup
```

#### Opción 2: Next.js (Si necesitas SSR)
```bash
npx create-next-app@latest frontend-turnos
cd frontend-turnos
npm install axios @mui/material @emotion/react @emotion/styled
```

#### Opción 3: Angular (Si prefieres TypeScript fuerte)
```bash
ng new frontend-turnos
cd frontend-turnos
npm install @angular/material axios
```

### Estructura Recomendada (React)

```
frontend-turnos/
├── src/
│   ├── api/
│   │   ├── axios.config.js          # Configuración de Axios
│   │   ├── auth.api.js              # Llamadas de autenticación
│   │   ├── turnos.api.js            # Llamadas de turnos
│   │   ├── medicos.api.js           # Llamadas de médicos
│   │   └── admin.api.js             # Llamadas de admin
│   ├── components/
│   │   ├── auth/
│   │   │   ├── Login.jsx
│   │   │   ├── Register.jsx
│   │   │   └── PrivateRoute.jsx
│   │   ├── paciente/
│   │   │   ├── BuscarMedicos.jsx
│   │   │   ├── VerDisponibilidad.jsx
│   │   │   ├── CrearTurno.jsx
│   │   │   └── MisTurnos.jsx
│   │   ├── medico/
│   │   │   ├── AgendaDia.jsx
│   │   │   ├── ConfirmarTurno.jsx
│   │   │   ├── MarcarAsistencia.jsx
│   │   │   └── BloquearDias.jsx
│   │   └── admin/
│   │       ├── GestionMedicos.jsx
│   │       ├── GestionEspecialidades.jsx
│   │       └── GestionSucursales.jsx
│   ├── context/
│   │   └── AuthContext.jsx          # Contexto de autenticación
│   ├── hooks/
│   │   ├── useAuth.js               # Hook personalizado de auth
│   │   └── useTurnos.js             # Hook personalizado de turnos
│   ├── pages/
│   │   ├── HomePage.jsx
│   │   ├── PacienteDashboard.jsx
│   │   ├── MedicoDashboard.jsx
│   │   └── AdminDashboard.jsx
│   ├── utils/
│   │   ├── formatters.js            # Funciones de formato
│   │   └── validators.js            # Validaciones
│   ├── App.jsx
│   └── main.jsx
├── .env
└── package.json
```

### Configuración Base de Axios

```javascript
// src/api/axios.config.js
import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const axiosInstance = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

// Interceptor para agregar el token en cada request
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Interceptor para manejar errores de autenticación
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;
```

### Ejemplo de AuthContext

```javascript
// src/context/AuthContext.jsx
import { createContext, useState, useEffect } from 'react';
import { jwtDecode } from 'jwt-decode';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        const decoded = jwtDecode(token);
        setUser(decoded);
      } catch (error) {
        localStorage.removeItem('token');
      }
    }
    setLoading(false);
  }, []);

  const login = (token, userData) => {
    localStorage.setItem('token', token);
    setUser(userData);
  };

  const logout = () => {
    localStorage.removeItem('token');
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, loading }}>
      {children}
    </AuthContext.Provider>
  );
};
```

### Páginas Principales a Crear

1. **Landing Page**
   - Descripción del sistema
   - Botones de Login/Registro
   - Información de contacto

2. **Dashboard de Paciente**
   - Buscar médicos por especialidad
   - Ver disponibilidad
   - Agendar turnos
   - Ver mis turnos (próximos e históricos)
   - Cancelar turnos

3. **Dashboard de Médico**
   - Ver agenda del día
   - Confirmar turnos
   - Marcar asistencia/inasistencia
   - Bloquear/desbloquear días
   - Ver historial de pacientes

4. **Dashboard de Admin**
   - Gestión de médicos (ABM)
   - Gestión de especialidades (ABM)
   - Gestión de sucursales (ABM)
   - Estadísticas generales

### Librerías Útiles

```json
{
  "dependencies": {
    "axios": "^1.6.2",                    // HTTP client
    "react-router-dom": "^6.20.1",        // Routing
    "@mui/material": "^5.15.0",           // UI Components
    "@emotion/react": "^11.11.1",         // Styling
    "@emotion/styled": "^11.11.0",        // Styling
    "date-fns": "^3.0.6",                 // Manejo de fechas
    "react-hook-form": "^7.49.2",         // Forms
    "yup": "^1.3.3",                      // Validación
    "jwt-decode": "^4.0.0",               // Decodificar JWT
    "react-toastify": "^9.1.3",           // Notificaciones
    "react-query": "^3.39.3"              // Cache y estado server
  }
}
```

### Variables de Entorno (.env)

```bash
VITE_API_URL=http://localhost:8080/api
VITE_APP_NAME=Sistema de Turnos Médicos
```

### Próximos Pasos

1. ✅ **Backend completado y probado**
2. ⏭️ **Crear proyecto frontend**
3. ⏭️ **Configurar routing y autenticación**
4. ⏭️ **Desarrollar componentes por rol**
5. ⏭️ **Integrar con el backend**
6. ⏭️ **Testing y deployment**

---

## 📝 Notas Importantes

### CORS
El backend ya tiene configurado CORS para `http://localhost:5173` (Vite) y `http://localhost:3000` (React/Next.js).

### Formato de Fechas
- Backend espera: `yyyy-MM-dd` (ej: "2025-01-15")
- Backend espera horas: `HH:mm` (ej: "09:00")
- Usar `date-fns` para formatear correctamente

### Validaciones del Backend
- **Turnos:** Solo se pueden crear si hay disponibilidad
- **Cancelación:** Pacientes deben cancelar con 24hs de anticipación
- **Horarios:** Se valida que estén dentro de los horarios laborales del médico
- **Días bloqueados:** No se puede agendar en días bloqueados por el médico

### Estados de Turno
- `PENDIENTE` → Recién creado
- `CONFIRMADO` → Médico confirmó
- `ASISTIDO` → Paciente asistió
- `NO_ASISTIDO` → Paciente no asistió
- `CANCELADO` → Turno cancelado

---

## 🆘 Soporte

Si encuentras algún error o tienes dudas:
1. Revisar logs del backend
2. Verificar que el token JWT sea válido
3. Confirmar que los datos del request sean correctos
4. Verificar que Docker/PostgreSQL estén corriendo

---

**¡Sistema listo para producción! 🚀**

**Documentado por:** GitHub Copilot
**Fecha:** 27 de diciembre de 2025
