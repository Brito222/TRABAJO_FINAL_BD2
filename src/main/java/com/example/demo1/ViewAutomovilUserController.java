package com.example.demo1;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
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

public class ViewAutomovilUserController {

    @FXML
    private TableColumn<ObservableList<String>, Integer> clmIDVehiculo;

    @FXML
    private TableColumn<ObservableList<String>, Integer> clmModeloVehi;

    @FXML
    private TableColumn<ObservableList<String>, Float> clmPrecioVehi;

    @FXML
    private ChoiceBox<String> choBoxFiltroVehi;

    @FXML
    private TableColumn<ObservableList<String>, String> clmMarcaVehi;

    @FXML
    private TextField txtCriterio;

    @FXML
    private TableView<ObservableList<String>> tblVehiculos;

    @FXML
    private TableColumn<ObservableList<String>, Integer> clmKilVehi;

    @FXML
    private Button volverBtn;

    @FXML
    private Button buscarBtn;


    @FXML
    private TableColumn<ObservableList<String>, String> clmGamaVehi;

    @FXML
    private void initialize() {
        // Método de inicialización

        clmIDVehiculo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<ObservableList<String>, Integer> param) {
                return new SimpleIntegerProperty(Integer.parseInt(param.getValue().get(0))).asObject();
            }
        });
        clmModeloVehi.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<ObservableList<String>, Integer> param) {
                return new SimpleIntegerProperty(Integer.parseInt(param.getValue().get(1))).asObject();
            }
        });
        clmPrecioVehi.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, Float>, ObservableValue<Float>>() {
            @Override
            public ObservableValue<Float> call(TableColumn.CellDataFeatures<ObservableList<String>, Float> param) {
                return new SimpleObjectProperty<>(Float.parseFloat(param.getValue().get(2)));
            }
        });
        clmMarcaVehi.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));

        clmKilVehi.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<ObservableList<String>, Integer> param) {
                return new SimpleIntegerProperty(Integer.parseInt(param.getValue().get(4))).asObject();
            }
        });
        clmGamaVehi.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(5)));


        cargarTodosLosVehiculos();

        // Crear una lista de elementos
        ObservableList<String> items = FXCollections.observableArrayList("MODELO", "MARCA", "PRECIO", "KILOMETRAJE", "GAMA");

        // Establecer la lista de elementos en el ChoiceBox
        choBoxFiltroVehi.setItems(items);

        // Opcionalmente, seleccionar un elemento por defecto
        choBoxFiltroVehi.setValue("empty");

        // Puedes agregar escuchadores de eventos si lo necesitas
        choBoxFiltroVehi.setOnAction(event -> {
            // Código a ejecutar cuando se selecciona un elemento
            String selectedItem = choBoxFiltroVehi.getValue();
            System.out.println("Elemento seleccionado: " + selectedItem);
        });
    }

    private void cargarTodosLosVehiculos() {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "BRITO", "123456789")) {
            String query = "SELECT A.ID, A.MODELO, A.PRECIO, M.NOMBRE AS NOM_MARCA, A.KILOMETRAJE, G.DESCRIPCION FROM AUTOMOVIL A JOIN MARCA M ON M.ID = A.MARCA_ID JOIN GAMA G ON A.GAMA_ID = G.ID";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    ObservableList<ObservableList<String>> hoteles = FXCollections.observableArrayList();


                    while (resultSet.next()) {
                        ObservableList<String> fila = FXCollections.observableArrayList();
                        fila.add(resultSet.getString("ID"));
                        fila.add(resultSet.getString("MODELO"));
                        fila.add(resultSet.getString("PRECIO"));
                        fila.add(resultSet.getString("NOM_MARCA"));
                        fila.add(resultSet.getString("KILOMETRAJE"));
                        fila.add(resultSet.getString("DESCRIPCION"));
                        hoteles.add(fila);
                    }
                    tblVehiculos.setItems(hoteles);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar errores de conexión o consulta
        }
    }

    @FXML
    void volverVehiculo(ActionEvent event) {
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
            Stage currentStage = (Stage) volverBtn.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void buscarPorFiltroVehiculo(ActionEvent event) {
        String criteria = txtCriterio.getText();
        String criterioSelecciondo = choBoxFiltroVehi.getValue();

        // Realizar la conexión y la consulta
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "BRITO", "123456789")) {
            String query;

            switch (criterioSelecciondo){
                case "MODELO":
                    query = "SELECT A.ID, A.MODELO, A.PRECIO, M.NOMBRE AS NOM_MARCA, A.KILOMETRAJE, G.DESCRIPCION FROM AUTOMOVIL A JOIN MARCA M ON M.ID = A.MARCA_ID JOIN GAMA G ON A.GAMA_ID = G.ID WHERE A.MODELO = ?";
                    break;
                case "MARCA":
                    query = "SELECT A.ID, A.MODELO, A.PRECIO, M.NOMBRE AS NOM_MARCA, A.KILOMETRAJE, G.DESCRIPCION FROM AUTOMOVIL A JOIN MARCA M ON M.ID = A.MARCA_ID JOIN GAMA G ON A.GAMA_ID = G.ID WHERE M.NOMBRE = ?";
                    break;
                case "PRECIO":
                    query = "SELECT A.ID, A.MODELO, A.PRECIO, M.NOMBRE AS NOM_MARCA, A.KILOMETRAJE, G.DESCRIPCION FROM AUTOMOVIL A JOIN MARCA M ON M.ID = A.MARCA_ID JOIN GAMA G ON A.GAMA_ID = G.ID WHERE A.PRECIO = ?";
                    break;
                case "KILOMETRAJE":
                    query = "SELECT A.ID, A.MODELO, A.PRECIO, M.NOMBRE AS NOM_MARCA, A.KILOMETRAJE, G.DESCRIPCION FROM AUTOMOVIL A JOIN MARCA M ON M.ID = A.MARCA_ID JOIN GAMA G ON A.GAMA_ID = G.ID WHERE A.KILOMETRAJE = ?";
                    break;
                case "GAMA":
                    query = "SELECT A.ID, A.MODELO, A.PRECIO, M.NOMBRE AS NOM_MARCA, A.KILOMETRAJE, G.DESCRIPCION FROM AUTOMOVIL A JOIN MARCA M ON M.ID = A.MARCA_ID JOIN GAMA G ON A.GAMA_ID = G.ID WHERE A.GAMA_ID = ?";
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
                        fila.add(resultSet.getString("MODELO"));
                        fila.add(resultSet.getString("PRECIO"));
                        fila.add(resultSet.getString("NOM_MARCA"));
                        fila.add(resultSet.getString("KILOMETRAJE"));
                        fila.add(resultSet.getString("DESCRIPCION"));
                        data.add(fila);
                    }
                    tblVehiculos.setItems(data);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar errores de conexión o consulta
        }
    }
}



