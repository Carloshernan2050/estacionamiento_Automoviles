package Interfaz;

import javax.swing.*;
import java.awt.*;

public class InterfazVehiculo extends JFrame {
	
	public InterfazVehiculo() {
        ventana();

    }

    private void ventana() {
        setTitle("Gestion estacionamiento de vehiculos");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));
    }
    
 private void Interfaz() {
        
        PanelListaVehiculos panelLista = new PanelListaVehiculos();
        PanelDetalleVehiculo panelDetalle = new PanelDetalleVehiculo();
        PanelOpciones panelOpciones = new PanelOpciones(panelLista, panelDetalle);
        
        panelLista.setPanelDetalle(panelDetalle);
        
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 5, 5));
        panelCentral.add(panelLista);
        panelCentral.add(panelDetalle);
        
        add(panelCentral, BorderLayout.CENTER);
        add(panelOpciones, BorderLayout.SOUTH);
 	}
 
 public static void main(String[] args) {
     SwingUtilities.invokeLater(() -> {
         try {
             UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
             new InterfazVehiculo().setVisible(true);
         } catch (Exception e) {
             JOptionPane.showMessageDialog(null, 
                 "Error al ejecutar: " + e.getMessage(), 
                 "Error", JOptionPane.ERROR_MESSAGE);
         }
     });
 }
}