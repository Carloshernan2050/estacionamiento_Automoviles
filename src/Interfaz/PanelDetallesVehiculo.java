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
	
	 public PanelDetallesVehiculo() {
	        detallesVehiculo();
	    }

	    private void detallesVehiculo() {
	        setLayout(new BorderLayout(10, 10));
	        setBorder(BorderFactory.createTitledBorder("Detalles del Vehículo"));
	    }
}