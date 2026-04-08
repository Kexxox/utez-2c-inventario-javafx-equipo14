package com.example.tienditalocal.services;

import com.example.tienditalocal.models.Producto;
import com.example.tienditalocal.repositories.ProductoRepository;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.ArrayList;

public class ProductoService {
    private final ProductoRepository repository = new ProductoRepository();


    public void cargarDatos(ObservableList<Producto> lista) {
        lista.setAll(repository.loadAll());
    }

    public void registrarProducto(Producto nuevo, ObservableList<Producto> listaActual) throws IllegalArgumentException {
        if (nuevo.getNombre().length() < 3) {
            throw new IllegalArgumentException("El nombre debe tener al menos 3 caracteres.");
        }

        if (listaActual.stream().anyMatch(p -> p.getCodigo().equalsIgnoreCase(nuevo.getCodigo()))) {
            throw new IllegalArgumentException("El código '" + nuevo.getCodigo() + "' ya existe.");
        }

        if (nuevo.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0.");
        }
        if (nuevo.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }

        listaActual.add(nuevo);
        repository.saveAll(new ArrayList<>(listaActual));


    }

    public void eliminarProducto(Producto producto, ObservableList<Producto> listaActual) {
        if (producto != null) {
            listaActual.remove(producto);
            repository.saveAll(new ArrayList<>(listaActual));
        }
    }
    public void configurarFiltro(FilteredList<Producto> filteredData, String textoBusqueda) {

        filteredData.setPredicate(producto -> {
            if (textoBusqueda == null || textoBusqueda.isEmpty()) {
                return true;
            }

            String term = textoBusqueda.toLowerCase();

            return producto.getNombre().toLowerCase().contains(term) ||
                    producto.getCodigo().toLowerCase().contains(term);
        });
    }
    public void actualizarProducto(Producto existente, String nombre, String precioStr, int stock, String categoria, ObservableList<Producto> listaActual) throws Exception {
        if (nombre.length() < 3) {
            throw new Exception("El nombre debe tener al menos 3 caracteres.");
        }

        double precio;
        try {
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException e) {
            throw new Exception("El precio debe ser un número válido.");
        }

        if (precio <= 0 || stock < 0) {
            throw new Exception("El precio debe ser > 0 y el stock >= 0.");
        }

        existente.setNombre(nombre);
        existente.setPrecio(precio);
        existente.setStock(stock);
        existente.setCategoria(categoria);

        repository.saveAll(new ArrayList<>(listaActual));
    }
}
