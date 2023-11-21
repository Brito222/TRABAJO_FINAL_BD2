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

public class ViewEditarPaqueteController {

    @FXML
    private TextField txtDescrPaq;

    @FXML
    private TextField txtTipoPaq;

    @FXML
    private TextField txtPrecioPaq;

    @FXML
    private TextField txtIdPaquete;

    @FXML
    private Button btnEditar;

    @FXML
    void EditarPaquete(ActionEvent event) {
        guardarCambios(event);
    }

    private String paqueteId;  // Para almacenar el ID del vehículo
    private ViewPaqueteAdminController ventanaPrincipalController;

    public void inicializarDatos(String id, String descripcion, String precio, String tipo, ViewPaqueteAdminController controller) {
        // Inicializa los campos de entrada con los datos del vehículo
        paqueteId = id;
        txtDescrPaq.setText(descripcion);
        txtPrecioPaq.setText(precio);
        txtTipoPaq.setText(tipo);


        // Inicializa otros campos de entrada según sea necesario

        // Guarda una referencia al controlador principal
        this.ventanaPrincipalController = controller;
    }

    @FXML
    void guardarCambios(ActionEvent event) {
        // Obtener los nuevos datos ingresados por el usuario
        String tipo = txtTipoPaq.getText();
        String descripcion = txtDescrPaq.getText();
        Float precio = Float.parseFloat(txtPrecioPaq.getText());


        // Realizar la conexión y la actualización
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "BRITO", "123456789")) {
            String query = "UPDATE PAQUETE SET  DESCRIPCION = ?, PRECIO = ?,  TIPO = ? WHERE ID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, descripcion);
                preparedStatement.setFloat(2, precio);
                preparedStatement.setString(3, tipo);

                preparedStatement.setString(4, paqueteId);

                preparedStatement.executeUpdate();

                // Informar al controlador principal para recargar los vehículos
                ventanaPrincipalController.cargarTodosLosPaquetes();

                // Cerrar la ventana de edición
                Stage stage = (Stage) txtDescrPaq.getScene().getWindow();
                stage.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar errores de conexión o actualización
        }
    }

}
