package estacionamientoAutomoviles.conexion.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB
{

    private static final String URL = "jdbc:mysql://localhost:3306/gestor_vehiculos";
    private static final String USUARIO = "root";
    private static final String CONTRASEÑA = ""; 

    public static Connection obtenerConexion( ) 
    {
        try 
        {
            Connection conexion = DriverManager.getConnection( URL, USUARIO, CONTRASEÑA);
            System.out.println( "¡Conexión exitosa a la base de datos!" );
            return conexion;
        } 
        catch ( SQLException e ) 
        {
            System.out.println( "Error al conectar: " + e.getMessage( ) );
            return null;
        }
    }
}
