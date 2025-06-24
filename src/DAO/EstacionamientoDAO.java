package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import ConexionDB.ConexionDB;
import mundo.Estacionamiento;

public class EstacionamientoDAO {

    public void insertarEstacionamiento(Estacionamiento e) throws SQLException {
        String sql = "INSERT INTO estacionamientos (placa, fecha_ingreso, fecha_retiro, numero_lugar) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e.getVehiculo().getPlaca());
            ps.setTimestamp(2, Timestamp.valueOf(e.getFechaIngreso()));
            ps.setTimestamp(3, e.getFechaRetiro() != null ? Timestamp.valueOf(e.getFechaRetiro()) : null);
            ps.setInt(4, e.getNumParqueo());
            ps.executeUpdate();
        }
    }

    public void retirarVehiculo(String placa, LocalDateTime fechaRetiro) throws SQLException {
        String sqlUpdate = "UPDATE estacionamientos SET fecha_retiro = ? WHERE placa = ? AND fecha_retiro IS NULL";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement psUpdate = con.prepareStatement(sqlUpdate)) {

            psUpdate.setTimestamp(1, Timestamp.valueOf(fechaRetiro));
            psUpdate.setString(2, placa);
            int filas = psUpdate.executeUpdate();

            if (filas > 0) {
                // Obtener la fecha_ingreso del retiro actualizado
                String sqlSelect = "SELECT fecha_ingreso FROM estacionamientos WHERE placa = ? AND fecha_retiro = ?";
                try (PreparedStatement psSelect = con.prepareStatement(sqlSelect)) {
                    psSelect.setString(1, placa);
                    psSelect.setTimestamp(2, Timestamp.valueOf(fechaRetiro));
                    try (ResultSet rs = psSelect.executeQuery()) {
                        if (rs.next()) {
                            LocalDateTime fechaIngreso = rs.getTimestamp("fecha_ingreso").toLocalDateTime();
                            registrarRetiro(placa, fechaIngreso, fechaRetiro);  // <-- guarda el historial
                        }
                    }
                }
            }
        }
    }


    private void registrarRetiro(String placa, LocalDateTime fechaIngreso, LocalDateTime fechaRetiro) throws SQLException {
        String sql = "INSERT INTO historialretiros (placa, fecha_ingreso, fecha_retiro) VALUES (?, ?, ?)";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, placa);
            ps.setTimestamp(2, Timestamp.valueOf(fechaIngreso));
            ps.setTimestamp(3, Timestamp.valueOf(fechaRetiro));
            ps.executeUpdate();
        }
    }


    public Estacionamiento buscarEstacionamientoPorPlaca(String placa) throws SQLException {
        String sql = "SELECT * FROM estacionamientos WHERE placa = ? ORDER BY fecha_ingreso DESC LIMIT 1";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, placa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Estacionamiento(
                        rs.getTimestamp("fecha_ingreso").toLocalDateTime(),
                        rs.getInt("numero_lugar"),
                        null, // Puedes cargar el Vehiculo con VehiculoDAO si lo necesitas
                        rs.getTimestamp("fecha_retiro") != null
                            ? rs.getTimestamp("fecha_retiro").toLocalDateTime()
                            : null
                    );
                }
            }
        }
        return null;
    }

    public void eliminarEstacionamiento(String placa) throws SQLException {
        String sql = "DELETE FROM estacionamientos WHERE placa = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, placa);
            ps.executeUpdate();
        }
    }
}
