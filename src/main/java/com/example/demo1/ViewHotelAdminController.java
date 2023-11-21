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
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class ViewHotelAdminController {

    @FXML
    private Button volverButton;

    @FXML
    private Button btnEliminar;

    @FXML
    private TableView<ObservableList<String>> TableHoteles;

    @FXML
    private TableColumn<ObservableList<String>, Integer> ColumnaIdHotel;

    @FXML
    private ChoiceBox<String> choBoxEleccionCriterioHoteles;

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnEditar;

    @FXML
    private TableColumn<ObservableList<String>, Integer> clmTipoHotel;

    @FXML
    private TableColumn<ObservableList<String>, Integer> clmDireccion;

    @FXML
    private TableColumn<ObservableList<String>, Integer> clmNumHab;

    @FXML
    private TableColumn<ObservableList<String>, String> ColumnaDescripcionHotel;

    @FXML
    private TableColumn<ObservableList<String>, String> ColumnaNombreHotel;

    @FXML
    private TextField txtCriterio;

    private ViewAgregarHotelController ventanaPrincipalController;

    @FXML
    void volver(ActionEvent event) {

        try {
            // Cargar la nueva ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewNavegacion.fxml"));
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

    public void setVentanaPrincipalController(ViewAgregarHotelController controller) {
        this.ventanaPrincipalController = controller;
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
        clmNumHab.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<ObservableList<String>, Integer> param) {
                return new SimpleIntegerProperty(Integer.parseInt(param.getValue().get(3))).asObject();
            }
        });
        clmTipoHotel.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<ObservableList<String>, Integer> param) {
                return new SimpleIntegerProperty(Integer.parseInt(param.getValue().get(4))).asObject();
            }
        });
        clmDireccion.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<ObservableList<String>, Integer> param) {
                return new SimpleIntegerProperty(Integer.parseInt(param.getValue().get(5))).asObject();
            }
        });

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

        btnEliminar.setOnAction(event -> {
            ObservableList<String> selectedItem = TableHoteles.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                eliminarRegistro(selectedItem);
            } else {
                // Mensaje de advertencia o manejo adecuado si no se selecciona ningún registro
            }
        });

        btnAgregar.setOnAction(event -> abrirVentanaAgregar());

    }

    private void abrirVentanaAgregar() {
        try {
            // Cargar la nueva ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewAgregarHotel.fxml"));
            Parent root = loader.load();

            ViewAgregarHotelController agregarHotelController = loader.getController();
            agregarHotelController.setVentanaPrincipalController(this);

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


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void eliminarRegistro(ObservableList<String> data) {
        String id = data.get(0); // Suponiendo que el ID está en la primera posición

        // Realizar la conexión y la eliminación
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "BRITO", "123456789")) {
            String query = "DELETE FROM HOTEL WHERE ID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, id);
                preparedStatement.executeUpdate();

                // Recargar la tabla después de eliminar el registro
                cargarTodosLosHoteles();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar errores de conexión o eliminación
        }
    }

    public void cargarTodosLosHoteles() {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "BRITO", "123456789")) {
            String query = "SELECT ID, NOMBRE, DESCRIPCION, NUMEROHABITACIONES, TIPO_HOTEL_ID, DIRECCION_ID  FROM HOTEL";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    ObservableList<ObservableList<String>> hoteles = FXCollections.observableArrayList();


                    while (resultSet.next()) {
                        ObservableList<String> fila = FXCollections.observableArrayList();
                        fila.add(resultSet.getString("ID"));
                        fila.add(resultSet.getString("NOMBRE"));
                        fila.add(resultSet.getString("DESCRIPCION"));
                        fila.add(resultSet.getString("NUMEROHABITACIONES"));
                        fila.add(resultSet.getString("TIPO_HOTEL_ID"));
                        fila.add(resultSet.getString("DIRECCION_ID"));
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
    void abrirVentanaEditarHotel(ActionEvent event) {
        ObservableList<String> hotelSeleccionado = TableHoteles.getSelectionModel().getSelectedItem();

        if (hotelSeleccionado != null) {
            try {
                // Cargar la nueva ventana de edición
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewEditarHotel.fxml"));
                Parent root = loader.load();

                // Obtener el controlador de la ventana de edición
                ViewEditarHotelController editarController = loader.getController();

                // Obtener datos del vehículo seleccionado y pasarlos al controlador de la ventana de edición
                String id = hotelSeleccionado.get(0);
                String nombre = hotelSeleccionado.get(1);
                String descripcion = hotelSeleccionado.get(2);
                String numHabitaciones= hotelSeleccionado.get(3);
                String tipo= hotelSeleccionado.get(4);
                String direccion = hotelSeleccionado.get(5);


                editarController.inicializarDatos(id, nombre, descripcion, numHabitaciones , tipo, direccion, this);

                // Crear la escena y la etapa para la ventana de edición
                Stage stage = new Stage();
                stage.setTitle("Editar Vehículo");
                stage.setScene(new Scene(root));

                // Mostrar la ventana de edición
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // No se ha seleccionado ninguna fila, puedes mostrar un mensaje o realizar otra acción
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
