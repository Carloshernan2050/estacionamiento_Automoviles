package Interfaz;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import DAO.*;
import mundo.*;

public class DialogoBusquedaVehiculo extends JDialog {
    
    public DialogoBusquedaVehiculo() {
        configurarDialogo();
    }

    private void configurarDialogo() {
        setTitle("Buscar Vehículo");
        setSize(500, 400);
        setModal(true);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));
    }
}