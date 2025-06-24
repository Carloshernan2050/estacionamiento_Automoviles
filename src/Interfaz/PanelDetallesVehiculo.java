package Interfaz;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import javax.imageio.ImageIO;
import javax.swing.*;
import DAO.*;
import mundo.*;

public class PanelDetallesVehiculo extends JPanel {
	
	private JTextArea txtDetalle;
	
    private JLabel lblImagen;
    
    
	 public PanelDetallesVehiculo() {
	        detallesVehiculo();
	        interfaz();
	    }

	    private void detallesVehiculo() {
	        setLayout(new BorderLayout(10, 10));
	        setBorder(BorderFactory.createTitledBorder("Detalles del Vehículo"));
	    }
	    
	    private void interfaz() {
	        
	        JPanel panelImagen = new JPanel(new BorderLayout());
	        panelImagen.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	        lblImagen = new JLabel(new ImageIcon(ajustarImagen("imagenes/vehiculo.png", 200, 150)));
	        lblImagen.setHorizontalAlignment(JLabel.CENTER);
	        panelImagen.add(lblImagen, BorderLayout.CENTER);

	        
	        JPanel panelTexto = new JPanel(new BorderLayout());
	        txtDetalle = new JTextArea(10, 30);
	        txtDetalle.setEditable(false);
	        txtDetalle.setLineWrap(true);
	        txtDetalle.setWrapStyleWord(true);
	        txtDetalle.setBackground(new Color(240, 240, 240));
	        txtDetalle.setFont(new Font("Monospaced", Font.PLAIN, 14));
	        
	        JScrollPane scrollDetalle = new JScrollPane(txtDetalle);
	        panelTexto.add(scrollDetalle, BorderLayout.CENTER);

	        
	        add(panelImagen, BorderLayout.WEST);
	        add(panelTexto, BorderLayout.CENTER);
	    }
	    
	    private Image ajustarImagen(String ruta, int ancho, int alto) {
	        try {
	            BufferedImage img = ImageIO.read(new File(ruta));
	            return img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
	        } catch (IOException e) {
	            return new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
	        }
	    }
	    public void mostrarDetalle(String placa) {
	        try {
	            StringBuilder sb = new StringBuilder();
	            
	            VehiculoDAO vDao = new VehiculoDAO();
	            EstacionamientoDAO eDao = new EstacionamientoDAO();
	            
	            Vehiculo v = vDao.buscarVehiculoPorPlaca(placa);
	            if (v != null) {
	                
	                String rutaImagen = v.getImagenUrl() != null ? v.getImagenUrl() : "imagenes/vehiculo.png";
	                lblImagen.setIcon(new ImageIcon(ajustarImagen(rutaImagen, 200, 150)));
	                
	               
	                sb.append(" informacion del vehiculo\n");
	                sb.append(String.format("%-12s: %s%n", "Placa", v.getPlaca()));
	                sb.append(String.format("%-12s: %s%n", "Marca", v.getMarca()));
	                sb.append(String.format("%-12s: %s%n", "Modelo", v.getModelo()));
	                sb.append(String.format("%-12s: %s%n", "Propietario", v.getPropietario()));
	                sb.append("\n");

	               
	                try {
	                    Estacionamiento e = eDao.buscarEstacionamientoPorPlaca(placa);
	                    if (e != null) {
	                        sb.append("=== ESTACIONAMIENTO ===\n");
	                        sb.append(String.format("%-12s: %s%n", "Lugar", e.getNumParqueo()));
	                        sb.append(String.format("%-12s: %s%n", "Ingreso", 
	                            e.getFechaIngreso().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));

	                        if (e.getFechaRetiro() != null) {
	                            sb.append(String.format("%-12s: %s%n", "Retiro", 
	                                e.getFechaRetiro().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));

	                            java.time.Duration duracion = java.time.Duration.between(
	                                e.getFechaIngreso(), e.getFechaRetiro());
	                            long totalMinutos = duracion.toMinutes();
	                            long horas = totalMinutos / 60;
	                            long minutos = totalMinutos % 60;
	                            sb.append(String.format("%-12s: %d h %d min%n", "Duración", horas, minutos));
	                        } else {
	                            sb.append(String.format("%-12s: %s%n", "Retiro", "EN ESTACIONAMIENTO"));
	                        }
	                    } else {
	                        sb.append("No se encontró información de estacionamiento.\n");
	                    }
	                } catch (SQLException ex) {
	                    sb.append("\nError al cargar datos de estacionamiento");
	                }
	            } else {
	                sb.append("No se encontró el vehículo con placa: " + placa);
	            }
	            
	            txtDetalle.setText(sb.toString());
	        } catch (SQLException e) {
	            txtDetalle.setText("Error al cargar detalles: " + e.getMessage());
	            lblImagen.setIcon(new ImageIcon(ajustarImagen("imagenes/vehiculo.png", 200, 150)));
	        }
	    }
}