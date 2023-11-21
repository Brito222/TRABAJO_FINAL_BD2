package com.example.demo1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;


public class main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("ViewLogin.fxml"));
        Parent rawRoot = fxmlLoader.load();

        // Castear a Region, que es un contenedor común
        Region root = (Region) rawRoot;

        // Configurar restricciones de redimensionamiento
        stage.setMinWidth(800); // Establecer un valor adecuado
        stage.setMinHeight(600); // Establecer un valor adecuado

        // Configurar el contenedor principal para usar su tamaño preferido
        root.prefWidthProperty().bind(stage.widthProperty());
        root.prefHeightProperty().bind(stage.heightProperty());

        Scene scene = new Scene(rawRoot);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

