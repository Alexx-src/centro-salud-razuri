# Sistema de Gestión para el Centro de Salud Rázuri (SistemaRural-PE)

## 📌 Bitácora de Desarrollo - Versión 1.0

### 1. Configuración Inicial y Estructura del Proyecto
* **Entorno y Herramientas:** Java OpenJDK 17, Maven, JUnit 5, Git/GitHub.
* **Estructura de Paquetes (MVC / Arquitectura limpia):** Se crearon los paquetes organizados bajo la ruta raíz `pe.razuri`:
  * `pe.razuri.modelo`: Clases principales del dominio clínico (`Paciente`, `HistorialClinico`).
  * `pe.razuri.servicio`: Lógica de negocio y consultas mediante programación funcional (Java Streams).
  * `pe.razuri.seguridad`: Enmascaramiento de datos personales para cumplir con la Ley N.º 29733.
  * `pe.razuri.evento`: Trazabilidad de accesos a historiales clínicos.
  * `pe.razuri.excepcion`: Excepciones personalizadas del dominio.

### 2. Implementación de Clases y Manejo de Errores
* **Modelo de Datos:** Se implementó `Paciente` con encapsulamiento estricto y la clase `HistorialClinico` para gestionar la evolución clínica y documentos adjuntos.
* **Excepciones Personalizadas:** Se desarrolló jerarquía de errores basada en `SistemaClinicoException` para un control robusto:
  * `PacienteNoEncontradoException`
  * `AccesoDenegadoException`
  * `DniInvalidoException`