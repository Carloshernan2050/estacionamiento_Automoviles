package Interfaz;

import DAO.HistorialRetirosDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class VentanaHistorialRetiros extends JFrame {

    private JTextArea txtHistorial;

    public VentanaHistorialRetiros() {
        setTitle("Historial de Retiros");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        txtHistorial = new JTextArea();
        txtHistorial.setEditable(false);
        txtHistorial.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(txtHistorial);

        add(scroll, BorderLayout.CENTER);
        cargarHistorial();
    }

    private void cargarHistorial() {
        try {
            HistorialRetirosDAO dao = new HistorialRetirosDAO();
            ArrayList<String> historial = dao.obtenerHistorial();
            StringBuilder sb = new StringBuilder();

            for (String registro : historial) {
                sb.append(registro).append("\n");
            }

            txtHistorial.setText(sb.toString());
        } catch (SQLException e) {
            txtHistorial.setText("Error al cargar historial: " + e.getMessage());
        }
    }
}
