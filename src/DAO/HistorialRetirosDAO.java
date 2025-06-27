package DAO;

import java.sql.*;
import java.util.ArrayList;
import ConexionDB.ConexionDB;

/**
 * DAO para gestionar el historial de retiros de vehículos.
 * Guarda un registro cuando un vehículo es retirado del estacionamiento.
 */
public class HistorialRetirosDAO {

    /**
     * Inserta un nuevo registro en la tabla historial de retiros.
     * Guarda la placa (como clave foránea), una copia fija de la placa,
     * la fecha de ingreso, la fecha de retiro y el número del lugar que ocupaba el vehículo.
     * 
     * @param placa        Placa del vehículo que se está retirando.
     * @param fechaIngreso Fecha de ingreso del vehículo al estacionamiento.
     * @param fechaRetiro  Fecha en la que se retiró el vehículo.
     * @throws SQLException Si ocurre un error durante la inserción en la base de datos.
     */
    public void insertarRetiro(String placa, Timestamp fechaIngreso, Timestamp fechaRetiro) throws SQLException {
        String sql = "INSERT INTO historialretiros (placa, placa_fija, fecha_ingreso, fecha_retiro, numero_lugar) VALUES (?, ?, ?, ?, ?)";

        // Obtener número de lugar antes de que sea eliminado de 'estacionamientos'
        int numeroLugar = obtenerNumeroLugar(placa);

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, placa);              // Relación con FK (si aún existe)
            ps.setString(2, placa);              // Copia permanente
            ps.setTimestamp(3, fechaIngreso);
            ps.setTimestamp(4, fechaRetiro);
            ps.setInt(5, numeroLugar);

            ps.executeUpdate();
        }
    }

    /**
     * Busca el número de lugar actual de un vehículo en la tabla 'estacionamientos',
     * antes de que sea eliminado tras el retiro.
     * 
     * @param placa Placa del vehículo cuyo número de lugar se desea obtener.
     * @return El número de lugar si se encuentra, o -1 si no se encuentra.
     * @throws SQLException Si ocurre un error al realizar la consulta.
     */
    private int obtenerNumeroLugar(String placa) throws SQLException {
        String sql = "SELECT numero_lugar FROM estacionamientos WHERE placa = ? ORDER BY fecha_ingreso DESC LIMIT 1";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, placa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("numero_lugar");
                }
            }
        }
        return -1; // -1 indica que no se encontró el número de lugar
    }

    /**
     * Obtiene todos los registros del historial de retiros, ordenados del más reciente al más antiguo.
     * 
     * @return Una lista de cadenas que representan los retiros de vehículos,
     *         incluyendo placa, fecha de ingreso y fecha de retiro.
     * @throws SQLException Si ocurre un error durante la consulta.
     */
    public ArrayList<String> obtenerHistorial() throws SQLException {
        ArrayList<String> historial = new ArrayList<>();
        String sql = "SELECT * FROM historialretiros ORDER BY fecha_retiro DESC";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String placaVisible = rs.getString("placa_fija");
                Timestamp ingreso = rs.getTimestamp("fecha_ingreso");
                Timestamp retiro = rs.getTimestamp("fecha_retiro");

                historial.add("Placa: " + placaVisible +
                              " | Ingreso: " + ingreso.toLocalDateTime() +
                              " | Retiro: " + retiro.toLocalDateTime());
            }
        }
        return historial;
    }
}
