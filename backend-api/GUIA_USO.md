# 🔥 GUÍA RÁPIDA DE USO - API Turnos Médicos

## 🚀 Quick Start

### 1. Iniciar el Sistema

```bash
# 1. Levantar PostgreSQL con Docker
docker-compose up -d

# 2. Ejecutar la aplicación
./mvnw spring-boot:run

# 3. Acceder a Swagger UI
http://localhost:8080/swagger-ui.html
```

---

## 📝 Flujo Completo de Uso

### PASO 1: Registrar Usuarios

#### Registrar Paciente
```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "email": "juan.perez@example.com",
  "password": "password123",
  "nombre": "Juan",
  "apellido": "Pérez",
  "telefono": "+541112345678",
  "rol": "PACIENTE",
  "documento": "35123456",
  "direccion": "Av. Corrientes 1234, CABA",
  "obraSocial": "OSDE",
  "numeroAfiliado": "12-35123456-7"
}
```

#### Registrar Médico
```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "email": "dra.martinez@hospital.com",
  "password": "medico123",
  "nombre": "María",
  "apellido": "Martínez",
  "telefono": "+541198765432",
  "rol": "MEDICO",
  "matricula": "MN-12345",
  "especialidadId": 1,
  "sucursalId": 1
}
```

#### Registrar Administrador
```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "email": "admin@turnos.com",
  "password": "admin123",
  "nombre": "Carlos",
  "apellido": "Admin",
  "rol": "ADMIN"
}
```

---

### PASO 2: Login y Obtener Token

```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "juan.perez@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqdWFuLnBlcmV6QGV4YW1wbGUuY29tIiwiaWF0IjoxNjQwOTk1MjAwLCJleHAiOjE2NDA5OTg4MDB9.abc123",
  "tipo": "Bearer",
  "id": 1,
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan.perez@example.com",
  "rol": "PACIENTE"
}
```

> 💡 **Importante:** Guarda el `token` para usarlo en todas las siguientes peticiones.

---

### PASO 3: Configurar Horarios del Médico (MÉDICO)

```http
POST http://localhost:8080/api/horarios-laborales
Authorization: Bearer {tu-token-medico}
Content-Type: application/json

{
  "medicoId": 2,
  "diaSemana": "MONDAY",
  "horaInicio": "09:00",
  "horaFin": "17:00"
}
```

Repetir para cada día:
- MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY

---

### PASO 4: Bloquear Días (Vacaciones, Feriados)

```http
POST http://localhost:8080/api/medicos/2/bloquear-dia
Authorization: Bearer {tu-token-medico}
Content-Type: application/json

{
  "fecha": "2025-02-14",
  "motivo": "Día de San Valentín - Feriado"
}
```

#### Ver días bloqueados:
```http
GET http://localhost:8080/api/medicos/2/dias-bloqueados
Authorization: Bearer {tu-token}
```

---

### PASO 5: Consultar Disponibilidad (PACIENTE)

#### Por día único:
```http
GET http://localhost:8080/api/agenda/medico/2?fecha=2025-01-20
Authorization: Bearer {tu-token-paciente}
```

#### Por rango de fechas:
```http
GET http://localhost:8080/api/agenda/medico/2?fecha=2025-01-20&fechaFin=2025-01-27
Authorization: Bearer {tu-token-paciente}
```

**Response Example:**
```json
[
  {
    "medicoId": 2,
    "medicoNombre": "Dra. María Martínez",
    "fecha": "2025-01-20",
    "horaInicio": "09:00",
    "horaFin": "09:30",
    "disponible": true
  },
  {
    "medicoId": 2,
    "medicoNombre": "Dra. María Martínez",
    "fecha": "2025-01-20",
    "horaInicio": "09:30",
    "horaFin": "10:00",
    "disponible": false
  }
]
```

---

### PASO 6: Crear Turno (PACIENTE)

```http
POST http://localhost:8080/api/turnos
Authorization: Bearer {tu-token-paciente}
Content-Type: application/json

{
  "pacienteId": 1,
  "medicoId": 2,
  "fechaHora": "2025-01-20T09:00:00",
  "tipoConsulta": "PRIMERA_VEZ",
  "sucursalId": 1
}
```

