# Proyecto Base Implementando Clean Architecture

## Antes de Iniciar

Empezaremos por explicar los diferentes componentes del proyectos y partiremos de los componentes externos, continuando con los componentes core de negocio (dominio) y por último el inicio y configuración de la aplicación.

Lee el artículo [Clean Architecture — Aislando los detalles](https://medium.com/bancolombia-tech/clean-architecture-aislando-los-detalles-4f9530f35d7a)

# Arquitectura

![Clean Architecture](https://miro.medium.com/max/1400/1*ZdlHz8B0-qu9Y-QO3AXR_w.png)

## Domain

Es el módulo más interno de la arquitectura, pertenece a la capa del dominio y encapsula la lógica y reglas del negocio mediante modelos y entidades del dominio.

## Usecases

Este módulo gradle perteneciente a la capa del dominio, implementa los casos de uso del sistema, define lógica de aplicación y reacciona a las invocaciones desde el módulo de entry points, orquestando los flujos hacia el módulo de entities.

## Infrastructure

### Helpers

En el apartado de helpers tendremos utilidades generales para los Driven Adapters y Entry Points.

Estas utilidades no están arraigadas a objetos concretos, se realiza el uso de generics para modelar comportamientos
genéricos de los diferentes objetos de persistencia que puedan existir, este tipo de implementaciones se realizan
basadas en el patrón de diseño [Unit of Work y Repository](https://medium.com/@krzychukosobudzki/repository-design-pattern-bc490b256006)

Estas clases no puede existir solas y debe heredarse su compartimiento en los **Driven Adapters**

### Driven Adapters

Los driven adapter representan implementaciones externas a nuestro sistema, como lo son conexiones a servicios rest,
soap, bases de datos, lectura de archivos planos, y en concreto cualquier origen y fuente de datos con la que debamos
interactuar.

### Entry Points

Los entry points representan los puntos de entrada de la aplicación o el inicio de los flujos de negocio.

## Application

Este módulo es el más externo de la arquitectura, es el encargado de ensamblar los distintos módulos, resolver las dependencias y crear los beans de los casos de use (UseCases) de forma automática, inyectando en éstos instancias concretas de las dependencias declaradas. Además inicia la aplicación (es el único módulo del proyecto donde encontraremos la función “public static void main(String[] args)”.

**Los beans de los casos de uso se disponibilizan automaticamente gracias a un '@ComponentScan' ubicado en esta capa.**
---

# 📦 Prueba Práctica Dev. Backend API de Gestión de Franquicias

---

## Descripción del Proyecto
Este proyecto consiste en una **API REST** diseñada para la administración de franquicias, permitiendo la gestión eficiente de sucursales y productos.
Una **franquicia** contiene:
- Un nombre
- Una lista de **sucursales**, donde cada sucursal tiene:
  - Un nombre
  - Una lista de **productos**, cada uno con:
    - Un nombre
    - Una cantidad de stock
      
---
###  Objetivo

La API facilita el manejo estructurado de franquicias mediante operaciones CRUD (Crear, Leer, Actualizar, Eliminar). Su propósito es proporcionar una gestión **rápida, flexible y segura** del inventario dentro de múltiples sucursales.

---

###  Características Principales

-  **Arquitectura basada en Clean Architecture**  
  Separación clara de responsabilidades mediante capas: dominio, aplicación y adaptadores. Aplicación de principios **SOLID** para un diseño limpio y mantenible.

-  **Spring Boot con WebFlux**  
  Procesamiento reactivo y asincrónico para alto rendimiento, especialmente útil en aplicaciones escalables.

-  **MongoDB como base de datos NoSQL**  
  Permite almacenar documentos de forma flexible y escalar horizontalmente con facilidad.

-  **Validaciones con Spring Boot Validation**  
  Se valida la entrada del usuario a nivel de DTO, evitando datos erróneos o malformados.

-  **Manejo centralizado de errores**  
  Implementado con un `GlobalExceptionHandler` que responde con mensajes estructurados y códigos de error personalizados.

-  **Documentación clara y paso a paso**  
  Facilitando el despliegue de la aplicación en local mediante Docker o Maven, con endpoints bien definidos.

---
##  Requisitos

Antes de desplegar la aplicación, asegúrate de tener instalados los siguientes componentes:

| Herramienta                 | Descripción                                                                  |
|-----------------------------|------------------------------------------------------------------------------|
| ☕ **Java 17**             | Versión recomendada para ejecutar el backend con Spring Boot.                |
| 📦 **Gradle**              | Usado para compilar, construir y gestionar las dependencias del proyecto.    |
| 🧭 **MongoDB Compass**     | Herramienta visual para gestionar y consultar la base de datos MongoDB.      |
| 🔍 **Postman**             | Ideal para probar manualmente los endpoints REST de la API.                  |
| 🐳 **Docker** *(opcional)* | Permite levantar MongoDB y/o el backend en contenedores de forma rápida.     |

> 🔧 **Recomendación:** Verifica que cada herramienta esté instalada correctamente antes de continuar.  

---
##  Instalación y Configuración

##  1. Configurar la base de datos MongoDB  

La API utiliza **MongoDB** como almacenamiento de datos. Puedes configurarlo de dos maneras:

---

###  1. MongoDB Local

1. Descarga e instala **MongoDB Community Server** desde [mongodb.com/try/download/community](https://www.mongodb.com/try/download/community).
2. Asegúrate de que el servicio esté corriendo en `localhost:27017`.
3. Usa **MongoDB Compass** (opcional) para visualizar la base de datos y las colecciones.
4. Crea la base de datos `test_accenture_bd` manualmente o deja que Spring Boot la cree automáticamente cuando insertes documentos.

---
### 2. Clonar el repositorio

Para obtener el código fuente del proyecto, abre una terminal y ejecuta:

```bash
git clone https://github.com/AJulio569/franchise-app.git
cd franchise-app

```
---
###  3. Configurar versión de Java en Gradle  

Para asegurarse de que todos los módulos usen la misma versión de Java, se puede definir globalmente en el `build.gradle` raíz:

```groovy
allprojects {
    tasks.withType(JavaCompile).configureEach {
        it.sourceCompatibility = JavaVersion.VERSION_17
        it.targetCompatibility = JavaVersion.VERSION_17
    }
}
```
---
## Endpoints del Proyecto  

A continuación, se describen los principales endpoints disponibles en la API del proyecto.

### Agregar una nueva franquicia  

Este endpoint permite la creación de una nueva franquicia proporcionando su nombre en el cuerpo de la solicitud.  

**Método:** `POST`  
**URL:** `http://localhost:8081/api/franchise`  

#### Ejemplo de solicitud  

```json
{
  "name": "NameFranchise"
}

```
### Obtener todas las franquicias  

Este endpoint permite mostrar todas las franquicias. 

**Método:** `GET`  
**URL:** `http://localhost:8081/api/franchise`  

#### Ejemplo de solicitud 

```url

http://localhost:8081/api/franchise

``` 

### Obtener una franquicias

Este endpoint permite mostrar una franquicias por su ID. 
 **Remplazar** `{id}` por el ID de la franquicia 

**Método:** `GET`  
**URL:** `http://localhost:8081/api/franchise/id/{id}`  

#### Ejemplo de solicitud 

```url

http://localhost:8081/api/franchise/id/680fb4c7dbcd1f7a78649310

``` 


### Agregar una nueva sucursal a la franquicia

 Este endpoint permite agregar una nueva sucursal a la franquicia proporcionando su nombre en el cuerpo de la solicitud.
 **Remplazar** `{franchiseId}` por el ID de la franquicia 

**Método:** `POST`  
**URL:** `http://localhost:8081/api/franchise/id/{franchiseId}/branch` 

#### Ejemplo de solicitud  


```json
{
  "name": "NameBranch"
}

```

###  Agregar un nuevo producto a la sucursal 

Este endpoint permite agregar una nuevo producto a la  sucursal de una franquicia proporcionando su nombre y stock en el cuerpo de la solicitud.
 **Remplazar** `{franchiseId}` por el ID de la franquicia y `{branchName}` por el nombre de la sucursal

**Método:** `POST`  
**URL:** `http://localhost:8081/api/franchise/id/{franchiseId}/branch/{branchName}/product` 

#### Ejemplo de solicitud  


```json
{
    "name":"NameProduct" ,
    "stock": 20
}

```

###  Eliminar un producto a una sucursal  

Este endpoint permite eliminar un producto de la sucursal de una franquicia proporcionando su nuevo stock en el cuerpo de la solicitud.
 **Remplazar** `{franchiseId}` por el ID de la franquicia, `{branchName}` por el nombre de la sucursal y `{productName}` por el nombre del producto

**Método:** `DELETE`  
**URL:** `http://localhost:8081/api/franchise/id/{franchiseId}/branch/{branchName}/product/{productName}` 

#### Ejemplo de solicitud  

```url

http://localhost:8081/api/franchise/id/680fb4c7dbcd1f7a78649310/branch/NameBranch/product/NameProduct

```

###  Actualizar un Stock de un nuevo producto 

Este endpoint permite actualizar un stock de producto en la sucursal de una franquicia.
 **Remplazar** `{franchiseId}` por el ID de la franquicia, `{branchName}` por el nombre de la sucursal y `{productName}` por el nombre del producto


**Método:** `PUT`  
**URL:** `http://localhost:8081/api/franchise/id/{franchiseId}/branch/{branchName}/product/{productName}/stock` 

#### Ejemplo de solicitud 

```json
{
    "newStock": 100
}

```

###  Obtener cual es el producto que más stock tiene por sucursal

Este endpoint permite  mostrar cual es el producto que más stock tiene por sucursal para una 
franquicia puntual. Retorna un listado de productos que indiquen a que sucursal pertenece. 
 **Remplazar** `{franchiseId}` por el ID de la franquicia 


**Método:** `GET`  
**URL:** `http://localhost:8081/api/franchise/id/{franchiseId}/product/top-stock` 

#### Ejemplo de solicitud 

```url

http://localhost:8081/api/franchise/id/680fb4c7dbcd1f7a78649310/product/top-stock

```

###  Actualizar el nombre de la franquicia

Este endpoint permite Actualizar el nombre de una franquicia proporcionando su nuevo nombre en el cuerpo de la solicitud.
 **Remplazar** `{id}` por el ID de la franquicia que se va actualizar

**Método:** `PUT`  
**URL:** `http://localhost:8081/api/franchise/id/{id}` 

#### Ejemplo de solicitud 

```json
{
    "name": "NameFranchise"
}

```

###   Actualizar el nombre de la sucursal


Este endpoint permite Actualizar el nombre de una sucursal proporcionando su nuevo nombre en el cuerpo de la solicitud.
 **Remplazar** `{franchiseId}` por el ID de la franquicia y `{branchName}` por el nombre de la sucursal que se va actualizar

**Método:** `PUT`  
**URL:** `http://localhost:8081/api/franchise/id/{franchiseId}/branch/{branchName}/name` 

#### Ejemplo de solicitud 

```json
{
    "newName": "NameBranch"
}

```

###    Actualizar el nombre del producto

Este endpoint permite Actualizar el nombre del producto proporcionando su nuevo nombre en el cuerpo de la solicitud.
 **Remplazar** `{franchiseId}` por el ID de la franquicia, `{branchName}` por el nombre de la sucursal que se va actualizar y `{productName}` por el nombre del producto que se va actualizar

**Método:** `PUT`  
**URL:** `http://localhost:8081/api/franchise/id/{franchiseId}/branch/{branchName}/product/{productName}/name` 

#### Ejemplo de solicitud 

```json
{
    "newName": "NameProduct"
}

```





