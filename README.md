# 🏷️ Prices API (Inditex Tech Test)

¡Bienvenido! Este proyecto es una implementación profesional para el sistema de consulta de precios de productos. Está diseñado bajo los principios de **Arquitectura Hexagonal** y **Clean Code**, asegurando un sistema escalable, testeable y fácil de mantener.

## 🚀 Funcionalidades clave
El servicio resuelve el reto de negocio de la siguiente manera:
- **Gestión de Tarifas:** Selección inteligente de precios basada en rangos de fechas.
- **Lógica de Prioridad:** Ante solapamientos horarios, el sistema aplica automáticamente la tarifa con mayor prioridad.
- **Robustez:** Validación estricta de parámetros y manejo de errores mediante el estándar **RFC 7807 (Problem Details)**.

---

## 🛠️ Stack Tecnológico
- **Core:** Java 25 & Spring Boot 4.0.1 🍃
- **Persistencia:** Spring Data JPA + H2 (In-memory) 🗄️
- **Migraciones:** Flyway (Versionado de base de datos) ✈️
- **Documentación:** Swagger UI / OpenAPI 3 📖
- **Calidad de Código:**
  - **Checkstyle:** Siguiendo el estándar de Google.
  - **PMD:** Análisis estático para evitar fallos de diseño y variables mal nombradas.
  - **JaCoCo:** Reportes de cobertura de tests automatizados.

---

## 🏗️ Arquitectura
He optado por una **Arquitectura Hexagonal (Puertos y Adaptadores)** para desacoplar la lógica de negocio de la infraestructura tecnológica:

* **`domain`**: El corazón del sistema. Contiene las entidades, excepciones de negocio y los contratos (interfaces) del repositorio.
* **`application`**: Casos de uso y servicios. Aquí reside la lógica de cálculo y la gestión de la capa de caché.
* **`infrastructure`**: Adaptadores de entrada (REST Controllers con OpenAPI) y salida (Persistencia JPA con H2).

---

## 🚥 Guía de Inicio Rápido

### Requisitos
* **Java 25**
* **Maven 3.9+** (o usar el wrapper `./mvnw` incluido)

### Ejecución en local
Para levantar el servicio rápidamente desde la terminal:
```bash
./mvnw clean spring-boot:run
```

La aplicación estará disponible en:
- `http://localhost:8080`

### Verificación de Calidad y Tests 🧪
Para ejecutar la suite completa de tests (Unitarios e Integración), junto con los controles de calidad de Checkstyle y PMD:
```bash
./mvnw clean verify
```

El reporte de cobertura de JaCoCo se generará tras la ejecución en: `target/site/jacoco/index.html`.

---

## 🐳 Dockerización
La aplicación está totalmente preparada para entornos de contenedores.

Construir y arrancar con Docker Compose:
```bash
docker compose up --build
```

Nota: El archivo `compose.yaml` ya gestiona el mapeo de puertos y el healthcheck de la aplicación para asegurar que el servicio esté listo antes de recibir peticiones.

---

## 📖 Documentación de la API

### Endpoint principal
`GET /api/v1/prices`

**Query params obligatorios:**
- `applicationDate`: Formato `yyyy-MM-dd HH:mm:ss`
- `productId`: Identificador del producto (ej: 35455)
- `brandId`: Identificador de la cadena (ej: 1 para ZARA)

**Ejemplo de consulta (curl)**
```bash
curl --location 'localhost:8080/api/v1/prices?applicationDate=2020-06-14%2010%3A00%3A00&productId=35455&brandId=1'
```

### Otros enlaces de interés
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Consola H2**: http://localhost:8080/h2-console
  - **JDBC URL**: `jdbc:h2:mem:inditex`
  - **User**: `sa` / **Password**: (vacío)

---

## 📁 Estructura de Datos (Migraciones)
Flyway se encarga de la creación del esquema y la carga de datos iniciales en cada arranque:
- `V1__Create_brand_table.sql`: Definición de marcas
- `V2__Create_prices_table.sql`: Definición de la tabla de precios
- `V3__Insert_prices_data.sql`: Carga de los datos de prueba del enunciado

---

*Desarrollado con atención al detalle para el proceso de selección de Inditex.*
