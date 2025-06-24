package Interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class InterfazPrincipal extends JFrame {

    public InterfazPrincipal() {
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setTitle("Gestión de Vehículos y Estacionamiento - Java 8");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));
    }

    private void inicializarComponentes() {
        // Crear componentes
        PanelListaVehiculos panelLista = new PanelListaVehiculos();
        PanelDetalleVehiculo panelDetalle = new PanelDetalleVehiculo();
        PanelOpciones panelOpciones = new PanelOpciones(panelLista, panelDetalle);

        // Configurar relaciones
        panelLista.setPanelDetalle(panelDetalle);

        // Panel central para lista y detalle
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 5, 5));
        panelCentral.add(panelLista);
        panelCentral.add(panelDetalle);

        // Panel superior con botones de historial y actualizar lista
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnHistorial = new JButton("Ver Historial de Retiros");
        btnHistorial.addActionListener((ActionEvent e) -> {
            VentanaHistorialRetiros ventana = new VentanaHistorialRetiros();
            ventana.setVisible(true);
        });

        JButton btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.addActionListener((ActionEvent e) -> {
            panelLista.cargarVehiculos();
        });

        panelSuperior.add(btnHistorial);
        panelSuperior.add(btnActualizar);

        // Agregar componentes al frame
        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelOpciones, BorderLayout.SOUTH);
    }

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
