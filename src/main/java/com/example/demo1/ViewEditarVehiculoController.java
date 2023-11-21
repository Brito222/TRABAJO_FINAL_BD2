package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ViewEditarVehiculoController {

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

    @FXML
    void editarVehiculo(ActionEvent event) {
        guardarCambios(event);
    }

    private String vehiculoId;  // Para almacenar el ID del vehículo
    private ViewAutomovilAdminController ventanaPrincipalController;

    public void inicializarDatos(String id, String modelo, String precio, String marcaId, String kilometraje, String gamaId, String placa, String tipo, ViewAutomovilAdminController controller) {
        // Inicializa los campos de entrada con los datos del vehículo
        vehiculoId = id;
        txtModeloVeh.setText(modelo);
        txtPrecioVeh.setText(precio);
        txtPlacaVeh.setText(placa);
        txtIdTipo.setText(tipo);
        txtKilVeh.setText(kilometraje);
        txtIdMarca.setText(marcaId);
        txtIdGama.setText(gamaId);



        // Inicializa otros campos de entrada según sea necesario

        // Guarda una referencia al controlador principal
        this.ventanaPrincipalController = controller;
    }

    @FXML
    void guardarCambios(ActionEvent event) {
        // Obtener los nuevos datos ingresados por el usuario
        String placa = txtPlacaVeh.getText();
        Integer modelo = Integer.parseInt(txtModeloVeh.getText());
        Float precio = Float.parseFloat(txtPrecioVeh.getText());
        Integer tipo = Integer.parseInt(txtIdTipo.getText());

        // Realizar la conexión y la actualización
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "BRITO", "123456789")) {
            String query = "UPDATE AUTOMOVIL SET PLACA = ?, MODELO = ?, PRECIO = ?, KILOMETRAJE = ?, GAMA_ID = ?, MARCA_ID = ?, TIPO_AUTOMOVIL_ID = ? WHERE ID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, placa);
                preparedStatement.setInt(2, modelo);
                preparedStatement.setFloat(3, precio);
                preparedStatement.setInt(4, Integer.parseInt(txtKilVeh.getText()));
                preparedStatement.setInt(5, Integer.parseInt(txtIdGama.getText()));
                preparedStatement.setInt(6, Integer.parseInt(txtIdMarca.getText()));
                preparedStatement.setInt(7, Integer.parseInt(txtIdTipo.getText()));
                preparedStatement.setString(8, vehiculoId);

                preparedStatement.executeUpdate();

                // Informar al controlador principal para recargar los vehículos
                ventanaPrincipalController.cargarTodosLosVehiculos();

                // Cerrar la ventana de edición
                Stage stage = (Stage) txtModeloVeh.getScene().getWindow();
                stage.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar errores de conexión o actualización
        }
    }
}
