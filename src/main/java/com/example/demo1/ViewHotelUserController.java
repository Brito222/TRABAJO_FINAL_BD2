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

public class ViewHotelUserController {

    @FXML
    private Button volverButton;

    @FXML
    private TableView<ObservableList<String>> TableHoteles;

    @FXML
    private TableColumn<ObservableList<String>, Integer> ColumnaIdHotel;

    @FXML
    private ChoiceBox<String> choBoxEleccionCriterioHoteles;

    @FXML
    private TableColumn<ObservableList<String>, String> ColumnaDescripcionHotel;

    @FXML
    private TableColumn<ObservableList<String>, String> ColumnaNombreHotel;

    @FXML
    private TextField txtCriterio;

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
            Stage currentStage = (Stage) volverButton.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        // Método de inicialización

        ColumnaIdHotel.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<ObservableList<String>, Integer> param) {
                return new SimpleIntegerProperty(Integer.parseInt(param.getValue().get(0))).asObject();
            }
        });
        ColumnaNombreHotel.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        ColumnaDescripcionHotel.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));

        cargarTodosLosHoteles();

        // Crear una lista de elementos
        ObservableList<String> items = FXCollections.observableArrayList("Numero de Habitaciones", "Tipo Hotel");

        // Establecer la lista de elementos en el ChoiceBox
        choBoxEleccionCriterioHoteles.setItems(items);

        // Opcionalmente, seleccionar un elemento por defecto
        choBoxEleccionCriterioHoteles.setValue("empty");

        // Puedes agregar escuchadores de eventos si lo necesitas
        choBoxEleccionCriterioHoteles.setOnAction(event -> {
            // Código a ejecutar cuando se selecciona un elemento
            String selectedItem = choBoxEleccionCriterioHoteles.getValue();
            System.out.println("Elemento seleccionado: " + selectedItem);
        });
    }


    private void cargarTodosLosHoteles() {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "BRITO", "123456789")) {
            String query = "SELECT ID, NOMBRE, DESCRIPCION FROM HOTEL";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    ObservableList<ObservableList<String>> hoteles = FXCollections.observableArrayList();


                    while (resultSet.next()) {
                        ObservableList<String> fila = FXCollections.observableArrayList();
                        fila.add(resultSet.getString("ID"));
                        fila.add(resultSet.getString("NOMBRE"));
                        fila.add(resultSet.getString("DESCRIPCION"));
                        hoteles.add(fila);
                    }
                    TableHoteles.setItems(hoteles);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar errores de conexión o consulta
        }
    }

    @FXML
    void buscarProducto(ActionEvent event) {
        String criteria = txtCriterio.getText();
        String criterioSelecciondo = choBoxEleccionCriterioHoteles.getValue();

        // Realizar la conexión y la consulta
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "BRITO", "123456789")) {
            String query;

            switch (criterioSelecciondo){
                case "Numero de Habitaciones":
                    query = "SELECT ID, NOMBRE, DESCRIPCION FROM HOTEL WHERE NUMEROHABITACIONES = ?";
                    break;
                case "Tipo Hotel":
                    query = "SELECT ID, NOMBRE, DESCRIPCION FROM HOTEL WHERE TIPO_HOTEL_ID = ?";
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
                        fila.add(resultSet.getString("NOMBRE"));
                        fila.add(resultSet.getString("DESCRIPCION"));
                        data.add(fila);
                    }
                    TableHoteles.setItems(data);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar errores de conexión o consulta
        }
    }
    }
