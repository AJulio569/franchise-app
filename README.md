
<img width="1024" height="1024" alt="ChatGPT Image 23 jul 2025, 08_35_59 p m" src="https://github.com/user-attachments/assets/4c4ca592-9b90-4a48-a576-345a2511f1f0" />


# 📦  API de Franquicias - Arquitectura Hexagonal con Java WebFlux y MongoDB (Reactive)

Este proyecto es una API REST reactiva desarrollada en **Java 17**, usando **Spring Boot WebFlux**, **MongoDB** y siguiendo la **arquitectura limpia hexagonal** propuesta por el _scaffold-clean-architecture_ de Bancolombia. Permite gestionar franquicias, sucursales y productos, así como consultar el producto con más stock por sucursal.

---

## 📖 Descripción del Proyecto
Este proyecto consiste en una **API REST** diseñada para la administración de franquicias, permitiendo la gestión eficiente de sucursales y productos.
Una **franquicia** contiene:
- Un nombre
- Una lista de **sucursales**, donde cada sucursal tiene:
  - Un nombre
  - Una lista de **productos**, cada uno con:
    - Un nombre
    - Una cantidad de stock
      
---

### 🎯 Objetivo

La API facilita el manejo estructurado de franquicias mediante operaciones CRUD (Crear, Leer, Actualizar, Eliminar). Su propósito es proporcionar una gestión **rápida, flexible y segura** del inventario dentro de múltiples sucursales.

---

### ⚙️ Características Principales

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
## 🧰 Requisitos

Antes de desplegar la aplicación, asegúrate de tener instalados los siguientes componentes:

| Herramienta                 | Descripción                                                                  |
|-----------------------------|------------------------------------------------------------------------------|
| ☕ **Java 17**             | Versión recomendada para ejecutar el backend con Spring Boot.                |
| 📦 **Gradle**              | Usado para compilar, construir y gestionar las dependencias del proyecto.    |
| 🍃 **MongoDB Compass**     | Herramienta visual para gestionar y consultar la base de datos MongoDB.      |
| 📫 **Postman**             | Ideal para probar manualmente los endpoints REST de la API.                  |
| 🐳 **Docker** *(opcional)* | Permite levantar MongoDB y/o el backend en contenedores de forma rápida.     |

> 🔧 **Recomendación:** Verifica que cada herramienta esté instalada correctamente antes de continuar.  

---
## 🛠️ Instalación y Configuración

###  1. MongoDB Local

La API utiliza **MongoDB** como almacenamiento de datos. Puedes configurarlo de dos maneras:

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

## 🌐 Endpoints del Proyecto  

A continuación, se describen los principales endpoints disponibles en la API del proyecto.

### ➕ Agregar una nueva franquicia  

Este endpoint permite la creación de una nueva franquicia proporcionando su nombre en el cuerpo de la solicitud.  

**Método:** `POST`  
**URL:** `http://localhost:8081/api/franchises`  

#### 🧪 Ejemplo de solicitud  

**Request Body:**
```json
{
  "name": "ROOTT+CO"
}

```

**Response**

**Code:** `201 Created`

```json
{
  "id": "664b2a7d47b8a72fc9e4f2c3",
  "name": "ROOTT+CO",
  "branches": []
}

```
---

### 📋 Obtener todas las franquicias  

Este endpoint permite mostrar todas las franquicias. 

**Método:** `GET`  
**URL:** `http://localhost:8081/api/franchises`  

#### 🧪 Ejemplo de solicitud 

```url

http://localhost:8081/api/franchises

``` 
**Response**

**Code:** `200 OK`

```json
[
  {
      "id": "664b2a7d47b8a72fc9e4f2c3",
      "name": "ROOTT+CO",
      "branches": []
  },
  {
     "id": "664b2a7d47b8a72fc9e4f2c4",
     "name": "NIKE",
     "branches": []
  }
]


```
---

### 🔍 Obtener una franquicia

Este endpoint permite mostrar una franquicia por su ID. 
#### 📥 Parámetros
 **Remplazar** `{franchiseId}` por el ID de la franquicia 

**Método:** `GET`  
**URL:** `http://localhost:8081/api/franchises/{franchiseId}`  

#### 🧪 Ejemplo de solicitud 

```url

http://localhost:8081/api/franchise/664b2a7d47b8a72fc9e4f2c3

```

**Response**

**Code:** `200 OK`

```json
  {
      "id": "664b2a7d47b8a72fc9e4f2c3",
      "name": "ROOTT+CO",
      "branches": []
  }

```
---

