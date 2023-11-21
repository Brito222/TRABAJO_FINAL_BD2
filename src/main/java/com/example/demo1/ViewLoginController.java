package com.example.demo1;


import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class ViewLoginController {

    @FXML
    private ToggleGroup SelectTipUsuario;

    @FXML
    private TextField txtContrasena;

    @FXML
    private Button loginButton;

    @FXML
    private TextField txtUsuario;

    @FXML
    private void inicioSesion() {

        String username = txtUsuario.getText();
        String password = txtContrasena.getText();
        Toggle selectedToggle = SelectTipUsuario.getSelectedToggle();
        RadioButton selectedRadio = (RadioButton) selectedToggle;
        String selectedValue = selectedRadio.getText();
        System.out.println("Selected Value: " + selectedValue);

        if (selectedValue.equalsIgnoreCase("Administrador")) {
            if (username.equals("admin") && password.equals("contrasena")) {
                System.out.println("inicio de sesion usuario exitoso");
                openNavegacionView();
            } else {
                System.out.println("datos no coinciden");
            }
        } else {
            if (selectedValue.equalsIgnoreCase("usuario")) {
                if (username.equals("admin") && password.equals("contrasena")) {
                    System.out.println("inicio de sesion usuario exitoso");
                    openNavegacionViewUser();
                } else {
                    System.out.println("datos no coinciden");
                }
            }
        }
    }



    private void openNavegacionView() {
        try {
            // Cargar la nueva ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewNavegacion.fxml"));
            Parent root = loader.load();

            // Crear la escena y la etapa
            Stage stage = new Stage();
            stage.setTitle("Home");

            // Configurar el contenedor principal para usar su tamaño preferido
            Region region = (Region) root;

            // Configurar la escena
            Scene scene = new Scene(root);

            // Establecer la escena y mostrar la nueva ventana
            stage.setScene(scene);

            // Configurar el contenedor principal para usar su tamaño preferido después de mostrar la ventana
            stage.setOnShown(event -> {
                region.prefWidthProperty().bind(stage.widthProperty());
                region.prefHeightProperty().bind(stage.heightProperty());
            });

            // Mostrar la nueva ventana
            stage.show();

            // Cerrar la ventana actual
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void openNavegacionViewUser() {
        try {
            // Cargar la nueva ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewNavegacionUser.fxml"));
            Parent root = loader.load();

            // Crear la escena y la etapa
            Stage stage = new Stage();
            stage.setTitle("Home");
            stage.setScene(new Scene(root, 800, 600));

            // Mostrar la nueva ventana
            stage.show();

            // Cerrar la ventana actual
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

