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
}