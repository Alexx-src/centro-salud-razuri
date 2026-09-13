cat << 'EOF' > README.md
# Sistema de Gestión para el Centro de Salud Rázuri (SistemaRural-PE)

## 📌 Bitácora de Desarrollo - Avances del Proyecto

### 1. Estructura Inicial y Paquetes
* Configuración de Java OpenJDK 17, Maven, JUnit 5 y Git/GitHub.
* Arquitectura modular organizada en: `pe.razuri.modelo`, `pe.razuri.servicio`, `pe.razuri.seguridad`, `pe.razuri.evento` y `pe.razuri.excepcion`.

### 2. Implementación del Dominio Clínico y Seguridad
* **Clases Principales (`pe.razuri.modelo`):** 
  * `Paciente`: Encapsulamiento estricto y validación de DNI de 8 dígitos mediante `DniInvalidoException`.
  * `HistorialClinico` y `EvolucionClinica`: Control de evoluciones, diagnósticos, tratamientos y notas, utilizando listas inmutables (`Collections.unmodifiableList`) para proteger la integridad de los datos.
  * **Documentos Clínicos (Jerarquía):** Clase abstracta `DocumentoClinico` con sus subclases especializadas (`ResultadoLaboratorio`, `InformeEmergencia`, `OtroDocumentoClinico`).
  * **Modelo de Usuarios:** Clase abstracta `Usuario`, enum `Rol` (`PROFESIONAL_SALUD`, `PERSONAL_GESTION`), y clases hijas `ProfesionalSalud` y `PersonalGestion`.
* **Excepciones Personalizadas (`pe.razuri.excepcion`):** 
  * Jerarquía de errores implementada (`SistemaClinicoException`, `PacienteNoEncontradoException`, `AccesoDenegadoException`, `DniInvalidoException`).
* **Seguridad y Privacidad (`pe.razuri.seguridad`):** 
  * Implementación de la clase de enmascaramiento de datos personales acorde a los estándares de protección de datos.
EOF