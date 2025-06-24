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

public class VentanaBusquedaVehiculo extends JDialog {
    
    public VentanaBusquedaVehiculo() {
        configurarVentana();
    }

    private void configurarVentana() {
        setTitle("Buscar Vehículo");
        setSize(500, 400);
        setModal(true);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));
    }
    
private void inicializarComponentes() {
        
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBusqueda.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField txtPlaca = new JTextField(15);
        JButton btnBuscar = new JButton("Buscar");
        
        panelBusqueda.add(new JLabel("Placa:"));
        panelBusqueda.add(txtPlaca);
        panelBusqueda.add(btnBuscar);
        
        
        JTextArea txtResultado = new JTextArea(10, 30);
        txtResultado.setEditable(false);
        txtResultado.setLineWrap(true);
        txtResultado.setWrapStyleWord(true);
        JScrollPane scrollResultado = new JScrollPane(txtResultado);
        
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        panelBotones.add(btnCerrar);
        
        
        btnBuscar.addActionListener(e -> {
            String placa = txtPlaca.getText().trim().toUpperCase();
            if (placa.isEmpty()) {
                txtResultado.setText("Ingrese una placa para buscar");
                return;
            }
            
            try {
                buscarVehiculo(placa, txtResultado);
            } catch (SQLException ex) {
                txtResultado.setText("Error al buscar vehículo: " + ex.getMessage());
            }
        });
        
        
        add(panelBusqueda, BorderLayout.NORTH);
        add(scrollResultado, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
}