**Response:**
```json
{
  "id": 1,
  "paciente": {
    "id": 1,
    "nombreCompleto": "Juan Pérez"
  },
  "medico": {
    "id": 2,
    "nombreCompleto": "Dra. María Martínez"
  },
  "fechaHora": "2025-01-20T09:00:00",
  "estado": "PENDIENTE",
  "tipoConsulta": "PRIMERA_VEZ"
}
```

---

### PASO 7: Ver Historial de Turnos (PACIENTE)

```http
GET http://localhost:8080/api/pacientes/1/turnos
Authorization: Bearer {tu-token-paciente}
```

**Response:**
```json
[
  {
    "id": 1,
    "medicoNombre": "Dra. María Martínez",
    "fechaHora": "2025-01-20T09:00:00",
    "estado": "PENDIENTE",
    "especialidad": "Cardiología"
  },
  {
    "id": 2,
    "medicoNombre": "Dr. López",
    "fechaHora": "2024-12-15T14:00:00",
    "estado": "ASISTIDO",
    "especialidad": "Traumatología"
  }
]
```

---

### PASO 8: Confirmar Turno (MÉDICO/ADMIN)

```http
PUT http://localhost:8080/api/turnos/1/confirmar
Authorization: Bearer {tu-token-medico}
```

---

### PASO 9: Marcar Asistencia (MÉDICO)

#### Con observaciones:
```http
PUT http://localhost:8080/api/turnos/1/marcar-asistencia
Authorization: Bearer {tu-token-medico}
Content-Type: application/json

"Paciente presenta mejoría. Continuar con tratamiento indicado."
```

#### Sin observaciones:
```http
PUT http://localhost:8080/api/turnos/1/marcar-asistencia
Authorization: Bearer {tu-token-medico}
```

---

### PASO 10: Marcar Inasistencia (MÉDICO)

```http
PUT http://localhost:8080/api/turnos/1/marcar-inasistencia
Authorization: Bearer {tu-token-medico}
```

---

### PASO 11: Cancelar Turno (PACIENTE)

> ⚠️ **Importante:** Solo se puede cancelar con 24 horas de anticipación.

```http
DELETE http://localhost:8080/api/turnos/1
Authorization: Bearer {tu-token-paciente}
```

**Error si es menos de 24hs:**
```json
{
  "mensaje": "No se puede cancelar con menos de 24 horas de anticipación. Faltan 18 horas.",
  "timestamp": "2025-01-19T15:00:00"
}
```

---

## 👨‍💼 Funciones de Administrador

### PASO 12: Crear Especialidad (ADMIN)

```http
POST http://localhost:8080/api/admin/especialidades?nombre=Dermatología&descripcion=Especialidad%20en%20piel
Authorization: Bearer {tu-token-admin}
```

### PASO 13: Crear Sucursal (ADMIN)

```http
POST http://localhost:8080/api/admin/sucursales
Authorization: Bearer {tu-token-admin}
Content-Type: application/json

{
  "nombre": "Clínica Central",
  "direccion": "Av. Libertador 5432, CABA",
  "telefono": "+541143211234",
  "email": "info@clinicacentral.com",
  "activo": true
}
```

### PASO 14: Listar Todos los Médicos (ADMIN)

```http
GET http://localhost:8080/api/admin/medicos
Authorization: Bearer {tu-token-admin}
```

### PASO 15: Actualizar Especialidad (ADMIN)

```http
PUT http://localhost:8080/api/admin/especialidades/1
Authorization: Bearer {tu-token-admin}
Content-Type: application/json

{
  "nombre": "Cardiología Clínica",
  "descripcion": "Especialidad actualizada",
  "activo": true
}
```

---

## 🔍 Búsquedas y Filtros

### Buscar Médicos por Especialidad

```http
GET http://localhost:8080/api/medicos?especialidad=1
Authorization: Bearer {tu-token}
```

### Listar Todas las Especialidades

