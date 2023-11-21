package com.example.demo1;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ViewAgregarHotelController {

    @FXML
    private TextField txtIdHotel;

    @FXML
    private TextField txtNumHabHotel;

    @FXML
    private TextField txtIdDireccionHot;

    @FXML
    private TextField txtDescHotel;

    @FXML
    private TextField txtNombreHotel;

    @FXML
    private TextField txtTipoIdHot;

    private ViewHotelAdminController  ventanaPrincipalController;

    public void setVentanaPrincipalController(ViewHotelAdminController controller) {
        this.ventanaPrincipalController = controller;
    }

    @FXML
    void agregarProducto(ActionEvent event) {
        // Obtener datos ingresados por el usuario
        String nombre = txtNombreHotel.getText();
        Integer IdDireccion = Integer.parseInt(txtIdDireccionHot.getText());
        Integer ID = Integer.parseInt(txtIdHotel.getText());
        String descripcionHotel = txtDescHotel.getText();
        Integer numHabitaciones = Integer.parseInt(txtNumHabHotel.getText());
        Integer iDTipoHotel = Integer.parseInt(txtTipoIdHot.getText());

        // Otros campos...

        // Realizar la conexión y la inserción
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "BRITO", "123456789")) {
            String query = "INSERT INTO HOTEL (ID, NOMBRE, DESCRIPCION, NUMEROHABITACIONES, TIPO_HOTEL_ID, DIRECCION_ID) VALUES (?, ?, ?, ?, ?, ? )";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, ID);  // Cambié setString a setInt
                preparedStatement.setString(2, nombre);
                preparedStatement.setString(3, descripcionHotel);
                preparedStatement.setInt(4, numHabitaciones);
                preparedStatement.setInt(5, iDTipoHotel);
                preparedStatement.setInt(6, IdDireccion);

                preparedStatement.executeUpdate();

                limpiarTextFields();
                ventanaPrincipalController.cargarTodosLosHoteles();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar errores de conexión o inserción
        }
    }

    private void limpiarTextFields() {
        txtNombreHotel.clear();
        txtIdDireccionHot.clear();
        txtIdHotel.clear();
        txtDescHotel.clear();
        txtNumHabHotel.clear();
        txtTipoIdHot.clear();
    }

}

