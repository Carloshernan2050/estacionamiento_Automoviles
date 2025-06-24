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

public class VehiculoDAO {
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

    public List<Vehiculo> obtenerTodosVehiculos() throws SQLException {
        List<Vehiculo> vehiculos = new ArrayList<>();
        String sql = "SELECT * FROM vehiculos";
        
        try (Connection con = ConexionDB.obtenerConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                vehiculos.add(new Vehiculo(
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getString("placa"),
                    rs.getString("propietario")
                ));
            }
        }
        return vehiculos;
    }

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
    
    public void eliminarVehiculo(String placa) throws SQLException {
        String sql = "DELETE FROM vehiculos WHERE placa = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, placa);
            ps.executeUpdate();
        }
    }
    
}