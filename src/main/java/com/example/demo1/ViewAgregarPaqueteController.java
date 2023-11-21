package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ViewAgregarPaqueteController {

    @FXML
    private TextField txtDescrPaq;

    @FXML
    private TextField txtTipoPaq;

    @FXML
    private TextField txtPrecioPaq;

    @FXML
    private TextField txtIdPaquete;

    private ViewPaqueteAdminController  ventanaPrincipalController;

    public void setVentanaPrincipalController(ViewPaqueteAdminController controller) {
        this.ventanaPrincipalController = controller;
    }

    @FXML
    void agregarPaquete(ActionEvent event) {
// Obtener datos ingresados por el usuario
        String descripcion = txtDescrPaq.getText();
        float precio = Float.parseFloat(txtPrecioPaq.getText());
        Integer ID = Integer.parseInt(txtIdPaquete.getText());
        String tipo = txtTipoPaq.getText();

        // Otros campos...

        // Realizar la conexión y la inserción
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "BRITO", "123456789")) {
            String query = "INSERT INTO PAQUETE (ID, DESCRIPCION, PRECIO, TIPO) VALUES (?, ?, ?, ? )";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, ID);
                preparedStatement.setString(2, descripcion);
                preparedStatement.setFloat(3, precio);
                preparedStatement.setString(4, tipo);


                preparedStatement.executeUpdate();

                limpiarTextFields();
                ventanaPrincipalController.cargarTodosLosPaquetes();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar errores de conexión o inserción
        }
    }

    private void limpiarTextFields() {
        txtDescrPaq.clear();
        txtPrecioPaq.clear();
        txtIdPaquete.clear();
        txtTipoPaq.clear();
    }
}
