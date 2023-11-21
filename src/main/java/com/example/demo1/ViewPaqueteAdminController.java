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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.*;

public class ViewPaqueteAdminController {

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
    private Button btnEliminarPaquete;

    @FXML
    private TableColumn<ObservableList<String>, Integer> clmIdPaquete;

    @FXML
    private TextField txtCriterio;

    @FXML
    private TableColumn<ObservableList<String>, String> clmTipoPaquete;

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnAbrir;

    @FXML
    private Button btnEditar;

    private ViewAgregarPaqueteController ventanaPrincipalController;

    public void setVentanaPrincipalController(ViewAgregarPaqueteController controller) {
        this.ventanaPrincipalController = controller;
    }

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

        btnEliminarPaquete.setOnAction(event -> {
            ObservableList<String> selectedItem = tablePaquete.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                eliminarRegistro(selectedItem);
            } else {
                // Mensaje de advertencia o manejo adecuado si no se selecciona ningún registro
            }
        });
    }

    @FXML
    void abrirVentanaAgregarPaquete(ActionEvent event) {
        try {
            // Cargar la nueva ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewAgregarPaquete.fxml"));
            Parent root = loader.load();

            ViewAgregarPaqueteController agregarPaqueteController = loader.getController();
            agregarPaqueteController.setVentanaPrincipalController(this);

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

    @FXML
    void abrirVentanaEditarPaquete(ActionEvent event) {
        ObservableList<String> paqueteSeleccionado = tablePaquete.getSelectionModel().getSelectedItem();

        if (paqueteSeleccionado != null) {
            try {
                // Cargar la nueva ventana de edición
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewEditarPaquete.fxml"));
                Parent root = loader.load();

                // Obtener el controlador de la ventana de edición
                ViewEditarPaqueteController editarController = loader.getController();

                // Obtener datos del vehículo seleccionado y pasarlos al controlador de la ventana de edición
                String id = paqueteSeleccionado.get(0);
                String descripcion = paqueteSeleccionado.get(1);
                String precio = paqueteSeleccionado.get(2);
                String tipo = paqueteSeleccionado.get(3);


                editarController.inicializarDatos(id, descripcion, precio, tipo , this);

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

    private void eliminarRegistro(ObservableList<String> data) {
        String id = data.get(0); // Suponiendo que el ID está en la primera posición

        // Realizar la conexión y la eliminación
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "BRITO", "123456789")) {
            String query = "DELETE FROM PAQUETE WHERE ID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, id);
                preparedStatement.executeUpdate();

                // Recargar la tabla después de eliminar el registro
                cargarTodosLosPaquetes();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar errores de conexión o eliminación
        }
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewNavegacion.fxml"));
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
