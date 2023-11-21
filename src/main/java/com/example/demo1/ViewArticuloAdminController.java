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

public class ViewArticuloAdminController {

    @FXML
    private TableView<ObservableList<String>> tableArticulos;

    @FXML
    private TableColumn<ObservableList<String>, String> clmNombreArti;

    @FXML
    private TableColumn<ObservableList<String>, String> clmTipoArt;

    @FXML
    private Button btnVolver;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnBuscarArt;

    @FXML
    private TableColumn<ObservableList<String>, Float> clmPrecioArt;

    @FXML
    private TableColumn<ObservableList<String>, String> clmDescArt;

    @FXML
    private TableColumn<ObservableList<String>, Integer> clmUnidadesArt;

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnEditarArt;

    @FXML
    private Button btnEditar;

    @FXML
    private TextField txtCriterio;

    @FXML
    private ChoiceBox<String> choBoxFiltroArticulos;

    @FXML
    private TableColumn<ObservableList<String>, Integer> clmIdArt;

    @FXML
    private void initialize() {
        // Método de inicialización

        clmIdArt.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<ObservableList<String>, Integer> param) {
                return new SimpleIntegerProperty(Integer.parseInt(param.getValue().get(0))).asObject();
            }
        });
        clmNombreArti.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        clmTipoArt.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));
        clmPrecioArt.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, Float>, ObservableValue<Float>>() {
            @Override
            public ObservableValue<Float> call(TableColumn.CellDataFeatures<ObservableList<String>, Float> param) {
                return new SimpleObjectProperty<>(Float.parseFloat(param.getValue().get(2)));
            }
        });
        clmDescArt.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(4)));

        clmUnidadesArt.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<ObservableList<String>, Integer> param) {
                return new SimpleIntegerProperty(Integer.parseInt(param.getValue().get(5))).asObject();
            }
        });

        cargarTodosLosArticulos();

        // Crear una lista de elementos
        ObservableList<String> items = FXCollections.observableArrayList("PRECIO", "TIPO", "UNIDADES");

        // Establecer la lista de elementos en el ChoiceBox
        choBoxFiltroArticulos.setItems(items);

        // Opcionalmente, seleccionar un elemento por defecto
        choBoxFiltroArticulos.setValue("empty");

        // Puedes agregar escuchadores de eventos si lo necesitas
        choBoxFiltroArticulos.setOnAction(event -> {
            // Código a ejecutar cuando se selecciona un elemento
            String selectedItem = choBoxFiltroArticulos.getValue();
            System.out.println("Elemento seleccionado: " + selectedItem);
        });
        btnEliminar.setOnAction(event -> {
            ObservableList<String> selectedItem = tableArticulos.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                eliminarRegistro(selectedItem);
            } else {
                // Mensaje de advertencia o manejo adecuado si no se selecciona ningún registro
            }
        });

    }

    @FXML
    void abrirVentanaAgregar(ActionEvent event) {

        try {
            // Cargar la nueva ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewAgregarArticulo.fxml"));
            Parent root = loader.load();

            ViewAgregarArticuloController agregarArticuloController = loader.getController();
            agregarArticuloController.setVentanaPrincipalController(this);

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
    void abrirVentanaEditArt(ActionEvent event) {
        ObservableList<String> articuloSeleccionado = tableArticulos.getSelectionModel().getSelectedItem();

        if (articuloSeleccionado != null) {
            try {
                // Cargar la nueva ventana de edición
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewEditarArticulo.fxml"));
                Parent root = loader.load();

                // Obtener el controlador de la ventana de edición
                ViewEditarArticuloController editarController = loader.getController();

                // Obtener datos del vehículo seleccionado y pasarlos al controlador de la ventana de edición
                String id = articuloSeleccionado.get(0);
                String nombre = articuloSeleccionado.get(1);
                String precio = articuloSeleccionado.get(2);
                String tipo = articuloSeleccionado.get(3);
                String descripcion = articuloSeleccionado.get(4);
                String unidades = articuloSeleccionado.get(5);


                editarController.inicializarDatos(id, nombre, precio, tipo , descripcion, unidades, this);

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
            String query = "DELETE FROM ARTICULO WHERE ID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, id);
                preparedStatement.executeUpdate();

                // Recargar la tabla después de eliminar el registro
                cargarTodosLosArticulos();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar errores de conexión o eliminación
        }
    }
    public void cargarTodosLosArticulos() {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "BRITO", "123456789")) {
            String query = "SELECT A.ID, A.NOMBRE, A.PRECIO, T.DESCRIPCION AS TIPO, A.DESCRIPCION , A.UNIDADES FROM ARTICULO A JOIN TIPO T ON T.ID = A.TIPO_ID";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    ObservableList<ObservableList<String>> articulos = FXCollections.observableArrayList();


                    while (resultSet.next()) {
                        ObservableList<String> fila = FXCollections.observableArrayList();
                        fila.add(resultSet.getString("ID"));
                        fila.add(resultSet.getString("NOMBRE"));
                        fila.add(resultSet.getString("PRECIO"));
                        fila.add(resultSet.getString("TIPO"));
                        fila.add(resultSet.getString("DESCRIPCION"));
                        fila.add(resultSet.getString("UNIDADES"));
                        articulos.add(fila);
                    }
                    tableArticulos.setItems(articulos);
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

    @FXML
    void buscarArticulos(ActionEvent event) {
        String criteria = txtCriterio.getText();
        String criterioSelecciondo = choBoxFiltroArticulos.getValue();

        // Realizar la conexión y la consulta
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "BRITO", "123456789")) {
            String query;
//"PRECIO", "TIPO", "UNIDADES"
            switch (criterioSelecciondo){
                case "PRECIO":
                    query = "SELECT A.ID, A.NOMBRE, A.PRECIO, T.DESCRIPCION AS TIPO, A.DESCRIPCION FROM ARTICULO A JOIN TIPO T ON T.ID = A.TIPO_ID WHERE A.PRECIO = ?";
                    break;
                case "TIPO":
                    query = "SELECT A.ID, A.NOMBRE, A.PRECIO, T.DESCRIPCION AS TIPO, A.DESCRIPCION FROM ARTICULO A JOIN TIPO T ON T.ID = A.TIPO_ID WHERE T.DESCRIPCION = ?";
                    break;
                case "UNIDADES":
                    query = "SELECT A.ID, A.NOMBRE, A.PRECIO, T.DESCRIPCION AS TIPO, A.DESCRIPCION FROM ARTICULO A JOIN TIPO T ON T.ID = A.TIPO_ID WHERE A.UNIDADES = ?";
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
                        fila.add(resultSet.getString("PRECIO"));
                        fila.add(resultSet.getString("TIPO"));
                        fila.add(resultSet.getString("DESCRIPCION"));
                        data.add(fila);
                    }
                    tableArticulos.setItems(data);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar errores de conexión o consulta
        }
    }
}



