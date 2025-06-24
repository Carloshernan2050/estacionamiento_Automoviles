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
        
 	}
}