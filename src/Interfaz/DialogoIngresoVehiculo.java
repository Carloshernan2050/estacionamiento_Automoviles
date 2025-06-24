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

public class DialogoIngresoVehiculo extends JDialog {
    private JTextField txtPlaca, txtMarca, txtModelo, txtPropietario;
    private JLabel lblImagen;
    private String rutaImagenSeleccionada;
    private final PanelListaVehiculos panelLista;

    public DialogoIngresoVehiculo(PanelListaVehiculos panelLista) {
        this.panelLista = panelLista;
        configurarDialogo();
        inicializarComponentes();
    }

    private void configurarDialogo() {
        setTitle("Ingresar Vehículo");
        setSize(600, 400);
        setModal(true);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }

    private void inicializarComponentes() {
        // Panel de imagen
        JPanel panelImagen = new JPanel(new BorderLayout());
        lblImagen = new JLabel(new ImageIcon(redimensionarImagen("imagenes/vehiculo.png", 200, 150)));
        panelImagen.add(lblImagen, BorderLayout.CENTER);
        
        JButton btnSeleccionarImagen = new JButton("Seleccionar Imagen");
        btnSeleccionarImagen.addActionListener(this::seleccionarImagen);
        panelImagen.add(btnSeleccionarImagen, BorderLayout.SOUTH);

        // Panel de formulario
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

        // Panel principal que combina imagen y formulario
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.add(panelImagen, BorderLayout.WEST);
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnGuardar.addActionListener(e -> guardarVehiculo());
        btnCancelar.addActionListener(e -> dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        // Agregar componentes al diálogo
        add(panelPrincipal, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void seleccionarImagen(ActionEvent e) {
    	JFileChooser fileChooser = new JFileChooser("C:/Users/ASUS/eclipse-workspace/estacionamiento_Automoviles/src/Imagenes");
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
                
                // Generar nombre único para la imagen
                String nombreArchivo = "vehiculo_" + System.currentTimeMillis() + "." + getExtension(archivo);
                File destino = new File("imagenes/" + nombreArchivo);
                
                // Copiar la imagen al directorio
                Files.copy(archivo.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
                
                // Mostrar la imagen
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

    private Image redimensionarImagen(String ruta, int ancho, int alto) {
        try {
            BufferedImage img = ImageIO.read(new File(ruta));
            return img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            return new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        }
    }

    private Image redimensionarImagen(BufferedImage img, int ancho, int alto) {
        return img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
    }

    private void guardarVehiculo() {
    try {
        String placa = txtPlaca.getText().trim();
        String marca = txtMarca.getText().trim();
        String modelo = txtModelo.getText().trim();
        String propietario = txtPropietario.getText().trim();

        if (placa.isEmpty() || marca.isEmpty() || modelo.isEmpty() || propietario.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Todos los campos son obligatorios", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear vehículo con la imagen (o la por defecto si no se seleccionó)
        Vehiculo v = new Vehiculo(marca, modelo, placa, propietario);
        v.setImagenUrl(rutaImagenSeleccionada != null ? rutaImagenSeleccionada : "imagenes/vehiculo.png");

        // Insertar en la base de datos
        VehiculoDAO vDao = new VehiculoDAO();
        vDao.insertarVehiculo(v);

        // --- NUEVO: Registrar el ingreso al estacionamiento ---
        int numParqueo = asignarNumeroParqueo(); // Implementa este método según tu lógica
        mundo.Estacionamiento e = new mundo.Estacionamiento(
            java.time.LocalDateTime.now(),
            numParqueo,
            v,
            null // fechaRetiro es null al ingresar
        );
        DAO.EstacionamientoDAO eDao = new DAO.EstacionamientoDAO();
        eDao.insertarEstacionamiento(e);

        // Actualizar lista y cerrar diálogo
        panelLista.cargarVehiculos();
        dispose();

        JOptionPane.showMessageDialog(this, 
            "Vehículo registrado exitosamente", 
            "Éxito", JOptionPane.INFORMATION_MESSAGE);
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, 
            "Error al guardar vehículo: " + ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}

// Método de ejemplo para asignar número de parqueadero
private int asignarNumeroParqueo() {
    // Puedes implementar aquí la lógica para asignar el número de parqueadero disponible
    // Por ejemplo, retornar un número fijo o consultar la base de datos por el primer lugar libre
    return 1; // Cambia esto por tu lógica real
}
}