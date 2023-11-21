package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ViewEditarArticuloController {

    @FXML
    private TextField txtIdArt;

    @FXML
    private Button btnEditarArticulo;

    @FXML
    private TextField txtIdTipo;

    @FXML
    private TextField txtDescArt;

    @FXML
    private TextField txtNombreArt;

    @FXML
    private TextField txtUniArt;

    @FXML
    private TextField txtPrecioArt;

    @FXML
    void editarArticulo(ActionEvent event) {
        guardarCambios(event);
    }

    private String articuloId;  // Para almacenar el ID del vehículo
    private ViewArticuloAdminController ventanaPrincipalController;

    public void inicializarDatos(String id, String nombre, String precio, String tipo, String descripcion, String unidades, ViewArticuloAdminController controller) {
        // Inicializa los campos de entrada con los datos del vehículo
        articuloId = id;
        txtDescArt.setText(descripcion);
        txtPrecioArt.setText(precio);
        txtUniArt.setText(unidades);
        txtIdTipo.setText(tipo);
        txtNombreArt.setText(nombre);


        // Inicializa otros campos de entrada según sea necesario

        // Guarda una referencia al controlador principal
        this.ventanaPrincipalController = controller;
    }

    @FXML
    void guardarCambios(ActionEvent event) {
        // Obtener los nuevos datos ingresados por el usuario
        String nombre = txtNombreArt.getText();
        String descripcion = txtDescArt.getText();
        Float precio = Float.parseFloat(txtPrecioArt.getText());
        Integer tipo = Integer.parseInt(txtIdTipo.getText());
        Integer unidades = Integer.parseInt(txtUniArt.getText());

        // Realizar la conexión y la actualización
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "BRITO", "123456789")) {
            String query = "UPDATE ARTICULO SET NOMBRE = ?, DESCRIPCION = ?, PRECIO = ?, UNIDADES = ?, TIPO_ID = ? WHERE ID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, nombre);
                preparedStatement.setString(2, descripcion);
                preparedStatement.setFloat(3, precio);
                preparedStatement.setInt(4, Integer.parseInt(txtUniArt.getText()));
                preparedStatement.setInt(5, Integer.parseInt(txtIdTipo.getText()));

                preparedStatement.setString(6, articuloId);

                preparedStatement.executeUpdate();

                // Informar al controlador principal para recargar los vehículos
                ventanaPrincipalController.cargarTodosLosArticulos();

                // Cerrar la ventana de edición
                Stage stage = (Stage) txtDescArt.getScene().getWindow();
                stage.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar errores de conexión o actualización
        }
    }
}
