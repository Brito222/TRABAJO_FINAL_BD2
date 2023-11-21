package com.example.demo1;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;


public class ViewNavegacionController {

    @FXML
    private Button hotelesButton;

    @FXML
    private Button vehiculosButton;

    @FXML
    private Button articulosButton;

    @FXML
    private Button volverButton;

    @FXML
    private Button paquetesButton;

    @FXML
    private Button informesButton;

    @FXML
    void verPantallaPaquetes(ActionEvent event) {
        try {
            // Cargar la nueva ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewPaquetesAdmin.fxml"));
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
            stage.setOnShown(e -> {
                region.prefWidthProperty().bind(stage.widthProperty());
                region.prefHeightProperty().bind(stage.heightProperty());
            });

            // Mostrar la nueva ventana
            stage.show();

            // Cerrar la ventana actual
            Stage currentStage = (Stage) articulosButton.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void verPantallaHoteles(ActionEvent event) {
        try {
            // Cargar la nueva ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewHotelAdmin.fxml"));
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
            stage.setOnShown(e -> {
                region.prefWidthProperty().bind(stage.widthProperty());
                region.prefHeightProperty().bind(stage.heightProperty());
            });

            // Mostrar la nueva ventana
            stage.show();

            // Cerrar la ventana actual
            Stage currentStage = (Stage) vehiculosButton.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void verPantallaArticulos(ActionEvent event) {
        try {
            // Cargar la nueva ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewArticuloAdmin.fxml"));
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
            stage.setOnShown(e -> {
                region.prefWidthProperty().bind(stage.widthProperty());
                region.prefHeightProperty().bind(stage.heightProperty());
            });

            // Mostrar la nueva ventana
            stage.show();

            // Cerrar la ventana actual
            Stage currentStage = (Stage) vehiculosButton.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void verPantallaVehiculo(ActionEvent event) {
        try {
            // Cargar la nueva ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewAutomovilAdmin.fxml"));
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
            stage.setOnShown(e -> {
                region.prefWidthProperty().bind(stage.widthProperty());
                region.prefHeightProperty().bind(stage.heightProperty());
            });

            // Mostrar la nueva ventana
            stage.show();

            // Cerrar la ventana actual
            Stage currentStage = (Stage) vehiculosButton.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void abrirVentanaInformes(ActionEvent event) {
        try {
            // Cargar la nueva ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewInformes.fxml"));
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
            stage.setOnShown(e -> {
                region.prefWidthProperty().bind(stage.widthProperty());
                region.prefHeightProperty().bind(stage.heightProperty());
            });

            // Mostrar la nueva ventana
            stage.show();

            // Cerrar la ventana actual
            Stage currentStage = (Stage) vehiculosButton.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void volverLogin(ActionEvent event) {
        try {
            // Cargar la nueva ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewLogin.fxml"));
            Parent root = loader.load();

            // Crear la escena y la etapa
            Stage stage = new Stage();
            stage.setTitle("Home");
            stage.setScene(new Scene(root, 800, 600));

            // Mostrar la nueva ventana
            stage.show();

            // Cerrar la ventana actual
            Stage currentStage = (Stage) volverButton.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }

