package Interfaz;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import DAO.EstacionamientoDAO;

public class VentanaRetiroVehiculo extends JDialog {
	private final PanelListaVehiculos panelLista;

    public VentanaRetiroVehiculo(PanelListaVehiculos panelLista) {
        this.panelLista = panelLista;
        ventanaRetiro();
    }

    private void ventanaRetiro() {
        setTitle("Retirar Vehículo");
        setSize(450, 300);
        setModal(true);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));
    }
}