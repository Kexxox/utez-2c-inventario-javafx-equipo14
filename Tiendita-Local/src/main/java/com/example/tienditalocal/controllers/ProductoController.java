package com.example.tienditalocal.controllers;

import com.example.tienditalocal.models.Producto;
import com.example.tienditalocal.repositories.ProductoRepository;
import com.example.tienditalocal.services.ProductoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProductoController {
    @FXML
    private TableView<Producto> tableProductos;
    @FXML private TableColumn<Producto, String> colCodigo, colNombre, colCat;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TableColumn<Producto, Integer> colStock;

    @FXML private TextField txtCodigo, txtNombre, txtPrecio, txtStock, txtCategoria, txtBusqueda;
    @FXML private Label lblMsg;
    @FXML
    private ComboBox<String> cmbCategoria;

    private ObservableList<Producto> masterData = FXCollections.observableArrayList();
    private ProductoRepository repository = new ProductoRepository();
    private ProductoService productoService = new ProductoService();


    @FXML
    public void initialize() {

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colCat.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        cmbCategoria.setItems(FXCollections.observableArrayList(
                "Lácteos", "Limpieza", "Frutas y Verduras", "Carnes"
        ));


        productoService.cargarDatos(masterData);


        FilteredList<Producto> filteredData = new FilteredList<>(masterData, p -> true);


        SortedList<Producto> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableProductos.comparatorProperty());

        tableProductos.setItems(sortedData);

        txtBusqueda.textProperty().addListener((obs, oldVal, newVal) -> {
            productoService.configurarFiltro(filteredData, newVal);
        });


        tableProductos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtNombre.setText(newSelection.getNombre());
                txtCodigo.setText(newSelection.getCodigo());
                txtPrecio.setText(String.valueOf(newSelection.getPrecio()));
                txtStock.setText(String.valueOf(newSelection.getStock()));
                cmbCategoria.setValue(newSelection.getCategoria());
            }
        });
    }

    @FXML
    private void onAgregar() {
        try {

            if (txtCodigo.getText().isEmpty() || txtNombre.getText().isEmpty() ||
                    txtPrecio.getText().isEmpty() || cmbCategoria.getValue() == null) {
                mostrarAlerta("Error", "Todos los campos son obligatorios.");
                return;
            }

            Producto p = new Producto(
                    txtCodigo.getText(),
                    txtNombre.getText(),
                    Double.parseDouble(txtPrecio.getText()),
                    Integer.parseInt(txtStock.getText()),
                    cmbCategoria.getValue()
            );

            productoService.registrarProducto(p, masterData);


            limpiarCampos();
            lblMsg.setText("Producto agregado con éxito.");

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "Precio y Stock deben ser valores numéricos válidos.");
        } catch (IllegalArgumentException e) {

            mostrarAlerta("Error de validación", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta("Error inesperado", "Ocurrió un error al guardar: " + e.getMessage());
        }
    }

    @FXML
    private void onEliminar() {

        Producto seleccionado = tableProductos.getSelectionModel().getSelectedItem();


        if (seleccionado == null) {
            mostrarAlerta("Atención", "Selecciona un producto de la tabla.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText("¿Borrar producto: " + seleccionado.getNombre() + "?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {

                productoService.eliminarProducto(seleccionado, masterData);

                lblMsg.setText("Producto eliminado con éxito.");
            } catch (Exception e) {
                mostrarAlerta("Error", "No se pudo eliminar el producto: " + e.getMessage());
            }
        }
    }
    @FXML
    private void onEditar() {
        Producto seleccionado = tableProductos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta("Atención", "Selecciona un producto de la tabla para editar.");
            return;
        }

        try {

            if (txtNombre.getText().isEmpty() || txtPrecio.getText().isEmpty()) {
                mostrarAlerta("Error", "Los campos no pueden estar vacíos.");
                return;
            }


            productoService.actualizarProducto(seleccionado,txtCodigo.getText(),txtNombre.getText()
                    ,txtPrecio.getText(),
                    Integer.parseInt(txtStock.getText()),
                    cmbCategoria.getValue(),masterData);


            tableProductos.refresh();
            limpiarCampos();
            txtCodigo.setEditable(true);
            lblMsg.setText("Producto actualizado con éxito.");

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El stock debe ser un número entero.");
        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage());
        }
    }
    @FXML
    private void onLimpiar (){
        Producto seleccionado = tableProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Atención", "Selecciona un producto de la tabla para editar.");
            return;
        }
        tableProductos.getSelectionModel().clearSelection();
        limpiarCampos();

    }
    private void limpiarCampos() {
        txtCodigo.clear();
        txtNombre.clear();
        txtPrecio.clear();
        txtStock.clear();
        cmbCategoria.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

