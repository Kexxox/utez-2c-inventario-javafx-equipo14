# utez-2c-inventario-javafx-equipo14

Aplicacion de escritorio en JavaFX para administrar el inventario de una tienda local.
Guarda los datos en un archivo CSV sin necesidad de internet ni base de datos.

---

## Descripcion

El sistema permite:
- Agregar productos nuevos
- Ver todos los productos en una tabla
- Editar un producto existente
- Eliminar productos (con confirmacion)
- Buscar productos por nombre o codigo en tiempo real
- Ordenar la lista por nombre, precio o stock

Cada producto tiene: codigo, nombre, precio, stock y categoria.

---

## Como ejecutar

Necesitas tener instalado **Java 21** y **Temurin 17**.

```

```

---

## Archivo de datos

Los productos se guardan en el archivo:

```
Data/productos.csv
```

El formato de cada linea es:

```
codigo,nombre,precio,stock,categoria
```

Ejemplo:
```
00099,jabon,23.0,3,limpieza
```

Si el archivo no existe, el programa lo crea automaticamente al iniciar.

---

## Estructura del proyecto

```
src/
  main/
    java/
      com/example/tienditalocal/
        HelloApplication.java      <- clase principal
        Models/
          Producto.java            <- clase modelo
        repositories/
          ProductoRepository.java   <- lee y escribe el archivo
        services/
          ProductoService.java      <- logica del negocio
        controllers/
          AppController.java       <- logica de los botones
    resources/
      views/
        app-view.fxml              <- vista principal o la tabla
data/
  productos.csv                    <- archivo de datos
```

---

## Validaciones

- Ningun campo puede estar vacio
- El nombre necesita al menos 3 caracteres
- El precio debe ser mayor a 0
- El stock no puede ser negativo
- El codigo no se puede repetir

---

## Tecnologias

- Java 21
- Temurin 17