### 🏢 Agregar una nueva sucursal a la franquicia

 Este endpoint permite agregar una nueva sucursal a la franquicia proporcionando su nombre en el cuerpo de la solicitud.
 
 #### 📥 Parámetros
 
 **Remplazar** `{franchiseId}` por el ID de la franquicia 

**Método:** `POST`  
**URL:** `http://localhost:8081/api/franchises/{franchiseId}/branches` 

#### 🧪 Ejemplo de solicitud  
```url
  `http://localhost:8081/api/franchises/664b2a7d47b8a72fc9e4f2c3/branches
```
**Request Body:**
```json
{
  "name": "Sucursal Centro"
}

```
**Response**

**Code:** `201 Created`

```json
  {
      "id": "664b2a7d47b8a72fc9e4f2c3",
      "name": "ROOTT+CO",
      "branches": [
        { 
           "name": "Sucursal Centro",
           "products": []
        }
    ]
  }

```
---

### 📦  Agregar un nuevo producto a la sucursal 

Este endpoint permite agregar un nuevo producto a la sucursal de una franquicia proporcionando su nombre y stock en el cuerpo de la solicitud.

#### 📥 Parámetros

 **Remplazar** `{franchiseId}` por el ID de la franquicia y `{branchName}` por el nombre de la sucursal

**Método:** `POST`  
**URL:** `http://localhost:8081/api/franchises/{franchiseId}/branches/{branchName}/products` 

#### 🧪 Ejemplo de solicitud  
```url
  `http://localhost:8081/api/franchises/664b2a7d47b8a72fc9e4f2c3/Sucursal Centro/products
```

**Request body**
```json
{
    "name":"Camiseta X" ,
    "stock": 20
}

```

**Response**

**Code:** `201 Created`

```json
  {
      "id": "664b2a7d47b8a72fc9e4f2c3",
      "name": "ROOTT+CO",
      "branches": [
        { 
           "name": "Sucursal Centro",
           "products": [
              {
                 "name":"Camiseta X" ,
                 "stock": 20
              }
          ]
        }
    ]
  }

```
---

### 🗑️ Eliminar un producto a una sucursal  

Este endpoint permite eliminar un producto de la sucursal de una franquicia proporcionando su nuevo stock en el cuerpo de la solicitud.

#### 📥 Parámetros

 **Remplazar** `{franchiseId}` por el ID de la franquicia, `{branchName}` por el nombre de la sucursal y `{productName}` por el nombre del producto

**Método:** `DELETE`  
**URL:** `http://localhost:8081/api/franchises/{franchiseId}/branches/{branchName}/products/{productName}` 

#### 🧪 Ejemplo de solicitud  

```url

http://localhost:8081/api/franchises/664b2a7d47b8a72fc9e4f2c3/branches/Sucursal Centro/products/Camiseta X

```
**Response**

**Code:** `204 No Content`

---

### 🔄 Actualizar un Stock de un producto 

Este endpoint permite actualizar un stock de producto en la sucursal de una franquicia.

#### 📥 Parámetros

 **Remplazar** `{franchiseId}` por el ID de la franquicia, `{branchName}` por el nombre de la sucursal y `{productName}` por el nombre del producto


**Método:** `PUT`  
**URL:** `http://localhost:8081/api/franchises/{franchiseId}/branches/{branchName}/products/{productName}/stock` 

#### 🧪 Ejemplo de solicitud 
```url

http://localhost:8081/api/franchises/664b2a7d47b8a72fc9e4f2c3/branches/Sucursal Centro/products/Camiseta X/stock

```
**Request Body:**
```json
{
    "newStock": 100
}

```
**Response**

**Code:** `200 OK`

```json
  {
  "name": "Camiseta X",
  "stock": 100
}

```
---

### 🥇 Obtener el producto que más stock tiene por sucursal

Este endpoint permite mostrar el producto con mayor stock por sucursal dentro de una franquicia específica. 
Retorna un listado de productos que indican a qué sucursal pertenecen.

#### 📥 Parámetros

 **Remplazar** `{franchiseId}` por el ID de la franquicia 


**Método:** `GET`  
**URL:** `http://localhost:8081/api/franchises/{franchiseId}/products/top-stock"` 

#### 🧪 Ejemplo de solicitud 

```url

http://localhost:8081/api/franchises/680fb4c7dbcd1f7a78649310/product/top-stock

```
**Response**

**Code:** `200 OK`

```json
  [
  {
    "branchName": "Sucursal Centro",
    "product": {
      "name": "Camiseta X",
      "stock": 100
    }
  },
  {
    "branchName": "Sucursal Sur",
    "product": {
      "name": "Pantalon Z",
      "stock": 80
    }
  }
]

```
---

### ✏️ Actualizar el nombre de la franquicia

