package Interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Clase principal de la interfaz gráfica del sistema de gestión de vehículos y estacionamiento.
 */
public class InterfazPrincipal extends JFrame {

    /**
     * Constructor: configura la ventana y componentes al iniciar.
     */
    public InterfazPrincipal() {
        configurarVentana();
        inicializarComponentes();
    }

    /**
     * Configura las propiedades básicas de la ventana.
     */
    private void configurarVentana() {
        setTitle("Gestión de Vehículos y Estacionamiento");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana
        setLayout(new BorderLayout(5, 5));
        getContentPane().setBackground(new Color(10, 15, 26)); // fondo azul oscuro
    }

    /**
     * Inicializa los componentes de la interfaz: paneles y botones.
     */
    private void inicializarComponentes() {
        // Paneles principales
        PanelListaVehiculos panelLista = new PanelListaVehiculos();
        PanelDetalleVehiculo panelDetalle = new PanelDetalleVehiculo();
        PanelOpciones panelOpciones = new PanelOpciones(panelLista, panelDetalle);

        // Establece la conexión entre lista y panel detalle
        panelLista.setPanelDetalle(panelDetalle);

        // Panel central dividido (lista a la izquierda, detalle a la derecha)
        JSplitPane panelCentral = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelLista, panelDetalle);
        panelCentral.setDividerLocation(400); // Ancho inicial del panel de lista
        panelCentral.setResizeWeight(0.33);   // Proporción de redimensionamiento
        panelCentral.setBorder(null);
        panelCentral.setBackground(new Color(20, 28, 45));

        // Panel superior con botones de historial y actualización
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelSuperior.setBackground(new Color(10, 15, 26));

        JButton btnHistorial = new JButton("Ver Historial de Retiros");
        JButton btnActualizar = new JButton("Actualizar Lista");

        estilizarBotonSuperior(btnHistorial);
        estilizarBotonSuperior(btnActualizar);

        // Acción al abrir historial de retiros
        btnHistorial.addActionListener((ActionEvent e) -> {
            VentanaHistorialRetiros ventana = new VentanaHistorialRetiros();
            ventana.setVisible(true);
        });

        // Acción para refrescar la lista de vehículos
        btnActualizar.addActionListener((ActionEvent e) -> {
            panelLista.cargarVehiculos();
        });

        panelSuperior.add(btnHistorial);
        panelSuperior.add(btnActualizar);

        // Agrega paneles a la ventana principal
        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelOpciones, BorderLayout.SOUTH);
    }

    /**
     * Estilo personalizado para botones inferiores (no se usa actualmente).
     */
    private void estilizarBoton(JButton boton) {
        boton.setBackground(new Color(40, 50, 65));
        boton.setForeground(new Color(240, 240, 240));
        boton.setFocusPainted(false);
        boton.setFont(new Font("SansSerif", Font.BOLD, 13));
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 120, 160), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**
     * Aplica un estilo claro y moderno a los botones del panel superior.
     */
    private void estilizarBotonSuperior(JButton boton) {
        Color fondoNormal = new Color(200, 210, 230);  // fondo claro

        boton.setPreferredSize(new Dimension(220, 40));
        boton.setBackground(fondoNormal);
        boton.setForeground(new Color(30, 30, 30)); // texto oscuro
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setOpaque(true);
        boton.setContentAreaFilled(true);
        boton.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180)));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**
     * Método principal para ejecutar la aplicación.
     *
     * @param args Argumentos de la línea de comandos (no utilizados en esta aplicación).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new InterfazPrincipal().setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                    "Error al iniciar la aplicación: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
