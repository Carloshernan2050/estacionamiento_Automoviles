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

import DAO.EstacionamientoDAO;
import DAO.VehiculoDAO;
import mundo.Estacionamiento;
import mundo.Vehiculo;

public class DialogoBusquedaVehiculo extends JDialog {
    
    public DialogoBusquedaVehiculo() {
        configurarDialogo();
        inicializarComponentes();
    }

    private void configurarDialogo() {
        setTitle("Buscar Vehículo");
        setSize(500, 400);
        setModal(true);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));
    }

    private void inicializarComponentes() {
        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBusqueda.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField txtPlaca = new JTextField(15);
        JButton btnBuscar = new JButton("Buscar");
        
        panelBusqueda.add(new JLabel("Placa:"));
        panelBusqueda.add(txtPlaca);
        panelBusqueda.add(btnBuscar);
        
        // Área de resultados
        JTextArea txtResultado = new JTextArea(10, 30);
        txtResultado.setEditable(false);
        txtResultado.setLineWrap(true);
        txtResultado.setWrapStyleWord(true);
        JScrollPane scrollResultado = new JScrollPane(txtResultado);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        panelBotones.add(btnCerrar);
        
        // Configurar acción de búsqueda
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
        
        // Agregar componentes al diálogo
        add(panelBusqueda, BorderLayout.NORTH);
        add(scrollResultado, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void buscarVehiculo(String placa, JTextArea txtResultado) throws SQLException {
        StringBuilder sb = new StringBuilder();
        
        VehiculoDAO vDao = new VehiculoDAO();
        Vehiculo vehiculo = vDao.buscarVehiculoPorPlaca(placa);
        
        if (vehiculo == null) {
            sb.append("No se encontró el vehículo con la placa: ").append(placa);
        } else {
            sb.append("información del vehiculo\n");
            sb.append(String.format("%-12s: %s%n", "Placa", vehiculo.getPlaca()));
            sb.append(String.format("%-12s: %s%n", "Marca", vehiculo.getMarca()));
            sb.append(String.format("%-12s: %s%n", "Modelo", vehiculo.getModelo()));
            sb.append(String.format("%-12s: %s%n", "Propietario", vehiculo.getPropietario()));
            sb.append("\n");
            
            // Buscar información de estacionamiento
            EstacionamientoDAO eDao = new EstacionamientoDAO();
            Estacionamiento estacionamiento = eDao.buscarEstacionamientoPorPlaca(placa);
            
            if (estacionamiento != null) {
                sb.append("inforación del estacionamiento\n");
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
                    sb.append(String.format("%-12s: %s%n", "Estado", "en el estacionamiento"));
                }
            } else {
                sb.append("No hay información de estacionamiento para este vehículo");
            }
        }
        
        txtResultado.setText(sb.toString());
    }
}