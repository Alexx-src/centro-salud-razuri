# Sistema Clínico Rázuri (SistemaRural-PE)

Sistema modular y profesional desarrollado en Java OpenJDK bajo una arquitectura multiparadigma (POO, funcional y orientada a eventos).

## 📂 1. Estructura de Directorios y Paquetes del Proyecto

La arquitectura del proyecto está dividida en capas independientes dentro de `src/main/java/pe/razuri/` para separar la responsabilidad de cada componente:

- **`pe.razuri.modelo`**: Contiene las entidades principales del dominio (`Paciente`, `HistorialClinico`, `EvolucionClinica`, `Usuario`, `ProfesionalSalud`, `PersonalGestion`). También incluye las subclases de documentos clínicos y la clase `DocumentoClinicoFactory` para la creación desacoplada de archivos.
- **`pe.razuri.servicio`**: Alberga la lógica de negocio central (`ServicioPaciente`, `ServicioHistorial`, `ServicioAuditoria`). Se encarga de coordinar las operaciones entre los modelos, la seguridad y los eventos.
- **`pe.razuri.seguridad`**: Encargada de la protección del sistema. Contiene `ControlAcceso` para la validación de roles/permisos y `SeguridadClave` para el cifrado de contraseñas mediante el algoritmo criptográfico PBKDF2WithHmacSHA256.
- **`pe.razuri.evento`**: Implementa la arquitectura orientada a eventos (`BusEventos`, `EventoHistorial`, `EventoListener`). Permite desacoplar la emisión de acciones en los historiales de su procesamiento de auditoría.
- **`pe.razuri.excepcion`**: Centraliza las excepciones personalizadas del sistema (`SistemaClinicoException`, `PacienteNoEncontradoException`, `AccesoDenegadoException`, `DniInvalidoException`). Evita el uso de excepciones genéricas y mejora la gestión de errores de negocio.
- **`Main.java`**: Punto de entrada de la aplicación. Integra todos los módulos, ejecuta las pruebas de flujo y demuestra el manejo seguro de excepciones sin exponer trazas técnicas (`e.printStackTrace()`).
- **`src/test/java/pe/razuri/`**: Carpeta destinada a albergar las pruebas unitarias automatizadas del sistema (`ServicioHistorialTest.java`) utilizando la librería JUnit 5.

## 🛠️ 2. Hitos y Commits Clave en el Proceso de Desarrollo

El sistema fue construido de manera incremental mediante Git. A continuación se detallan los commits más importantes que marcaron el avance del proyecto y la razón de su relevancia técnica:

- **`feat: crear estructura Maven del proyecto`**: Estableció la base del proyecto (`pom.xml`) y la jerarquía de paquetes bajo los estándares de desarrollo Java.
- **`feat: implementar entidades del dominio clinico`**: Definió el modelo POO base con encapsulamiento estricto (`private`), getters/setters y la herencia de usuarios (`Usuario → ProfesionalSalud / PersonalGestion`).
- **`feat: incorporar patron Factory para documentos`**: Permitió desacoplar la instanciación de documentos (`ResultadoLaboratorio`, `InformeEmergencia`) mediante `DocumentoClinicoFactory`.
- **`security: proteger credenciales mediante PBKDF2`**: Hito crítico de seguridad que implementó el cifrado con `PBKDF2WithHmacSHA256` y sal aleatoria para evitar el almacenamiento de contraseñas en texto plano.
- **`feat: incorporar control de acceso`**: Creó la clase `ControlAcceso` para validar si el usuario activo posee los permisos requeridos antes de ejecutar consultas o modificaciones.
- **`feat: agregar auditoria orientada a eventos`**: Implementó la infraestructura del `BusEventos` para registrar automáticamente las acciones del historial sin acoplar la lógica principal.
- **`feat: aplicar Stream API con map y filter`**: Incorporó el paradigma funcional en `ServicioAuditoria` para filtrar y transformar colecciones de registros de forma eficiente.
- **`test: incorporar pruebas unitarias con JUnit`**: Aseguró la calidad del código mediante pruebas automatizadas para validar la lógica de negocio y las excepciones.
EOF