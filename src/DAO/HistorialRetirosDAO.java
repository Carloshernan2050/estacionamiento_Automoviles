package DAO;

import java.sql.*;
import java.util.ArrayList;
import ConexionDB.ConexionDB;

public class HistorialRetirosDAO {

    public void insertarRetiro(String placa, Timestamp fechaIngreso, Timestamp fechaRetiro) throws SQLException {
        String sql = "INSERT INTO historialretiros (placa, fecha_ingreso, fecha_retiro) VALUES (?, ?, ?)";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, placa);
            ps.setTimestamp(2, fechaIngreso);
            ps.setTimestamp(3, fechaRetiro);
            ps.executeUpdate();
        }
    }

    public ArrayList<String> obtenerHistorial() throws SQLException {
        ArrayList<String> historial = new ArrayList<>();
        String sql = "SELECT * FROM historialretiros ORDER BY fecha_retiro DESC";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String placa = rs.getString("placa");
                Timestamp ingreso = rs.getTimestamp("fecha_ingreso");
                Timestamp retiro = rs.getTimestamp("fecha_retiro");

                historial.add("Placa: " + placa +
                              " | Ingreso: " + ingreso.toLocalDateTime() +
                              " | Retiro: " + retiro.toLocalDateTime());
            }
        }
        return historial;
    }
}
