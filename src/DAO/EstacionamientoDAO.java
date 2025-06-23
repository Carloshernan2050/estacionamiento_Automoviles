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

        // Validación para evitar NullPointerException
        if (e.getVehiculo() == null || e.getVehiculo().getPlaca() == null) {
            throw new SQLException("El vehículo o la placa no pueden ser nulos");
        }
        if (e.getFechaIngreso() == null) {
            throw new SQLException("La fecha de ingreso no puede ser nula");
        }

        ps.setString(1, e.getVehiculo().getPlaca());
        ps.setTimestamp(2, Timestamp.valueOf(e.getFechaIngreso()));
        if (e.getFechaRetiro() != null) {
            ps.setTimestamp(3, Timestamp.valueOf(e.getFechaRetiro()));
        } else {
            ps.setNull(3, java.sql.Types.TIMESTAMP);
        }
        ps.setInt(4, e.getNumParqueo());
        ps.executeUpdate();
    }
}
    public void retirarVehiculo(String placa, LocalDateTime fechaRetiro) throws SQLException {
        String sql = "UPDATE estacionamientos SET fecha_retiro = ? WHERE placa = ? AND fecha_retiro IS NULL";
        
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setTimestamp(1, Timestamp.valueOf(fechaRetiro));
            ps.setString(2, placa);
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
                        null, // El vehículo se obtiene por separado
                        rs.getTimestamp("fecha_retiro") != null ? rs.getTimestamp("fecha_retiro").toLocalDateTime() : null
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