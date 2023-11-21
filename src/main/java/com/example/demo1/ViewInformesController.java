package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ViewInformesController {

    @FXML
    private Button btnGenerar1;

    @FXML
    private Button btnVolver;

    @FXML
    private Button btnGenerar7;

    @FXML
    private Button btnGenerar6;

    @FXML
    private Button btnGenerar8;

    @FXML
    private Button btnGenerar3;

    @FXML
    private Button btnGenerar2;

    @FXML
    private Button btnGenerar5;

    @FXML
    private Button btnGenerar4;

    @FXML
    void generarConsutla1(ActionEvent event) {
        try {
            // Realizar la conexión y la consulta
            try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "BRITO", "123456789")) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT v.cliente_id,cl.nombre, SUM(DV.PRECIO_TOTAL) AS INGRESOS_TOTALES " +
                        "FROM factura_venta v " +
                        "JOIN detalle_venta dv ON v.id = dv.factura_venta_id " +
                        "JOIN cliente cl ON v.cliente_id = cl.id " +
                        "GROUP BY v.cliente_id,cl.nombre " +
                        "UNION " +
                        "SELECT ca.reserva_id, cl.nombre, SUM(CA.PRECIO) AS INGRESOS_TOTALES " +
                        "FROM cancelacion_automovil ca " +
                        "JOIN detalle_vehiculo dt ON ca.reserva_id = dt.reserva_id " +
                        "JOIN reserva_vehiculo rv ON dt.reserva_id = rv.reserva_id " +
                        "JOIN reserva r ON rv.reserva_id = r.id " +
                        "JOIN cliente cl ON r.cliente_id = cl.id " +
                        "GROUP BY ca.reserva_id, cl.nombre");

                // Crear un objeto FileChooser para seleccionar la ubicación del archivo de texto
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Guardar Reporte");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de texto (*.txt)", "*.txt"));

                // Mostrar el diálogo de guardar archivo y obtener la ruta seleccionada
                File file = fileChooser.showSaveDialog(btnGenerar1.getScene().getWindow());

                if (file != null) {
                    // Escribir el resultado de la consulta en el archivo
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                        writer.write("Calcular los ingresos totales generados por cada cliente, considerando todas sus compras y reserva\n\n");
                        while (resultSet.next()) {
                            String resultLine = resultSet.getString("cliente_id") + "\t" +
                                    resultSet.getString("nombre") + "\t" +
                                    resultSet.getFloat("INGRESOS_TOTALES");
                            writer.write(resultLine);
                            writer.newLine();
                        }
                    }

                    // Mostrar un mensaje de éxito
                    System.out.println("Reporte generado exitosamente.");
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            // Manejar errores de conexión o escritura
        }
    }

    @FXML
    void generarConsutla2(ActionEvent event) {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "BRITO", "123456789")) {
            String query = "SELECT TO_CHAR(FECHA_RESERVA, 'MM') AS MES, COUNT(*) AS NUMERO_DE_RESERVAS " +
                    "FROM RESERVA_VEHICULO rv " +
                    "JOIN detalle_vehiculo dt ON rv.reserva_id = dt.reserva_id " +
                    "GROUP BY TO_CHAR(FECHA_RESERVA, 'MM') " +
                    "ORDER BY MES";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                // Guardar los resultados en un archivo
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Guardar Informe");
                File file = fileChooser.showSaveDialog(btnGenerar1.getScene().getWindow());

                if (file != null) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                        // Escribir el encabezado
                        writer.write("Reserva de vehiculo por mes");
                        writer.newLine();

                        // Escribir los resultados
                        while (resultSet.next()) {
                            String mes = resultSet.getString("MES");
                            String numeroDeReservas = resultSet.getString("NUMERO_DE_RESERVAS");
                            writer.write(mes + "\t" + numeroDeReservas);
                            writer.newLine();
                        }
                    }

                    // Mostrar un mensaje de éxito
                    System.out.println("Informe generado exitosamente.");
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            // Manejar errores de conexión o consulta
        }
    }

    @FXML
    void generarConsutla3(ActionEvent event) {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "BRITO", "123456789")) {
            String query = "SELECT H.NOMBRE AS NOMBRE_HOTEL, I.DESCRIPCION AS INSTALACION, COUNT(*) AS CANTIDAD_USOS " +
                    "FROM HOTEL H " +
                    "JOIN INSTALACION I ON H.ID = I.HOTEL_ID " +
                    "GROUP BY H.NOMBRE, I.DESCRIPCION";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                // Guardar los resultados en un archivo
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Guardar Informe");
                File file = fileChooser.showSaveDialog(btnGenerar1.getScene().getWindow());

                if (file != null) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                        // Escribir el encabezado
                        writer.write("Uso de instalaciones por hotel");
                        writer.newLine();

                        // Escribir los resultados
                        while (resultSet.next()) {
                            String nombreHotel = resultSet.getString("NOMBRE_HOTEL");
                            String instalacion = resultSet.getString("INSTALACION");
                            String cantidadUsos = resultSet.getString("CANTIDAD_USOS");
                            writer.write(nombreHotel + "\t" + instalacion + "\t" + cantidadUsos);
                            writer.newLine();
                        }
                    }

                    // Mostrar un mensaje de éxito
                    System.out.println("Informe generado exitosamente.");
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            // Manejar errores de conexión o consulta
        }
    }

    @FXML
    void generarConsutla4(ActionEvent event) {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "BRITO", "123456789")) {
            String query = "SELECT P.DESCRIPCION AS PAQUETE, COUNT(*) AS NUMERO_DE_RESERVAS " +
                    "FROM PAQUETE P " +
                    "JOIN tipo_paquete tp ON p.id = tp.paquete_id " +
                    "JOIN detalle_paquete dp ON tp.paquete_id = dp.paquete_id " +
                    "JOIN reserva_paquete rp ON dp.reserva_id = rp.reserva_id " +
                    "JOIN reserva r ON rp.reserva_id = r.id " +
                    "WHERE ROWNUM <= 5 " +
                    "GROUP BY P.DESCRIPCION " +
                    "ORDER BY NUMERO_DE_RESERVAS DESC";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                // Guardar los resultados en un archivo
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Guardar Informe");
                File file = fileChooser.showSaveDialog(btnGenerar1.getScene().getWindow());

                if (file != null) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                        // Escribir el encabezado
                        writer.write("Top 5 de paquetes reservados");
                        writer.newLine();
                        writer.newLine(); // Agrega una línea en blanco después del encabezado

                        // Escribir los resultados
                        while (resultSet.next()) {
                            String paquete = resultSet.getString("PAQUETE");
                            String numeroReservas = resultSet.getString("NUMERO_DE_RESERVAS");
                            writer.write("Paquete: " + paquete);
                            writer.newLine();
                            writer.write("Número de Reservas: " + numeroReservas);
                            writer.newLine();
                            writer.newLine(); // Agrega una línea en blanco entre resultados
                        }
                    }
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            // Manejar errores de conexión o consulta
        }
    }

    @FXML
    void generarConsutla5(ActionEvent event) {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "BRITO", "123456789")) {
            String query = "SELECT cl.nombre, SUM(PRECIO_TOTAL) AS VENTAS_TOTALES " +
                    "FROM detalle_venta dv " +
                    "JOIN factura_venta fv ON dv.factura_venta_id = fv.id " +
                    "JOIN cliente cl ON fv.cliente_id = cl.id " +
                    "GROUP BY cl.nombre";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                // Guardar los resultados en un archivo
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Guardar Informe");
                File file = fileChooser.showSaveDialog(btnGenerar1.getScene().getWindow());

                if (file != null) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                        // Escribir el encabezado
                        writer.write("Venta totales por clientes");
                        writer.newLine();

                        // Escribir los resultados
                        while (resultSet.next()) {
                            String nombreCliente = resultSet.getString("NOMBRE");
                            String ventasTotales = resultSet.getString("VENTAS_TOTALES");
                            writer.write(nombreCliente + "," + ventasTotales);
                            writer.newLine();
                        }
                    }
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            // Manejar errores de conexión o consulta
        }
    }

    @FXML
    void generarConsutla6(ActionEvent event) {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "BRITO", "123456789")) {
            String query = "SELECT TIPO_SEGURO_ID, SUM(PRECIO) AS INGRESOS_TOTALES " +
                    "FROM SERVICIO_ADICIONALES " +
                    "GROUP BY TIPO_SEGURO_ID";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                // Guardar los resultados en un archivo
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Guardar Informe");
                File file = fileChooser.showSaveDialog(btnGenerar1.getScene().getWindow());

                if (file != null) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                        // Escribir el encabezado
                        writer.write("Ingresos Totales por Tipo de Servicio Adicional");
                        writer.newLine();

                        // Escribir los resultados
                        while (resultSet.next()) {
                            String tipoSeguroId = resultSet.getString("TIPO_SEGURO_ID");
                            String ingresosTotales = resultSet.getString("INGRESOS_TOTALES");
                            writer.write(tipoSeguroId + "," + ingresosTotales);
                            writer.newLine();
                        }
                    }
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            // Manejar errores de conexión o consulta
        }
    }

    @FXML
    void generarConsutla7(ActionEvent event) {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "BRITO", "123456789")) {
            String query = "SELECT cd.nombre, COUNT(*) AS NUMERO_DE_RESERVAS " +
                    "FROM reserva r " +
                    "JOIN cliente cl ON r.cliente_id = cl.id " +
                    "JOIN direccion dr ON cl.direccion_id = dr.ciudad_id " +
                    "JOIN ciudad cd ON dr.ciudad_id = cd.id " +
                    "GROUP BY cd.nombre " +
                    "ORDER BY NUMERO_DE_RESERVAS DESC";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                // Guardar los resultados en un archivo
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Guardar Informe");
                File file = fileChooser.showSaveDialog(btnGenerar1.getScene().getWindow());

                if (file != null) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                        // Escribir el encabezado
                        writer.write("Ranking de Ciudades por Número de Reservas");
                        writer.newLine();

                        // Escribir los resultados
                        while (resultSet.next()) {
                            String nombreCiudad = resultSet.getString("NOMBRE");
                            String numeroDeReservas = resultSet.getString("NUMERO_DE_RESERVAS");
                            writer.write(nombreCiudad + "," + numeroDeReservas);
                            writer.newLine();
                        }
                    }
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            // Manejar errores de conexión o consulta
        }
    }

    @FXML
    void generarConsutla8(ActionEvent event) {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "BRITO", "123456789")) {
            String query = "SELECT * FROM RESERVA_PAQUETE";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                // Guardar los resultados en un archivo
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Guardar Informe");
                File file = fileChooser.showSaveDialog(btnGenerar1.getScene().getWindow());

                if (file != null) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                        // Escribir el encabezado
                        for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                            writer.write(resultSet.getMetaData().getColumnName(i));
                            if (i < resultSet.getMetaData().getColumnCount()) {
                                writer.write(",");
                            }
                        }
                        writer.newLine();

                        // Escribir los resultados
                        while (resultSet.next()) {
                            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                                writer.write(resultSet.getString(i));
                                if (i < resultSet.getMetaData().getColumnCount()) {
                                    writer.write(",");
                                }
                            }
                            writer.newLine();
                        }
                    }
                }
            }
        } catch (SQLException | IOException e) {
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