Este endpoint permite Actualizar el nombre de una franquicia proporcionando su nuevo nombre en el cuerpo de la solicitud.
 **Remplazar** `{franchiseId}` por el ID de la franquicia que se va a actualizar

**Método:** `PUT`  
**URL:** `http://localhost:8081/api/franchises/{franchiseId}` 

#### 🧪 Ejemplo de solicitud 
```url

http://localhost:8081/api/franchises/680fb4c7dbcd1f7a78649310

```
**Request Body:**
```json
{
    "newName": "Motorora"
}

```
**Response**

**Code:** `200 OK`

```json
{
  "id": "680fb4c7dbcd1f7a78649310",
  "newName": "Motorora"
}

```
---

###  📝 Actualizar el nombre de la sucursal

Este endpoint permite Actualizar el nombre de una sucursal proporcionando su nuevo nombre en el cuerpo de la solicitud.
#### 📥 Parámetros

 **Remplazar** `{franchiseId}` por el ID de la franquicia y `{branchName}` por el nombre de la sucursal que se va a actualizar

**Método:** `PUT`  
**URL:** `http://localhost:8081/api/franchises/{franchiseId}/branches/{branchName}/name` 

####  🧪 Ejemplo de solicitud 
```url

http://localhost:8081/api/franchises/680fb4c7dbcd1f7a78649310/branches/Sucursal Centro/name

```
**Request Body:**
```json
{
    "newName": "Sucursdal Centro 2"
}

```
**Response**

**Code:** `200 OK`

```json
{

  "newName": "Sucursdal Centro 2"
}

```
---

### ✏️  Actualizar el nombre del producto

Este endpoint permite Actualizar el nombre del producto proporcionando su nuevo nombre en el cuerpo de la solicitud.
#### 📥 Parámetros

 **Remplazar** `{franchiseId}` por el ID de la franquicia, `{branchName}` por el nombre de la sucursal que se va a actualizar y `{productName}` por el nombre del producto que se va a actualizar

**Método:** `PUT`  
**URL:** `http://localhost:8081/api/franchises/{franchiseId}/branches/{branchName}/products/{productName}/name` 

####  🧪 Ejemplo de solicitud 
```url

http://localhost:8081/api/franchises/680fb4c7dbcd1f7a78649310/branches/Sucursal Centro/products/Pantalo z/name

```

**Request Body:**
```json
{
    "newName": "Pantalo z-x"
}

```
**Response**

**Code:** `200 OK`

```json
{
  "newName": "Pantalo z-x"",
  "stock": 30
}

```
---

## ⚠️ Posibles errores por endpoint

### 🔧 `POST /api/franchises`

| Código | Error             | Causa posible                                      | Ejemplo de respuesta                                                                 |
|--------|-------------------|----------------------------------------------------|----------------------------------------------------------------------------------------|
| 400    | Datos inválidos   | El nombre de la franquicia es nulo o vacío         | `{ "error": "BAD_REQUEST", "message": "El nombre no puede estar vacío.", "status": 400 }` |
| 409    | Conflicto         | Ya existe una franquicia con ese nombre            | `{ "error": "CONFLICT", "message": "La franquicia ya existe.", "status": 409 }`          |

---

### 🔍 `GET /api/franchises`

| Código | Error                    | Causa posible                              | Ejemplo de respuesta                                                               |
|--------|--------------------------|--------------------------------------------|------------------------------------------------------------------------------------|
| 500    | Internal Server Error    | Error inesperado al obtener franquicias    | `{ "error": "INTERNAL_SERVER_ERROR", "message": "Error inesperado.", "status": 500 }` |

---

### 🔍 `GET /api/franchises/{franchiseId}`

| Código | Error             | Causa posible                              | Ejemplo de respuesta                                                               |
|--------|-------------------|--------------------------------------------|------------------------------------------------------------------------------------|
| 400    | ID inválido       | El formato del ID es inválido              | `{ "error": "BAD_REQUEST", "message": "ID inválido.", "status": 400 }`            |
| 404    | No encontrado     | La franquicia no existe                    | `{ "error": "NOT_FOUND", "message": "Franquicia no encontrada.", "status": 404 }` |

---

### 🔧 `POST /api/franchises/{franchiseId}/branches`

| Código | Error                        | Causa posible                                             | Ejemplo de respuesta                                                                          |
|--------|------------------------------|-----------------------------------------------------------|-----------------------------------------------------------------------------------------------|
| 400    | Nombre de sucursal inválido  | El nombre de la sucursal es vacío o nulo                  | `{ "error": "BAD_REQUEST", "message": "Nombre de sucursal no puede estar vacío.", "status": 400 }` |
| 404    | Franquicia no encontrada     | El ID de la franquicia no existe                          | `{ "error": "NOT_FOUND", "message": "Franquicia no encontrada.", "status": 404 }`             |
| 409    | Conflicto                    | Ya existe una sucursal con ese nombre en la franquicia    | `{ "error": "CONFLICT", "message": "Ya existe una sucursal con ese nombre.", "status": 409 }` |

