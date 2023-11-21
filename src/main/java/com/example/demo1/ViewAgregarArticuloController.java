package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ViewAgregarArticuloController {

    @FXML
    private TextField txtIdArt;

    @FXML
    private Button btnAgregarArticulo;

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

    private ViewArticuloAdminController  ventanaPrincipalController;

    public void setVentanaPrincipalController(ViewArticuloAdminController controller) {
        this.ventanaPrincipalController = controller;
    }

    @FXML
    void agregarArticulo(ActionEvent event) {
        // Obtener datos ingresados por el usuario
        String nombre = txtNombreArt.getText();
        Integer unidades = Integer.parseInt(txtUniArt.getText());
        Integer ID = Integer.parseInt(txtIdArt.getText());
        String descripcionArti = txtDescArt.getText();
        Float precioArt = Float.parseFloat(txtPrecioArt.getText());
        Integer iDTipoArt = Integer.parseInt(txtIdTipo.getText());

        // Otros campos...

        // Realizar la conexión y la inserción
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "BRITO", "123456789")) {
            String query = "INSERT INTO ARTICULO (ID, NOMBRE, DESCRIPCION, PRECIO, UNIDADES, TIPO_ID) VALUES (?, ?, ?, ?, ?, ? )";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, ID);  // Cambié setString a setInt
                preparedStatement.setString(2, nombre);
                preparedStatement.setString(3, descripcionArti);
                preparedStatement.setFloat(4, precioArt);
                preparedStatement.setInt(5, unidades);
                preparedStatement.setInt(6, iDTipoArt);

                preparedStatement.executeUpdate();

                limpiarTextFields();
                ventanaPrincipalController.cargarTodosLosArticulos();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar errores de conexión o inserción
        }
    }

    private void limpiarTextFields() {
        txtNombreArt.clear();
        txtUniArt.clear();
        txtIdArt.clear();
        txtDescArt.clear();
        txtPrecioArt.clear();
        txtIdTipo.clear();
    }
}


