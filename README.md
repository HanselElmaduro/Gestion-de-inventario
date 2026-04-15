[README.md](https://github.com/user-attachments/files/26731686/README.md)
# Sistema de Gestión de Almacén

Sistema de gestión completo con manejo de usuarios y productos para almacén,
desarrollado en Java con Swing y conexión a base de datos MySQL remota.

---

## Arquitectura del Proyecto

```
AlmacenSystem/
├── pom.xml                          ← Configuración Maven (descarga MySQL connector)
└── src/main/java/com/almacen/
    ├── Main.java                    ← Punto de entrada
    ├── model/
    │   ├── Usuario.java             ← Modelo de usuario (Encapsulamiento)
    │   └── Producto.java            ← Modelo de producto (Encapsulamiento)
    ├── dao/
    │   ├── ConexionDB.java          ← Patrón SINGLETON (conexión BD)
    │   ├── UsuarioDAO.java          ← Patrón DAO (CRUD usuarios)
    │   └── ProductoDAO.java         ← Patrón DAO (CRUD productos)
    ├── util/
    │   └── Estilos.java             ← Helper de diseño visual
    └── view/
        ├── BaseFrame.java           ← Clase base abstracta (Abstracción + Herencia)
        ├── LoginFrame.java          ← Pantalla de login
        ├── RegistroFrame.java       ← Formulario de registro
        ├── MainFrame.java           ← Pantalla principal (dashboard)
        ├── UsuariosFrame.java       ← Listado y gestión de usuarios
        ├── NuevoUsuarioFrame.java   ← Formulario nuevo usuario
        ├── EditarUsuarioFrame.java  ← Formulario editar/eliminar usuario
        ├── ProductosFrame.java      ← Listado y gestión de productos
        ├── NuevoProductoFrame.java  ← Formulario nuevo producto
        └── EditarProductoFrame.java ← Formulario editar/eliminar producto
```

---

## Pilares de POO Aplicados

| Pilar | Dónde se aplica |
|-------|----------------|
| **Abstracción** | `BaseFrame` (clase abstracta con `initComponents()` y `configurarLayout()` abstractos) |
| **Encapsulamiento** | `Usuario.java` y `Producto.java` (atributos privados con getters/setters) |
| **Herencia** | `LoginFrame`, `MainFrame`, `ProductosFrame`, `UsuariosFrame` extienden `BaseFrame`; `PrecioRenderer`, `StockRenderer` extienden `DefaultTableCellRenderer` |
| **Polimorfismo** | `initComponents()` y `configurarLayout()` son sobreescritos por cada Frame; Renderers de tabla personalizan `getTableCellRendererComponent()` |

---

## Patrones de Diseño Aplicados

### 1. SINGLETON — `ConexionDB.java`
Garantiza una única instancia de conexión a la base de datos:
```java
public static synchronized ConexionDB getInstancia() {
    if (instancia == null) instancia = new ConexionDB();
    return instancia;
}
```

### 2. DAO (Data Access Object) — `UsuarioDAO.java`, `ProductoDAO.java`
Separa la lógica SQL de la presentación. Las vistas llaman `usuarioDAO.listar()` sin conocer SQL.

### 3. MVC (Model-View-Controller)
- **Model**: `model/Usuario.java`, `model/Producto.java`
- **View**: Todos los frames en `view/`
- **Controller**: Lógica en los listeners de botones dentro de cada frame

### 4. OBSERVER (simplificado) — `cargarDatos()` en frames
Cada vez que hay una inserción, actualización o eliminación, se llama `cargarDatos()` que refresca la vista automáticamente desde la BD, manteniendo la UI siempre sincronizada.

---

## Instrucciones de Ejecución

### Requisitos
- Java JDK 11 o superior
- Maven 3.6 o superior
- Conexión a internet (para descargar dependencias y conectar a BD)

### Pasos

**1. Compilar el proyecto:**
```bash
cd AlmacenSystem
mvn clean package -q
```

**2. Ejecutar:**
```bash
java -jar target/AlmacenSystem-1.0-SNAPSHOT.jar
```

### En Eclipse/IntelliJ
1. Importar como proyecto Maven existente
2. Esperar que descargue dependencias automáticamente
3. Ejecutar `Main.java`

---

## Base de Datos

**Conexión remota configurada:**
```
Host:     almacenitla-db-itla-3837.e.aivencloud.com:25037
Database: almacenitlafinal
User:     avnadmin
```

**Tablas:**
```sql
-- Usuarios
CREATE TABLE usuarios (
    idUser    INT PRIMARY KEY AUTO_INCREMENT,
    UserName  VARCHAR(140),
    Nombre    VARCHAR(140),
    Apellido  VARCHAR(140),
    Telefono  VARCHAR(140),
    Email     VARCHAR(140),
    Password  VARCHAR(140)
);

-- Productos
CREATE TABLE productos (
    idProducto        INT PRIMARY KEY AUTO_INCREMENT,
    NombreProducto    VARCHAR(140),
    MarcaProducto     VARCHAR(140),
    CategoriaProducto VARCHAR(140),
    PrecioProducto    INT,
    StockProducto     INT
);
```

---

## Funcionalidades

### Gestión de Usuarios
- Login con validación de campos
- Contraseña oculta (JPasswordField)
- Registro con todos los campos validados
- Error específico por campo faltante
- Verificación de contraseñas coincidentes
- Verificación de nombre de usuario único
- Listado de todos los usuarios
- Editar datos de usuario
- Eliminar usuario con confirmación
- Actualización automática de la lista
- Cerrar sesión

### Gestión de Productos
- Listado de todos los productos con colores por stock
- Clic en producto abre formulario de edición
- Crear nuevo producto con validación
- Guardar cambios → cierra y actualiza lista
- Eliminar con confirmación → cierra y actualiza lista
- Precios formateados en RD$
- Stock coloreado (verde/naranja/rojo según cantidad)

---

*Desarrollado con Java 11 + Swing + MySQL*