```http
GET http://localhost:8080/api/especialidades
```

**Response:**
```json
[
  {
    "id": 1,
    "nombre": "Cardiología",
    "descripcion": "Especialidad del corazón"
  },
  {
    "id": 2,
    "nombre": "Dermatología",
    "descripcion": "Especialidad de la piel"
  }
]
```

---

## 🛠️ Testing con cURL

### Registro de Paciente
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "test123",
    "nombre": "Test",
    "apellido": "Usuario",
    "telefono": "+541112345678",
    "rol": "PACIENTE",
    "documento": "12345678",
    "direccion": "Calle Falsa 123",
    "obraSocial": "OSDE",
    "numeroAfiliado": "123456"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "test123"
  }'
```

### Consultar Disponibilidad
```bash
curl -X GET "http://localhost:8080/api/agenda/medico/1?fecha=2025-01-20" \
  -H "Authorization: Bearer {tu-token}"
```

---

## 📊 Casos de Uso Completos

### Caso 1: Paciente Reserva Turno

1. **Login** → Obtener token
2. **Buscar médicos** por especialidad
3. **Consultar disponibilidad** del médico elegido
4. **Crear turno** en horario disponible
5. **Ver historial** para confirmar

### Caso 2: Médico Gestiona su Agenda

1. **Login** → Obtener token
2. **Configurar horarios** laborales
3. **Bloquear días** de vacaciones
4. **Ver turnos** asignados
5. **Marcar asistencia** después de la consulta

### Caso 3: Admin Gestiona el Sistema

1. **Login** → Obtener token admin
2. **Crear especialidades** nuevas
3. **Crear sucursales**
4. **Registrar médicos**
5. **Ver reportes** del sistema

---

## ⚠️ Errores Comunes

### 1. Token Expirado
```json
{
  "mensaje": "Token JWT expirado",
  "timestamp": "2025-01-20T10:00:00"
}
```
**Solución:** Hacer login nuevamente.

### 2. Cancelación Tardía
```json
{
  "mensaje": "No se puede cancelar con menos de 24 horas de anticipación. Faltan 12 horas."
}
```
**Solución:** Contactar al médico directamente.

### 3. Horario No Disponible
```json
{
  "mensaje": "El horario seleccionado no está disponible"
}
```
**Solución:** Consultar disponibilidad actualizada.

### 4. Sin Autorización
```json
{
  "mensaje": "Acceso denegado",
  "timestamp": "2025-01-20T10:00:00"
}
```
**Solución:** Verificar rol y permisos del usuario.

---

## 🎯 Tips y Mejores Prácticas

1. **Siempre incluye el token** en el header `Authorization: Bearer {token}`
2. **Consulta disponibilidad** antes de crear turno
3. **Confirma turnos** lo antes posible
4. **Cancela con anticipación** (24hs mínimo)
5. **Usa Swagger UI** para explorar la API: `http://localhost:8080/swagger-ui.html`

---

## 📱 Integración con Frontend

### Headers requeridos:
```javascript
const headers = {
  'Content-Type': 'application/json',
  'Authorization': `Bearer ${token}`
};
```

### Ejemplo con Fetch API:
```javascript
// Login
const login = async (email, password) => {
  const response = await fetch('http://localhost:8080/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password })
  });
  const data = await response.json();
  localStorage.setItem('token', data.token);
  return data;
};

// Consultar disponibilidad
const getDisponibilidad = async (medicoId, fecha) => {
  const token = localStorage.getItem('token');
  const response = await fetch(
    `http://localhost:8080/api/agenda/medico/${medicoId}?fecha=${fecha}`,
    {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    }
  );
  return await response.json();
};
```

---

**¡Listo para usar! 🚀**

Para más detalles, consulta:
- 📖 [README.md](README.md) - Documentación completa
- 📋 [RESUMEN_IMPLEMENTACION.md](RESUMEN_IMPLEMENTACION.md) - Detalles técnicos
- 🌐 [Swagger UI](http://localhost:8080/swagger-ui.html) - Documentación interactiva
