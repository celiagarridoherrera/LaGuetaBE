# Proyecto "La Gueta"

**La Gueta** es una web para un bar, con un sistema de gestión de productos, opiniones y galería de imágenes. La plataforma permite a los administradores gestionar productos, visualizar opiniones y añadir imágenes de manera sencilla. La web también está optimizada para SEO, destacando que es un chigre-pub donde se escancia sidra.

## Tecnologías

- **Frontend:** React, Tailwind CSS, Axios
- **Backend:** Java, Spring Boot, Spring Security, H2
- **Base de Datos:** H2 (en memoria para desarrollo)
- **Autenticación:** JWT (JSON Web Tokens)
- **Almacenamiento de imágenes:** Local (en el servidor, se almacenan en carpetas específicas)
  
## Características

### Funcionalidades del Frontend

- **Vista pública:**
  - Carta del menú (productos)
  - Galería de imágenes
  - Sección de opiniones
  - Información de contacto
  
- **Vista privada (Admin):**
  - CRUD de productos (crear, leer, actualizar, eliminar productos)
  - CRUD de opiniones (ver y eliminar opiniones)
  - Subida de imágenes (productos y galería)
  
### Autenticación

- **Login real** con autenticación de usuario mediante JWT.
  
### Implementación de Imágenes

- Las imágenes se almacenan de forma local en el servidor y se vinculan a productos y galería.
- Cuando se suben imágenes, se generan nombres únicos para evitar colisiones.

## Estructura del Proyecto

- **Frontend:**  
  La parte frontend está desarrollada con React y utiliza Tailwind CSS para el diseño responsivo. Axios se usa para la comunicación con el backend a través de los endpoints REST.

- **Backend:**  
  El backend está construido con Spring Boot, y utiliza H2 como base de datos en memoria para facilitar el desarrollo. La seguridad está implementada con Spring Security, y la autenticación de usuarios se maneja mediante JWT.
