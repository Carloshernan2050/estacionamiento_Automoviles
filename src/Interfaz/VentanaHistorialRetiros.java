package Interfaz;

import DAO.HistorialRetirosDAO;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Ventana que muestra el historial de retiros registrados desde la base de datos.
 * Se despliega como una ventana independiente y presenta los registros en un área de texto.
 */
public class VentanaHistorialRetiros extends JFrame {

    // Área de texto donde se mostrará el historial
    private JTextArea txtHistorial;

    /**
     * Constructor principal. Configura la ventana y carga los registros del historial.
     */
    public VentanaHistorialRetiros() {
        setTitle("Historial de Retiros");
        setSize(600, 450);
        setLocationRelativeTo(null); // Centra la ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta ventana
        getContentPane().setBackground(new Color(15, 20, 30));
        setLayout(new BorderLayout());

        // Configuración del área de texto donde se mostrará el historial
        txtHistorial = new JTextArea();
        txtHistorial.setEditable(false);
        txtHistorial.setFont(new Font("Consolas", Font.PLAIN, 14));
        txtHistorial.setBackground(new Color(25, 30, 40));
        txtHistorial.setForeground(Color.WHITE);
        txtHistorial.setCaretColor(Color.WHITE);
        txtHistorial.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Scroll para el área de texto
        JScrollPane scroll = new JScrollPane(txtHistorial);
        scroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 120, 160)),
            "Registros de Retiro",
            0, 0,
            new Font("SansSerif", Font.BOLD, 15),
            new Color(180, 200, 255)
        ));
        scroll.getViewport().setBackground(new Color(25, 30, 40));
        scroll.setBackground(new Color(25, 30, 40));

        // Personaliza la barra de desplazamiento vertical
        scroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(60, 70, 90);
                this.trackColor = new Color(25, 30, 40);
            }
        });

        // Agrega el scroll al centro de la ventana
        add(scroll, BorderLayout.CENTER);

        // Cargar historial desde la base de datos
        cargarHistorial();
    }

    /**
     * Recupera los datos del historial desde la base de datos y los muestra en el área de texto.
     * 
     * @throws SQLException si ocurre un error al acceder a la base de datos.
     */
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
