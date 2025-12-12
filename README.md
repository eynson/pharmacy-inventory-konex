# Pharmacy Inventory Konex 💊

Sistema full-stack para la gestión de inventario y ventas de medicamentos en las Droguerías Konex. Desarrollado con arquitectura hexagonal en el backend y Angular en el frontend.

## 📋 Tabla de Contenidos

- [Descripción General](#descripción-general)
- [Arquitectura](#arquitectura)
- [Tecnologías](#tecnologías)
- [Requisitos Previos](#requisitos-previos)
- [Instalación](#instalación)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Uso](#uso)
- [Testing](#testing)
- [Calidad de Código](#calidad-de-código)
- [API Documentation](#api-documentation)
- [Contribución](#contribución)

---

## 🎯 Descripción General

Sistema empresarial diseñado para optimizar la gestión de inventario y ventas de medicamentos, permitiendo:

- **Gestión de Medicamentos**: CRUD completo con validaciones de fechas, stock y precios
- **Gestión de Ventas**: Registro de ventas con cálculo automático de totales
- **Control de Stock**: Validación de disponibilidad y actualización automática
- **Consultas Avanzadas**: Búsqueda, filtrado y paginación de datos
- **Reportes**: Ventas por rango de fechas con métricas

---

## 🏗️ Arquitectura

### Backend - Arquitectura Hexagonal (Ports & Adapters)

El backend implementa **Clean Architecture** siguiendo el patrón hexagonal:

```
backend/pharmacy-inventory/src/main/java/com/eynson/pharmacy_inventory/
│
├── domain/                          # Capa de Dominio (Núcleo)
│   ├── model/                       # Entidades y Value Objects
│   │   ├── Medicine.java            # Entidad agregada raíz
│   │   ├── Sale.java                # Entidad agregada raíz
│   │   ├── MedicineId.java          # Value Object
│   │   ├── SaleId.java              # Value Object
│   │   ├── Money.java               # Value Object
│   │   └── Quantity.java            # Value Object
│   │
│   ├── port/                        # Contratos (Interfaces)
│   │   ├── in/                      # Puertos de entrada (Use Cases)
│   │   │   ├── CreateMedicineUseCase.java
│   │   │   ├── UpdateMedicineUseCase.java
│   │   │   ├── DeleteMedicineUseCase.java
│   │   │   ├── GetMedicineByIdUseCase.java
│   │   │   ├── GetMedicinesUseCase.java
│   │   │   ├── CreateSaleUseCase.java
│   │   │   ├── GetSaleByIdUseCase.java
│   │   │   └── GetSalesByDateRangeUseCase.java
│   │   │
│   │   └── out/                     # Puertos de salida (Repositorios)
│   │       ├── MedicineRepositoryPort.java
│   │       └── SaleRepositoryPort.java
│   │
│   ├── usecase/                     # Implementación de casos de uso
│   │   ├── medicine/
│   │   │   ├── CreateMedicineUseCaseImpl.java
│   │   │   ├── UpdateMedicineUseCaseImpl.java
│   │   │   ├── DeleteMedicineUseCaseImpl.java
│   │   │   ├── GetMedicineByIdUseCaseImpl.java
│   │   │   └── GetMedicinesUseCaseImpl.java
│   │   │
│   │   └── sale/
│   │       ├── CreateSaleUseCaseImpl.java
│   │       ├── GetSaleByIdUseCaseImpl.java
│   │       └── GetSalesByDateRangeUseCaseImpl.java
│   │
│   └── exception/                   # Excepciones de dominio
│       ├── MedicineNotFoundException.java
│       ├── SaleNotFoundException.java
│       ├── InsufficientStockException.java
│       └── InvalidDataException.java
│
├── application/                     # Capa de Aplicación
│   ├── service/                     # Servicios de aplicación
│   │   ├── MedicineApplicationService.java
│   │   └── SaleApplicationService.java
│   │
│   ├── mapper/                      # Mappers DTO <-> Domain
│   │   ├── MedicineMapper.java
│   │   └── SaleMapper.java
│   │
│   └── dto/                         # DTOs (Data Transfer Objects)
│       ├── request/
│       │   ├── CreateMedicineRequest.java
│       │   ├── UpdateMedicineRequest.java
│       │   ├── CreateSaleRequest.java
│       │   ├── GetMedicinesRequest.java
│       │   └── GetSalesByDateRangeRequest.java
│       │
│       └── response/
│           ├── MedicineResponse.java
│           ├── SaleResponse.java
│           ├── PagedMedicineResponse.java
│           └── PagedSaleResponse.java
│
└── infrastructure/                  # Capa de Infraestructura
    ├── adapter/                     # Adaptadores
    │   ├── MedicineRepositoryAdapter.java
    │   └── SaleRepositoryAdapter.java
    │
    ├── entity/                      # Entidades JPA
    │   ├── MedicineEntity.java      # Implementa Builder Pattern
    │   └── SaleEntity.java          # Implementa Builder Pattern
    │
    ├── repository/                  # Repositorios JPA
    │   ├── MedicineJpaRepository.java
    │   └── SaleJpaRepository.java
    │
    ├── controller/                  # Controladores REST
    │   ├── MedicineController.java
    │   └── SaleController.java
    │
    └── config/                      # Configuración
        └── SecurityConfig.java      # Spring Security (Stateless API)
```

**Principios aplicados:**
- ✅ **SOLID**: Cada clase tiene una responsabilidad única
- ✅ **DDD**: Agregados, Entidades y Value Objects
- ✅ **Dependency Inversion**: El dominio no depende de la infraestructura
- ✅ **Builder Pattern**: Construcción fluida de entidades complejas
- ✅ **Record Pattern**: DTOs inmutables para transferencia de datos

### Frontend - Arquitectura por Capas (Angular)

```
frontend/pharmacy-inventory-ui/src/app/
│
├── core/                            # Funcionalidad central
│   ├── services/                    # Servicios singleton
│   ├── guards/                      # Guards de navegación
│   ├── interceptors/                # HTTP Interceptors
│   └── models/                      # Interfaces y tipos
│
├── shared/                          # Componentes compartidos
│   ├── components/                  # Componentes reutilizables
│   ├── directives/                  # Directivas personalizadas
│   └── pipes/                       # Pipes personalizados
│
├── features/                        # Módulos de funcionalidad
│   ├── medicines/                   # Gestión de medicamentos
│   │   ├── components/
│   │   ├── services/
│   │   └── models/
│   │
│   └── sales/                       # Gestión de ventas
│       ├── components/
│       ├── services/
│       └── models/
│
└── app.component.ts                 # Componente raíz
```

---

## 💻 Tecnologías

### Backend

| Tecnología | Versión | Descripción |
|------------|---------|-------------|
| **Java** | 17 | Lenguaje de programación |
| **Spring Boot** | 3.5.8 | Framework de aplicación |
| **Spring Data JPA** | 6.2.14 | Persistencia de datos |
| **Spring Security** | 6.2.14 | Seguridad (Stateless API) |
| **Maven** | 3.9+ | Gestión de dependencias |
| **H2 Database** | 2.3.232 | Base de datos en memoria (desarrollo) |
| **Oracle JDBC** | 21.9.0.0 | Driver para producción |
| **Flyway** | 10.30.0 | Migración de base de datos |
| **Lombok** | 1.18.36 | Reducción de boilerplate |
| **JUnit 5** | 5.11.4 | Framework de testing |
| **JaCoCo** | 0.8.10 | Cobertura de código |

### Frontend

| Tecnología | Versión | Descripción |
|------------|---------|-------------|
| **Angular** | 19.0.6 | Framework frontend |
| **TypeScript** | 5.6.3 | Lenguaje tipado |
| **RxJS** | 7.8.1 | Programación reactiva |
| **Karma** | 6.4.4 | Test runner |
| **Jasmine** | 5.4.0 | Framework de testing |
| **Node.js** | 18.x+ | Entorno de ejecución |
| **npm** | 10.x+ | Gestor de paquetes |

### Herramientas de Calidad

| Herramienta | Versión | Propósito |
|-------------|---------|-----------|
| **SonarQube** | 9.9.8 Community | Análisis estático de código |
| **SonarScanner** | 3.10.0 | Plugin Maven para análisis |

---

## 📦 Requisitos Previos

### Software Requerido

- **Java JDK 17** o superior
- **Maven 3.9+**
- **Node.js 18.x** o superior
- **npm 10.x** o superior
- **Docker** (opcional, para SonarQube)
- **Git** para control de versiones

### Verificar Instalación

```powershell
# Java
java -version

# Maven
mvn -version

# Node.js
node --version

# npm
npm --version
```

---

## 🚀 Instalación

### 1. Clonar el Repositorio

```powershell
git clone https://github.com/tu-usuario/pharmacy-inventory-konex.git
cd pharmacy-inventory-konex
```

### 2. Backend - Configuración

```powershell
# Navegar al directorio del backend
cd backend\pharmacy-inventory

# Instalar dependencias y compilar
mvn clean install

# Ejecutar tests
mvn test

# Iniciar aplicación
mvn spring-boot:run
```

El backend estará disponible en: `http://localhost:8080`

**Endpoints principales:**
- `/api/medicines` - CRUD de medicamentos
- `/api/sales` - CRUD de ventas
- `/h2-console` - Consola de H2 Database

### 3. Frontend - Configuración

```powershell
# Navegar al directorio del frontend
cd frontend\pharmacy-inventory-ui

# Instalar dependencias
npm install

# Iniciar servidor de desarrollo
npm start
```

El frontend estará disponible en: `http://localhost:4200`

---

## 📁 Estructura del Proyecto

```
pharmacy-inventory-konex/
│
├── backend/
│   └── pharmacy-inventory/
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/              # Código fuente Java
│       │   │   └── resources/
│       │   │       ├── application.yaml
│       │   │       └── db/migration/  # Scripts Flyway
│       │   │
│       │   └── test/                  # Tests unitarios e integración
│       │
│       ├── pom.xml                    # Configuración Maven
│       └── target/                    # Artefactos compilados
│
├── frontend/
│   └── pharmacy-inventory-ui/
│       ├── src/
│       │   ├── app/                   # Código fuente Angular
│       │   ├── assets/                # Recursos estáticos
│       │   └── styles.scss            # Estilos globales
│       │
│       ├── package.json               # Dependencias npm
│       ├── angular.json               # Configuración Angular
│       └── tsconfig.json              # Configuración TypeScript
│
├── docs/                              # Documentación adicional
├── sonar-project.properties           # Configuración SonarQube
├── sonar-analyze.ps1                  # Script de análisis
├── SECURITY.md                        # Justificación de seguridad
├── SONARQUBE_SETUP.md                 # Guía de SonarQube
└── README.md                          # Este archivo
```

---

## 🎮 Uso

### Backend - API REST

#### Medicamentos

**Crear medicamento:**
```http
POST /api/medicines
Content-Type: application/json

{
  "name": "Acetaminofén 500mg",
  "factoryLaboratory": "Genfar",
  "manufacturingDate": "2024-01-15T10:00:00",
  "expirationDate": "2026-01-15T10:00:00",
  "quantityInStock": 100,
  "unitValue": 2500.00
}
```

**Obtener medicamentos (paginado):**
```http
GET /api/medicines?page=0&size=10&sortBy=name
```

**Actualizar medicamento:**
```http
PUT /api/medicines
Content-Type: application/json

{
  "id": "uuid-del-medicamento",
  "name": "Acetaminofén 500mg",
  "factoryLaboratory": "Genfar",
  "manufacturingDate": "2024-01-15T10:00:00",
  "expirationDate": "2026-01-15T10:00:00",
  "quantityInStock": 150,
  "unitValue": 2600.00
}
```

**Eliminar medicamento:**
```http
DELETE /api/medicines/{id}
```

#### Ventas

**Crear venta:**
```http
POST /api/sales
Content-Type: application/json

{
  "medicineId": "uuid-del-medicamento",
  "quantitySold": 5
}
```

**Obtener ventas por rango de fechas:**
```http
GET /api/sales/by-date-range?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59&page=0&size=10
```

### Frontend - Interfaz de Usuario

1. **Dashboard**: Vista principal con resumen
2. **Medicamentos**: Listado con opciones de crear, editar y eliminar
3. **Ventas**: Registro de ventas y consultas por fecha
4. **Búsqueda**: Filtros avanzados y paginación

---

## 🧪 Testing

### Backend

```powershell
cd backend\pharmacy-inventory

# Ejecutar todos los tests
mvn test

# Ejecutar tests con reporte de cobertura
mvn clean test jacoco:report

# Ver reporte de cobertura
# Abrir: target/site/jacoco/index.html
```

**Métricas de Testing:**
- ✅ **77 tests unitarios** (100% passing)
- ✅ **65% cobertura de instrucciones**
- ✅ **39% cobertura de branches**
- ✅ **95% cobertura de clases**

**Distribución de tests:**
- Mappers: 10 tests (100% coverage)
- Value Objects: 17 tests (100% coverage)
- Use Cases: 28 tests (77% coverage)
- Repositories: 12 tests (75% coverage)
- Controllers: 5 tests (32% coverage)
- Application Services: 5 tests (15% coverage)

### Frontend

```powershell
cd frontend\pharmacy-inventory-ui

# Ejecutar tests unitarios
npm test

# Ejecutar tests con cobertura
npm run test:coverage

# Ver reporte de cobertura
# Abrir: coverage/index.html
```

**Métricas de Testing:**
- ✅ **80 tests unitarios** (100% passing)
- ✅ **87.23% cobertura general**

---

## 🔍 Calidad de Código

### SonarQube Setup

El proyecto incluye análisis estático de código con SonarQube.

#### Iniciar SonarQube con Docker

```powershell
# Iniciar contenedor
docker run -d --name sonarqube `
  -p 9000:9000 `
  -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true `
  sonarqube:lts-community

# Esperar inicio (2-3 minutos)
# Acceder a: http://localhost:9000
# Usuario: admin / Contraseña: admin
```

#### Analizar Backend

```powershell
cd backend\pharmacy-inventory

# Ejecutar análisis
mvn clean verify sonar:sonar `
  -Dsonar.projectKey=pharmacy-inventory-backend `
  -Dsonar.host.url=http://localhost:9000 `
  -Dsonar.login=tu-token-aqui

# O usar el script incluido
..\..\..\sonar-analyze.ps1
```

#### Dashboard de SonarQube

Acceder a: http://localhost:9000/dashboard?id=pharmacy-inventory-backend

**Métricas actuales:**
- ✅ **0 Bugs**
- ✅ **0 Vulnerabilidades**
- ✅ **Code Smells**: Resueltos (refactorizado con Builder y Record patterns)
- ⚠️ **Security Hotspot**: CSRF disabled (justificado en SECURITY.md)

Para más detalles, consultar [SONARQUBE_SETUP.md](SONARQUBE_SETUP.md)

---

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api
```

### Autenticación
Actualmente la API es de acceso público (desarrollo). En producción se recomienda implementar JWT.

### Endpoints

#### Medicines API

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/medicines` | Lista paginada de medicamentos |
| `GET` | `/medicines/{id}` | Obtener medicamento por ID |
| `POST` | `/medicines` | Crear nuevo medicamento |
| `PUT` | `/medicines` | Actualizar medicamento existente |
| `DELETE` | `/medicines/{id}` | Eliminar medicamento |

#### Sales API

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/sales/{id}` | Obtener venta por ID |
| `GET` | `/sales/by-date-range` | Ventas por rango de fechas (paginado) |
| `POST` | `/sales` | Crear nueva venta |

### Códigos de Respuesta

| Código | Descripción |
|--------|-------------|
| `200 OK` | Operación exitosa |
| `201 Created` | Recurso creado exitosamente |
| `400 Bad Request` | Datos inválidos |
| `404 Not Found` | Recurso no encontrado |
| `500 Internal Server Error` | Error del servidor |

---

## 🤝 Contribución

### Convenciones de Código

#### Backend (Java)
- Seguir convenciones de Java (CamelCase, PascalCase)
- Usar Lombok para reducir boilerplate
- Mantener arquitectura hexagonal
- Tests unitarios obligatorios (mínimo 70% coverage)
- JavaDoc en métodos públicos

#### Frontend (TypeScript)
- Seguir guía de estilo de Angular
- Usar tipos TypeScript (evitar `any`)
- Componentes pequeños y reutilizables
- Tests unitarios para componentes

### Proceso de Contribución

1. Fork el repositorio
2. Crear rama feature: `git checkout -b feature/nueva-funcionalidad`
3. Commit cambios: `git commit -m "feat: descripción del cambio"`
4. Push a la rama: `git push origin feature/nueva-funcionalidad`
5. Crear Pull Request

### Commit Messages

Seguir [Conventional Commits](https://www.conventionalcommits.org/):

```
feat: nueva funcionalidad
fix: corrección de bug
docs: cambios en documentación
style: formateo de código
refactor: refactorización
test: agregar o modificar tests
chore: tareas de mantenimiento
```

---

## 📄 Licencia

Este proyecto es propiedad de Droguerías Konex. Todos los derechos reservados.

---

## 👥 Equipo de Desarrollo

- **Arquitectura**: Equipo de desarrollo
- **Backend**: Java Spring Boot
- **Frontend**: Angular
- **QA**: SonarQube + Tests automatizados

---

## 📞 Soporte

Para consultas o soporte técnico, contactar a:
- Email: soporte@drogueriaskonex.com
- Repositorio: [GitHub Issues](https://github.com/tu-usuario/pharmacy-inventory-konex/issues)

---

## 🔄 Changelog

Ver [CHANGELOG.md](CHANGELOG.md) para historial de cambios detallado.

---

## 📊 Estado del Proyecto

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![Tests](https://img.shields.io/badge/tests-77%20passing-brightgreen)
![Coverage](https://img.shields.io/badge/coverage-65%25-yellow)
![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.8-brightgreen)
![Angular](https://img.shields.io/badge/Angular-19.0.6-red)

---

**Desarrollado con ❤️ para Droguerías Konex**
