package Interfaz;

import javax.swing.*;
import java.awt.*;

public class InterfazVehiculo extends JFrame {
	
	public InterfazVehiculo() {
        interfaz();

    }

    private void interfaz() {
        setTitle("Gestión estacionamiento de vehiculos");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));
    }
}