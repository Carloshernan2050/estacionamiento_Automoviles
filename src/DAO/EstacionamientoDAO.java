package DAO;

import java.sql.*;
import java.time.LocalDateTime;

import ConexionDB.ConexionDB;
import mundo.Estacionamiento;

/**
 * Clase DAO para manejar operaciones sobre la tabla 'estacionamientos'.
 */
public class EstacionamientoDAO {

    /**
     * Inserta un nuevo registro en la tabla de estacionamientos.
     * 
     * @param e Objeto Estacionamiento con los datos del vehículo y su ingreso.
     * @throws SQLException si ocurre un error al ejecutar la consulta.
     */
    public void insertarEstacionamiento(Estacionamiento e) throws SQLException {
        String sql = "INSERT INTO estacionamientos (placa, fecha_ingreso, fecha_retiro, numero_lugar) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (e.getVehiculo() == null || e.getVehiculo().getPlaca() == null) {
                throw new IllegalArgumentException("El vehículo o su placa no pueden ser nulos.");
            }

            ps.setString(1, e.getVehiculo().getPlaca());
            ps.setTimestamp(2, Timestamp.valueOf(e.getFechaIngreso()));
            ps.setTimestamp(3, e.getFechaRetiro() != null ? Timestamp.valueOf(e.getFechaRetiro()) : null);
            ps.setInt(4, e.getNumParqueo());
            ps.executeUpdate();
        }
    }

    /**
     * Marca como retirado un vehículo actualizando la fecha de retiro.
     * 
     * @param placa        Placa del vehículo.
     * @param fechaRetiro  Fecha en la que se retira el vehículo.
     * @throws SQLException si ocurre un error al ejecutar las consultas.
     */
    public void retirarVehiculo(String placa, LocalDateTime fechaRetiro) throws SQLException {
        String sqlUpdate = "UPDATE estacionamientos SET fecha_retiro = ? WHERE placa = ? AND fecha_retiro IS NULL";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement psUpdate = con.prepareStatement(sqlUpdate)) {

            psUpdate.setTimestamp(1, Timestamp.valueOf(fechaRetiro));
            psUpdate.setString(2, placa);
            int filas = psUpdate.executeUpdate();

            if (filas > 0) {
                // Obtener la fecha de ingreso correspondiente
                String sqlSelect = "SELECT fecha_ingreso FROM estacionamientos WHERE placa = ? AND fecha_retiro = ?";
                try (PreparedStatement psSelect = con.prepareStatement(sqlSelect)) {
                    psSelect.setString(1, placa);
                    psSelect.setTimestamp(2, Timestamp.valueOf(fechaRetiro));
                    try (ResultSet rs = psSelect.executeQuery()) {
                        if (rs.next()) {
                            LocalDateTime fechaIngreso = rs.getTimestamp("fecha_ingreso").toLocalDateTime();
                            registrarRetiro(placa, fechaIngreso, fechaRetiro);
                        }
                    }
                }
            }
        }
    }

    /**
     * Registra el retiro de un vehículo en la tabla historialretiros.
     * 
     * @param placa        Placa del vehículo.
     * @param fechaIngreso Fecha de ingreso del vehículo.
     * @param fechaRetiro  Fecha de retiro del vehículo.
     * @throws SQLException si ocurre un error al insertar en historial.
     */
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

    /**
     * Busca el último registro de un vehículo por su placa.
     * 
     * @param placa Placa del vehículo.
     * @return Objeto Estacionamiento con la información encontrada, o null si no se encuentra.
     * @throws SQLException si ocurre un error al buscar.
     */
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
                        null,
                        rs.getTimestamp("fecha_retiro") != null
                            ? rs.getTimestamp("fecha_retiro").toLocalDateTime()
                            : null
                    );
                }
            }
        }
        return null;
    }

    /**
     * Retira un vehículo del sistema eliminando sus datos de estacionamiento y vehículo,
     * y registrando el retiro en historial.
     * 
     * @param placa        Placa del vehículo.
     * @param fechaRetiro  Fecha de retiro del vehículo.
     * @throws SQLException si ocurre un error durante la transacción.
     */
    public void retirarVehiculoYEliminarDatos(String placa, LocalDateTime fechaRetiro) throws SQLException {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa no puede ser nula o vacía.");
        }
        if (fechaRetiro == null) {
            throw new IllegalArgumentException("La fecha de retiro no puede ser nula.");
        }

        Connection con = null;

        try {
            con = ConexionDB.obtenerConexion();
            con.setAutoCommit(false); // Iniciar transacción

            // 1. Obtener datos actuales del estacionamiento
            String sqlSelect = "SELECT fecha_ingreso, numero_lugar FROM estacionamientos WHERE placa = ? ORDER BY fecha_ingreso DESC LIMIT 1";
            LocalDateTime fechaIngreso = null;
            int numeroLugar = 0;

            try (PreparedStatement psSelect = con.prepareStatement(sqlSelect)) {
                psSelect.setString(1, placa);
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        fechaIngreso = rs.getTimestamp("fecha_ingreso").toLocalDateTime();
                        numeroLugar = rs.getInt("numero_lugar");
                    } else {
                        throw new SQLException("No se encontró el vehículo en estacionamientos.");
                    }
                }
            }

            // 2. Insertar en historialretiros
            String sqlHistorial = "INSERT INTO historialretiros (placa, placa_fija, fecha_ingreso, fecha_retiro, numero_lugar) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement psHist = con.prepareStatement(sqlHistorial)) {
                psHist.setString(1, placa);  // FK
                psHist.setString(2, placa);  // copia fija
                psHist.setTimestamp(3, Timestamp.valueOf(fechaIngreso));
                psHist.setTimestamp(4, Timestamp.valueOf(fechaRetiro));
                psHist.setInt(5, numeroLugar);

                int filas = psHist.executeUpdate();
                if (filas == 0) {
                    throw new SQLException("No se pudo insertar en historial.");
                }
            }

            // 3. Eliminar de estacionamientos
            String sqlDeleteEst = "DELETE FROM estacionamientos WHERE placa = ?";
            try (PreparedStatement psDelEst = con.prepareStatement(sqlDeleteEst)) {
                psDelEst.setString(1, placa);
                psDelEst.executeUpdate();
            }

            // 4. Eliminar de vehiculos
            String sqlDeleteVeh = "DELETE FROM vehiculos WHERE placa = ?";
            try (PreparedStatement psDelVeh = con.prepareStatement(sqlDeleteVeh)) {
                psDelVeh.setString(1, placa);
                psDelVeh.executeUpdate();
            }

            con.commit(); // Confirmar transacción

        } catch (SQLException e) {
            if (con != null) {
                con.rollback(); // Revertir si hay error
            }
            throw e;
        } finally {
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        }
    }
}
