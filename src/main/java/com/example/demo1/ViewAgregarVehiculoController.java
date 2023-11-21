package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ViewAgregarVehiculoController {

    @FXML
    private TextField txtPrecioVeh;

    @FXML
    private TextField txtModeloVeh;

    @FXML
    private TextField txtIdTipo;

    @FXML
    private TextField txtPlacaVeh;

    @FXML
    private TextField txtIdVeh;

    @FXML
    private TextField txtIdMarca;

    @FXML
    private TextField txtKilVeh;

    @FXML
    private TextField txtIdGama;

    private ViewAutomovilAdminController  ventanaPrincipalController;

    public void setVentanaPrincipalController(ViewAutomovilAdminController controller) {
        this.ventanaPrincipalController = controller;
    }

    @FXML
    void agregarVehiculo(ActionEvent event) {
        // Obtener datos ingresados por el usuario
        String placa = txtPlacaVeh.getText();
        Integer modelo = Integer.parseInt(txtModeloVeh.getText());
        Integer ID = Integer.parseInt(txtIdVeh.getText());
        Integer iDMarca = Integer.parseInt(txtIdMarca.getText());
        Integer iDTipo = Integer.parseInt(txtIdTipo.getText());
        Integer kilometraje = Integer.parseInt(txtKilVeh.getText());
        Integer iDGama = Integer.parseInt(txtIdGama.getText());
        Float precio = Float.parseFloat(txtPrecioVeh.getText());
        // Otros campos...

        // Realizar la conexión y la inserción
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "BRITO", "123456789")) {
            String query = "INSERT INTO AUTOMOVIL (ID, PLACA, MODELO, PRECIO, KILOMETRAJE, GAMA_ID, MARCA_ID, TIPO_AUTOMOVIL_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ? )";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, ID);
                preparedStatement.setString(2, placa);
                preparedStatement.setInt(3, modelo);
                preparedStatement.setFloat(4, precio);
                preparedStatement.setInt(5, kilometraje);
                preparedStatement.setInt(6, iDGama);
                preparedStatement.setInt(7, iDMarca);
                preparedStatement.setInt(8, iDTipo);

                preparedStatement.executeUpdate();

                limpiarTextFields();
                ventanaPrincipalController.cargarTodosLosVehiculos();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar errores de conexión o inserción
        }
    }

    private void limpiarTextFields() {
        txtPlacaVeh.clear();
        txtModeloVeh.clear();
        txtIdVeh.clear();
        txtIdMarca.clear();
        txtIdTipo.clear();
        txtKilVeh.clear();
        txtIdGama.clear();
        txtPrecioVeh.clear();
    }
}
