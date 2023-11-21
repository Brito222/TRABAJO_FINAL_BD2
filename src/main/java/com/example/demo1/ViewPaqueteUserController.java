package com.example.demo1;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.*;

public class ViewPaqueteUserController {

    @FXML
    private ChoiceBox<String> choBoxFiltroPaquete;

    @FXML
    private TableView<ObservableList<String>> tablePaquete;

    @FXML
    private TableColumn<ObservableList<String>, String> clmNombrePaquete;

    @FXML
    private TableColumn<ObservableList<String>, Integer> clmPrecioPaquete;

    @FXML
    private Button btnVolver;

    @FXML
    private TableColumn<ObservableList<String>, Integer> clmIdPaquete;

    @FXML
    private TextField txtCriterio;

    @FXML
    private TableColumn<ObservableList<String>, String> clmTipoPaquete;

    @FXML
    private Button btnBuscar;

    @FXML
    private void initialize() {
        // Método de inicialización

        clmIdPaquete.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<ObservableList<String>, Integer> param) {
                return new SimpleIntegerProperty(Integer.parseInt(param.getValue().get(0))).asObject();
            }
        });
        clmNombrePaquete.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        clmPrecioPaquete.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<ObservableList<String>, Integer> param) {
                return new SimpleIntegerProperty(Integer.parseInt(param.getValue().get(2))).asObject();
            }
        });
        clmTipoPaquete.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));

        cargarTodosLosPaquetes();

        // Crear una lista de elementos
        ObservableList<String> items = FXCollections.observableArrayList("TIPO", "PRECIO");

        // Establecer la lista de elementos en el ChoiceBox
        choBoxFiltroPaquete.setItems(items);

        // Opcionalmente, seleccionar un elemento por defecto
        choBoxFiltroPaquete.setValue("empty");

        // Puedes agregar escuchadores de eventos si lo necesitas
        choBoxFiltroPaquete.setOnAction(event -> {
            // Código a ejecutar cuando se selecciona un elemento
            String selectedItem = choBoxFiltroPaquete.getValue();
            System.out.println("Elemento seleccionado: " + selectedItem);
        });

    }


    public void cargarTodosLosPaquetes() {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "BRITO", "123456789")) {
            String query = "SELECT ID, DESCRIPCION, PRECIO, TIPO FROM PAQUETE";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    ObservableList<ObservableList<String>> paquetes = FXCollections.observableArrayList();


                    while (resultSet.next()) {
                        ObservableList<String> fila = FXCollections.observableArrayList();
                        fila.add(resultSet.getString("ID"));
                        fila.add(resultSet.getString("DESCRIPCION"));
                        fila.add(resultSet.getString("PRECIO"));
                        fila.add(resultSet.getString("TIPO"));
                        paquetes.add(fila);
                    }
                    tablePaquete.setItems(paquetes);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar errores de conexión o consulta
        }
    }

    @FXML
    void buscarPaquete(ActionEvent event) {
        String criteria = txtCriterio.getText();
        String criterioSelecciondo = choBoxFiltroPaquete.getValue();

        // Realizar la conexión y la consulta
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "BRITO", "123456789")) {
            String query;

            switch (criterioSelecciondo){
                case "TIPO":
                    query = "SELECT ID, DESCRIPCION, PRECIO, TIPO FROM PAQUETE WHERE TIPO = ?";
                    break;
                case "PRECIO":
                    query = "SELECT ID, DESCRIPCION, PRECIO, TIPO FROM PAQUETE WHERE PRECIO = ?";
                    break;
                default:
                    throw new IllegalArgumentException("Opción no válida: ");

            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, criteria);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

                    while (resultSet.next()) {
                        ObservableList<String> fila = FXCollections.observableArrayList();
                        fila.add(resultSet.getString("ID"));
                        fila.add(resultSet.getString("DESCRIPCION"));
                        fila.add(resultSet.getString("PRECIO"));
                        fila.add(resultSet.getString("TIPO"));
                        data.add(fila);
                    }
                    tablePaquete.setItems(data);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar errores de conexión o consulta
        }
    }

    @FXML
    void volver(ActionEvent event) {

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
            Stage currentStage = (Stage) btnVolver.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
