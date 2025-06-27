package Interfaz;

import javax.swing.*;
import java.sql.Connection;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import DAO.VehiculoDAO;
import mundo.Vehiculo;

/**
 * Diálogo que permite editar los datos de un vehículo existente,
 * incluyendo su marca, modelo, propietario e imagen asociada.
 */
public class DialogoEditarVehiculo extends JDialog {

    private JTextField txtMarca, txtModelo, txtPropietario;
    private JLabel lblImagen;
    private String rutaImagenSeleccionada;
    private final Vehiculo vehiculoOriginal;
    private final PanelDetalleVehiculo panelDetalle;

    /**
     * Constructor del diálogo de edición de vehículo.
     * 
     * @param vehiculo El vehículo a editar.
     * @param panelDetalle Referencia al panel para actualizar la vista tras los cambios.
     */
    public DialogoEditarVehiculo(Vehiculo vehiculo, PanelDetalleVehiculo panelDetalle) {
        this.vehiculoOriginal = vehiculo;
        this.panelDetalle = panelDetalle;
        configurarDialogo();
        inicializarComponentes();
    }

    /**
     * Configura las propiedades básicas de la ventana de diálogo.
     */
    private void configurarDialogo() {
        setTitle("Editar Vehículo");
        setSize(600, 400);
        setModal(true);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }

    /**
     * Inicializa todos los componentes visuales y funcionales del formulario.
     */
    private void inicializarComponentes() {
        Font fuente = new Font("SansSerif", Font.PLAIN, 14);
        Color fondo = new Color(20, 24, 32);
        Color texto = Color.WHITE;
        Color campoFondo = new Color(35, 40, 50);
        Color borde = new Color(60, 70, 80);

        // Panel de imagen
        JPanel panelImagen = new JPanel(new BorderLayout());
        panelImagen.setBackground(fondo);

        lblImagen = new JLabel(new ImageIcon(redimensionarImagen(vehiculoOriginal.getImagenUrl(), 200, 150)));
        lblImagen.setHorizontalAlignment(JLabel.CENTER);
        panelImagen.add(lblImagen, BorderLayout.CENTER);

        JButton btnSeleccionarImagen = new JButton("Cambiar Imagen");
        btnSeleccionarImagen.addActionListener(this::seleccionarImagen);
        panelImagen.add(btnSeleccionarImagen, BorderLayout.SOUTH);

        // Campos de entrada
        txtMarca = new JTextField(vehiculoOriginal.getMarca());
        txtModelo = new JTextField(vehiculoOriginal.getModelo());
        txtPropietario = new JTextField(vehiculoOriginal.getPropietario());

        JTextField[] campos = {txtMarca, txtModelo, txtPropietario};
        for (JTextField campo : campos) {
            campo.setFont(fuente);
            campo.setBackground(campoFondo);
            campo.setForeground(texto);
            campo.setCaretColor(texto);
            campo.setBorder(BorderFactory.createLineBorder(borde));
        }

        JLabel[] etiquetas = {
            new JLabel("Marca:"), new JLabel("Modelo:"), new JLabel("Propietario:")
        };
        for (JLabel etiqueta : etiquetas) {
            etiqueta.setFont(fuente);
            etiqueta.setForeground(texto);
        }

        JPanel panelFormulario = new JPanel(new GridLayout(3, 2, 8, 8));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelFormulario.setBackground(fondo);
        panelFormulario.add(etiquetas[0]); panelFormulario.add(txtMarca);
        panelFormulario.add(etiquetas[1]); panelFormulario.add(txtModelo);
        panelFormulario.add(etiquetas[2]); panelFormulario.add(txtPropietario);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBackground(fondo);
        panelPrincipal.add(panelImagen, BorderLayout.WEST);
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);

        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.addActionListener(e -> guardarCambios());

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(fondo);
        panelBotones.add(btnGuardar);

        add(panelPrincipal, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Abre un JFileChooser para seleccionar una nueva imagen del vehículo.
     * 
     * @param e Evento de acción del botón "Cambiar Imagen".
     */
    private void seleccionarImagen(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser("imagenes");
        fileChooser.setDialogTitle("Seleccionar nueva imagen");

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
                File directorio = new File("imagenes");
                if (!directorio.exists()) directorio.mkdir();

                String nombreArchivo = "vehiculo_editado_" + System.currentTimeMillis() + "." + getExtension(archivo);
                File destino = new File("imagenes/" + nombreArchivo);
                Files.copy(archivo.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);

                BufferedImage img = ImageIO.read(destino);
                if (img != null) {
                    lblImagen.setIcon(new ImageIcon(redimensionarImagen(img, 200, 150)));
                    rutaImagenSeleccionada = "imagenes/" + nombreArchivo;
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error al cargar la imagen: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Extrae la extensión de un archivo.
     * 
     * @param f Archivo del cual extraer la extensión.
     * @return Extensión del archivo en minúsculas.
     */
    private String getExtension(File f) {
        String name = f.getName();
        int lastDot = name.lastIndexOf(".");
        return (lastDot > 0) ? name.substring(lastDot + 1).toLowerCase() : "";
    }

    /**
     * Redimensiona una imagen desde una ruta dada.
     * 
     * @param ruta Ruta del archivo de imagen.
     * @param ancho Ancho deseado.
     * @param alto Alto deseado.
     * @return Imagen redimensionada.
     */
    private Image redimensionarImagen(String ruta, int ancho, int alto) {
        try {
            BufferedImage img = ImageIO.read(new File(ruta));
            return img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            return new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        }
    }

    /**
     * Redimensiona una imagen BufferedImage.
     * 
     * @param img Imagen en memoria.
     * @param ancho Ancho deseado.
     * @param alto Alto deseado.
     * @return Imagen redimensionada.
     */
    private Image redimensionarImagen(BufferedImage img, int ancho, int alto) {
        return img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
    }

    /**
     * Guarda los cambios realizados en los campos del vehículo
     * y actualiza la base de datos. También actualiza el panel de detalle.
     */
    private void guardarCambios() {
        String marca = txtMarca.getText().trim();
        String modelo = txtModelo.getText().trim();
        String propietario = txtPropietario.getText().trim();

        if (marca.isEmpty() || modelo.isEmpty() || propietario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!propietario.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            JOptionPane.showMessageDialog(this, "El nombre del propietario solo puede contener letras.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            vehiculoOriginal.setMarca(marca);
            vehiculoOriginal.setModelo(modelo);
            vehiculoOriginal.setPropietario(propietario);
            if (rutaImagenSeleccionada != null) {
                vehiculoOriginal.setImagenUrl(rutaImagenSeleccionada);
            }

            VehiculoDAO dao = new VehiculoDAO();
            dao.actualizarVehiculo(vehiculoOriginal);

            panelDetalle.mostrarDetalle(vehiculoOriginal.getPlaca());
            dispose();

            JOptionPane.showMessageDialog(this, "Datos actualizados correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
