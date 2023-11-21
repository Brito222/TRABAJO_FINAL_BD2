package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ViewEditarHotelController {

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
    private Button btnEditar;

    @FXML
    private TextField txtTipoIdHot;

    @FXML
    void editarProducto(ActionEvent event) {
        guardarCambios(event);
    }


    private String hotelId;  // Para almacenar el ID del vehículo
    private ViewHotelAdminController ventanaPrincipalController;

    public void inicializarDatos(String id, String nombre, String descripcion, String numHabitaciones, String tipo, String direccion, ViewHotelAdminController controller) {
        // Inicializa los campos de entrada con los datos del vehículo
        hotelId = id;
        txtDescHotel.setText(descripcion);
        txtTipoIdHot.setText(tipo);
        txtIdDireccionHot.setText(direccion);
        txtNumHabHotel.setText(numHabitaciones);
        txtNombreHotel.setText(nombre);


        // Inicializa otros campos de entrada según sea necesario

        // Guarda una referencia al controlador principal
        this.ventanaPrincipalController = controller;
    }

    @FXML
    void guardarCambios(ActionEvent event) {
        // Obtener los nuevos datos ingresados por el usuario
        String nombre = txtNombreHotel.getText();
        String descripcion = txtDescHotel.getText();
        Integer tipo = Integer.parseInt(txtTipoIdHot.getText());
        Integer direccion = Integer.parseInt(txtIdDireccionHot.getText());
        Integer numHab = Integer.parseInt(txtNumHabHotel.getText());

        // Realizar la conexión y la actualización
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "BRITO", "123456789")) {
            String query = "UPDATE HOTEL SET NOMBRE = ?, DESCRIPCION = ?, NUMEROHABITACIONES = ?, TIPO_HOTEL_ID = ?, DIRECCION_ID = ? WHERE ID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, nombre);
                preparedStatement.setString(2, descripcion);
                preparedStatement.setInt(3, numHab);
                preparedStatement.setInt(4, tipo);
                preparedStatement.setInt(5, direccion);

                preparedStatement.setString(6, hotelId);

                preparedStatement.executeUpdate();

                // Informar al controlador principal para recargar los vehículos
                ventanaPrincipalController.cargarTodosLosHoteles();

                // Cerrar la ventana de edición
                Stage stage = (Stage) txtNombreHotel.getScene().getWindow();
                stage.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar errores de conexión o actualización
        }
    }

}

