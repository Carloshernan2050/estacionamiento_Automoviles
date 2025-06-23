package ConexionDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/estacionamiento";
    private static final String USUARIO = "root";
    private static final String CLAVE = "";

    public static Connection obtenerConexion() throws SQLException {
        try {
        	Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            throw new SQLException("Error al cargar el driver JDBC", e);
        }
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}