package Interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import DAO.VehiculoDAO;
import mundo.Vehiculo;


public class VentanaIngresoVehiculo extends JDialog {
    private JTextField txtPlaca, txtMarca, txtModelo, txtPropietario;
    private JLabel lblImagen;
    private String rutaImagenSeleccionada;
    private final PanelListaVehiculos panelLista;

    public VentanaIngresoVehiculo(PanelListaVehiculos panelLista) {
        this.panelLista = panelLista;
        VentanaIngreso();
        InicializarCampos();
    }
    private void VentanaIngreso() {
        setTitle("Ingresar Vehículo");
        setSize(600, 400);
        setModal(true);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }
    
private void InicializarCampos() {
        
        JPanel panelImagen = new JPanel(new BorderLayout());
        lblImagen = new JLabel(new ImageIcon(redimensionarImagen("imagenes/vehiculo.png", 200, 150)));
        panelImagen.add(lblImagen, BorderLayout.CENTER);
        
        JButton btnSeleccionarImagen = new JButton("Seleccionar Imagen");
        btnSeleccionarImagen.addActionListener(this::seleccionarImagen);
        panelImagen.add(btnSeleccionarImagen, BorderLayout.SOUTH);

        
        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        txtPlaca = new JTextField();
        txtMarca = new JTextField();
        txtModelo = new JTextField();
        txtPropietario = new JTextField();
        
        panelFormulario.add(new JLabel("Placa:"));
        panelFormulario.add(txtPlaca);
        panelFormulario.add(new JLabel("Marca:"));
        panelFormulario.add(txtMarca);
        panelFormulario.add(new JLabel("Modelo:"));
        panelFormulario.add(txtModelo);
        panelFormulario.add(new JLabel("Propietario:"));
        panelFormulario.add(txtPropietario);

        
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.add(panelImagen, BorderLayout.WEST);
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);

       
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnGuardar.addActionListener(e -> guardarVehiculo());
        btnCancelar.addActionListener(e -> dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        
        add(panelPrincipal, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
	
	private void seleccionarImagen(ActionEvent e) {
	    JFileChooser fileChooser = new JFileChooser();
	    fileChooser.setDialogTitle("Seleccionar imagen del vehículo");
	    
	    fileChooser.setAcceptAllFileFilterUsed(false);
	    fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
	        public boolean accept(File f) {
	            if (f.isDirectory()) return true;
	            String name = f.getName().toLowerCase();
	            return name.endsWith(".jpg") || name.endsWith(".jpeg") || 
	                   name.endsWith(".png") || name.endsWith(".gif");
	        }
	        public String getDescription() {
	            return "Imágenes (*.jpg, *.png, *.gif)";
	        }
	    });
	    
	    int resultado = fileChooser.showOpenDialog(this);
	    if (resultado == JFileChooser.APPROVE_OPTION) {
	        File archivo = fileChooser.getSelectedFile();
	        try {
	            // Crear directorio si no existe
	            File directorioImagenes = new File("imagenes");
	            if (!directorioImagenes.exists()) {
	                directorioImagenes.mkdir();
	            }
	            
	        
	            String nombreArchivo = "vehiculo_" + System.currentTimeMillis() + "." + getExtension(archivo);
	            File destino = new File("imagenes/" + nombreArchivo);
	            
	            // Copiar la imagen al directorio
	            Files.copy(archivo.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
	            
	           
	            BufferedImage img = ImageIO.read(destino);
	            if (img != null) {
	                ImageIcon icono = new ImageIcon(redimensionarImagen(img, 200, 150));
	                lblImagen.setIcon(icono);
	                rutaImagenSeleccionada = "imagenes/" + nombreArchivo;
	            }
	        } catch (IOException ex) {
	            JOptionPane.showMessageDialog(this, 
	                "Error al copiar la imagen: " + ex.getMessage(),
	                "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    }
	}
	
	private String getExtension(File f) {
        String name = f.getName();
        int lastDot = name.lastIndexOf(".");
        if (lastDot > 0) {
            return name.substring(lastDot + 1).toLowerCase();
        }
        return "";
    }
}