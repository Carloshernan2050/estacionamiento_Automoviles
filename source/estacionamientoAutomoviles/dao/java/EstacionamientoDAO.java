package estacionamientoAutomoviles.dao.java;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import estacionamientoAutomoviles.conexion.ConexionBD;
import estacionamientoAutomoviles.mundo.java.Estacionamiento;

public class EstacionamientoDAO {

    public void insertarVehiculo(Vehiculo v) throws SQLException {
        Connection con = ConexionBD.obtenerConexion();
        if (con != null) {
            String sql = "INSERT INTO estacionamiento (placa, fechaIngreso, fechaRetiro, numParqueo) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, v.getPlaca());
            ps.setString(2, v.getMarca());
            ps.setString(3, v.getModelo());
            ps.setString(4, v.getPropietario());
            ps.executeUpdate();
            ps.close();
            con.close();
        }
    }
}