---

### 🔧 `POST /api/franchises/{franchiseId}/branches/{branchName}/products`

| Código | Error                    | Causa posible                                     | Ejemplo de respuesta                                                                          |
|--------|--------------------------|---------------------------------------------------|-----------------------------------------------------------------------------------------------|
| 400    | Datos inválidos          | El nombre o el stock del producto es inválido     | `{ "error": "BAD_REQUEST", "message": "El nombre y el stock son obligatorios.", "status": 400 }` |
| 404    | Sucursal no encontrada   | La sucursal no existe en la franquicia            | `{ "error": "NOT_FOUND", "message": "Sucursal no encontrada.", "status": 404 }`              |
| 409    | Conflicto                | El producto ya existe en la sucursal              | `{ "error": "CONFLICT", "message": "Producto ya existe en la sucursal.", "status": 409 }`    |

---

### ❌ `DELETE /api/franchises/{franchiseId}/branches/{branchName}/products/{productName}`

| Código | Error                | Causa posible                                | Ejemplo de respuesta                                                           |
|--------|----------------------|----------------------------------------------|--------------------------------------------------------------------------------|
| 404    | No encontrado        | La franquicia, sucursal o producto no existen| `{ "error": "NOT_FOUND", "message": "Producto no encontrado.", "status": 404 }` |

---

### 🔄 `PUT /api/franchises/{franchiseId}/branches/{branchName}/products/{productName}/stock`

| Código | Error                | Causa posible                                | Ejemplo de respuesta                                                           |
|--------|----------------------|----------------------------------------------|--------------------------------------------------------------------------------|
| 400    | Stock inválido       | El nuevo stock es nulo o negativo            | `{ "error": "BAD_REQUEST", "message": "Stock inválido.", "status": 400 }`     |
| 404    | Producto no encontrado | El producto no existe en la sucursal       | `{ "error": "NOT_FOUND", "message": "Producto no encontrado.", "status": 404 }` |

---

### 📊 `GET /api/franchises/{franchiseId}/products/top-stock`

| Código | Error                    | Causa posible                            | Ejemplo de respuesta                                                               |
|--------|--------------------------|------------------------------------------|------------------------------------------------------------------------------------|
| 404    | Franquicia no encontrada | No existe la franquicia con ese ID       | `{ "error": "NOT_FOUND", "message": "Franquicia no encontrada.", "status": 404 }` |

---

### ✏️ `PUT /api/franchises/{franchiseId}`

| Código | Error                | Causa posible                              | Ejemplo de respuesta                                                                 |
|--------|----------------------|--------------------------------------------|--------------------------------------------------------------------------------------|
| 400    | Nombre inválido      | El nuevo nombre de la franquicia es inválido | `{ "error": "BAD_REQUEST", "message": "El nombre no puede estar vacío.", "status": 400 }` |
| 404    | No encontrado        | La franquicia no existe                    | `{ "error": "NOT_FOUND", "message": "Franquicia no encontrada.", "status": 404 }`     |

---

### ✏️ `PUT /api/franchises/{franchiseId}/branches/{branchName}/name`

| Código | Error                | Causa posible                                  | Ejemplo de respuesta                                                                     |
|--------|----------------------|------------------------------------------------|------------------------------------------------------------------------------------------|
| 400    | Nombre inválido      | El nuevo nombre de la sucursal es vacío o igual| `{ "error": "BAD_REQUEST", "message": "El nuevo nombre es inválido.", "status": 400 }`   |
| 404    | Sucursal no encontrada | No existe la sucursal a renombrar            | `{ "error": "NOT_FOUND", "message": "Sucursal no encontrada.", "status": 404 }`          |

---

### ✏️ `PUT /api/franchises/{franchiseId}/branches/{branchName}/products/{productName}/name`

| Código | Error                | Causa posible                                  | Ejemplo de respuesta                                                                     |
|--------|----------------------|------------------------------------------------|------------------------------------------------------------------------------------------|
| 400    | Nombre inválido      | El nuevo nombre del producto es vacío o inválido| `{ "error": "BAD_REQUEST", "message": "El nombre no puede estar vacío.", "status": 400 }` |
| 404    | Producto no encontrado | No existe el producto a renombrar            | `{ "error": "NOT_FOUND", "message": "Producto no encontrado.", "status": 404 }`          |

 



