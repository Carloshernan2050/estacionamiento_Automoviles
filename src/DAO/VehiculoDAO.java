package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ConexionDB.ConexionDB;
import mundo.Vehiculo;

/**
 * Clase DAO para realizar operaciones CRUD sobre la tabla 'vehiculos'.
 */
public class VehiculoDAO {

    /**
     * Inserta un vehículo en la base de datos.
     *
     * @param v Objeto Vehiculo con los datos a insertar.
     * @throws SQLException Si ocurre un error en la operación SQL.
     */
    public void insertarVehiculo(Vehiculo v) throws SQLException {
        String sql = "INSERT INTO vehiculos (placa, marca, modelo, propietario, imagen_url) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, v.getPlaca());
            ps.setString(2, v.getMarca());
            ps.setString(3, v.getModelo());
            ps.setString(4, v.getPropietario());
            ps.setString(5, v.getImagenUrl());
            ps.executeUpdate();
        }
    }

    /**
     * Obtiene todos los vehículos registrados en la base de datos.
     *
     * @return Lista de objetos Vehiculo.
     * @throws SQLException Si ocurre un error al consultar la base de datos.
     */
    public List<Vehiculo> obtenerTodosVehiculos() throws SQLException {
        List<Vehiculo> vehiculos = new ArrayList<>();
        String sql = "SELECT * FROM vehiculos";

        try (Connection con = ConexionDB.obtenerConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Vehiculo v = new Vehiculo(
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getString("placa"),
                    rs.getString("propietario")
                );
                v.setImagenUrl(rs.getString("imagen_url"));
                vehiculos.add(v);
            }
        }
        return vehiculos;
    }

    /**
     * Busca un vehículo por su placa.
     *
     * @param placa Placa del vehículo a buscar.
     * @return Objeto Vehiculo si se encuentra, o null si no existe.
     * @throws SQLException Si ocurre un error al ejecutar la consulta.
     */
    public Vehiculo buscarVehiculoPorPlaca(String placa) throws SQLException {
        String sql = "SELECT * FROM vehiculos WHERE placa = ?";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, placa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Vehiculo v = new Vehiculo(
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getString("placa"),
                        rs.getString("propietario")
                    );
                    v.setImagenUrl(rs.getString("imagen_url"));
                    return v;
                }
            }
        }
        return null;
    }

    /**
     * Actualiza los datos de un vehículo existente.
     *
     * @param v Objeto Vehiculo con los nuevos datos (la placa no cambia).
     * @throws SQLException Si ocurre un error al actualizar los datos.
     */
    public void actualizarVehiculo(Vehiculo v) throws SQLException {
        String sql = "UPDATE vehiculos SET marca = ?, modelo = ?, propietario = ?, imagen_url = ? WHERE placa = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, v.getMarca());
            stmt.setString(2, v.getModelo());
            stmt.setString(3, v.getPropietario());
            stmt.setString(4, v.getImagenUrl());
            stmt.setString(5, v.getPlaca());

            stmt.executeUpdate();
        }
    }

    /**
     * Elimina un vehículo de la base de datos usando su placa.
     *
     * @param placa Placa del vehículo a eliminar.
     * @throws SQLException Si ocurre un error al realizar la eliminación.
     */
    public void eliminarVehiculo(String placa) throws SQLException {
        String sql = "DELETE FROM vehiculos WHERE placa = ?";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, placa);
            ps.executeUpdate();
        }
    }
}
