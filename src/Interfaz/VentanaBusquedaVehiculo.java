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
        inicializarCampos();
    }

    private void configurarVentana() {
        setTitle("Buscar Vehículo");
        setSize(500, 400);
        setModal(true);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));
    }
    
private void inicializarCampos() {
        
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
	private void buscarVehiculo(String placa, JTextArea txtResultado) throws SQLException {
	    StringBuilder sb = new StringBuilder();
	    
	    VehiculoDAO vDao = new VehiculoDAO();
	    Vehiculo vehiculo = vDao.buscarVehiculoPorPlaca(placa);
	    
	    if (vehiculo == null) {
	        sb.append("No se encontró vehículo con placa: ").append(placa);
	    } else {
	        sb.append("informacion del vehiculo\n");
	        sb.append(String.format("%-12s: %s%n", "Placa", vehiculo.getPlaca()));
	        sb.append(String.format("%-12s: %s%n", "Marca", vehiculo.getMarca()));
	        sb.append(String.format("%-12s: %s%n", "Modelo", vehiculo.getModelo()));
	        sb.append(String.format("%-12s: %s%n", "Propietario", vehiculo.getPropietario()));
	        sb.append("\n");
	        

	        EstacionamientoDAO eDao = new EstacionamientoDAO();
	        Estacionamiento estacionamiento = eDao.buscarEstacionamientoPorPlaca(placa);
	        
	        if (estacionamiento != null) {
	            sb.append("=== INFORMACIÓN DE ESTACIONAMIENTO ===\n");
	            sb.append(String.format("%-12s: %d%n", "Lugar", estacionamiento.getNumParqueo()));
	            sb.append(String.format("%-12s: %s%n", "Ingreso", 
	                estacionamiento.getFechaIngreso().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
	            
	            if (estacionamiento.getFechaRetiro() != null) {
	                sb.append(String.format("%-12s: %s%n", "Retiro", 
	                    estacionamiento.getFechaRetiro().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
	                
	                long horas = java.time.Duration.between(
	                    estacionamiento.getFechaIngreso(), estacionamiento.getFechaRetiro()).toHours();
	                sb.append(String.format("%-12s: %d horas%n", "Duración", horas));
	            } else {
	                sb.append(String.format("%-12s: %s%n", "Estado", "EN ESTACIONAMIENTO"));
	            }
	        } else {
	            sb.append("No hay información de estacionamiento para este vehículo");
	        }
	    }
	    
	    txtResultado.setText(sb.toString());
	}
}