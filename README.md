# SINOVA API – Módulo de empleados (GA7-220501096-AA3-EV01)

CRUD del módulo de empleados de **SINOVA (Sistema de Liquidación de Nómina)** con **Spring Boot 3.5**, **Spring Data JPA (Hibernate)** y **MySQL**, sobre la base de datos `sinova` del proyecto.

## Endpoints
| Método | URL | Acción |
|---|---|---|
| GET | `/api/empleados` | Listar empleados |
| GET | `/api/empleados/{id}` | Consultar por id |
| GET | `/api/empleados/documento/{documento}` | Consultar por documento |
| POST | `/api/empleados` | Registrar empleado |
| PUT | `/api/empleados/{id}` | Actualizar empleado |
| DELETE | `/api/empleados/{id}` | Eliminar empleado |

Validaciones: tipo de documento (CC, CE, TI, PA), documento numérico de 6 a 12 dígitos y sin repetir, nombres y apellidos solo letras (2 a 80), fecha de nacimiento anterior a hoy, teléfono de 7 a 10 dígitos, formato de correo y longitudes máximas.

## Estructura
`entity` (Empleado) · `repository` (EmpleadoRepository) · `service` (EmpleadoService) · `controller` (EmpleadoController) · `exception` (manejo de errores)

## Ejecución
1. Tener la base `sinova` creada en MySQL 8.
2. Copiar `src/main/resources/db-local.properties.example` como `db-local.properties` y escribir la clave de MySQL.
3. Abrir en NetBeans (File > Open Project) y ejecutar `SinovaApiApplication` (Run File).
4. Probar en Postman: `http://localhost:8080/api/empleados`

Tecnologías: Java 25 · Spring Boot 3.5.7 · Spring Web · Spring Data JPA · Validation · MySQL Connector/J · Lombok · DevTools · NetBeans 31.

Autor: Wilmar Andrés Matiz Rodas – Ficha 3336196 – SENA
