# microservicio_monopatines 🛴

_Este microservicio se centra en la administración y control de monopatines, así como su estado y las paradas asociadas. Se divide en entidades clave y controladores que gestionan las operaciones necesarias._

# Entidades Principales
### Estado ✔
Representa el estado actual de un monopatín. Los estados posibles incluyen:

* Habilitado
* Mantenimiento
* Deshabilitado
### Monopatín 🛴
Se refiere a la información específica de un monopatín, incluyendo:

* Kilómetros totales recorridos
* Ubicación actual
* Kilómetros recorridos
* Estado actual del monopatín
### Parada 📍
Define las ubicaciones que albergan monopatines. Contiene:

* Nombre y ubicación de la parada
* Estado actual de la parada
* Lista de monopatines ubicados en esa parada
 ----------
# Controladores ✔
### Controlador de Estado ✅
Gestiona las operaciones relacionadas con los estados de los monopatines:

* Recuperar todos los estados
* Obtener un estado en particular por su ID
* Crear un nuevo estado
* Actualizar un estado
* Eliminar un estado existente

### Controlador de Monopatín ✅
Administra las operaciones de los monopatines:

* Recuperar todos los monopatines
* Obtener un monopatín por su ID
* Generar un reporte de monopatines por kilómetros recorridos
* Reportar la cantidad de monopatines en funcionamiento vs. en mantenimiento
* Crear un nuevo monopatín
* Actualizar un monopatín existente
* Eliminar un monopatín
### Controlador de Parada ✅
Controla las operaciones de las paradas:

* Recuperar todas las paradas
* Obtener una parada por su ID
* Verificar el estado actual de la parada según una ubicación
* Determinar si una parada existe en una ubicación
* Obtener la lista de monopatines en una parada
* Crear una nueva parada
* Actualizar una parada existente
* Agregar un monopatín a una parada
# Repositorios 🗄️↔🗄️ ✔
El servicio interactúa con una base de datos MongoDB a través de repositorios dedicados a cada entidad principal para realizar operaciones de almacenamiento y recuperación de datos.

----
# Tecnologías Utilizadas 👩🏻‍💻
* Lenguaje de programación: Spring-boot
* Base de datos: MongoDB
* Datos de la base de datos: microservicio_monopatin

#### Requerimientos y Dependencias:
* Maven
* Lombok
* Spring Web
* Spring Boot Starter Data MongoDB
* MongoDB Driver
