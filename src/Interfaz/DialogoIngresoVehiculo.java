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

/**
 * Diálogo para ingresar un nuevo vehículo con sus datos e imagen asociada.
 * Almacena la información en la base de datos y actualiza la lista visual.
 */
public class DialogoIngresoVehiculo extends JDialog {

    private JTextField txtPlaca, txtMarca, txtModelo, txtPropietario;
    private JLabel lblImagen;
    private String rutaImagenSeleccionada;
    private final PanelListaVehiculos panelLista;

    /**
     * Constructor del diálogo de ingreso de vehículos.
     *
     * @param panelLista Panel de lista al que se añadirá el vehículo tras su creación.
     */
    public DialogoIngresoVehiculo(PanelListaVehiculos panelLista) {
        this.panelLista = panelLista;
        configurarDialogo();
        inicializarComponentes();
    }

    // Configuración general del diálogo
    private void configurarDialogo() {
        setTitle("Ingresar Vehículo");
        setSize(600, 400);
        setModal(true);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }

    // Inicialización de todos los componentes de la interfaz
    private void inicializarComponentes() {
        Font fuente = new Font("SansSerif", Font.PLAIN, 14);
        Color fondo = new Color(20, 24, 32);
        Color texto = Color.WHITE;
        Color campoFondo = new Color(35, 40, 50);
        Color borde = new Color(60, 70, 80);

        JPanel panelImagen = new JPanel(new BorderLayout());
        panelImagen.setBackground(fondo);
        lblImagen = new JLabel(new ImageIcon(redimensionarImagen("imagenes/vehiculo.png", 200, 150)));
        lblImagen.setHorizontalAlignment(JLabel.CENTER);
        panelImagen.add(lblImagen, BorderLayout.CENTER);

        JButton btnSeleccionarImagen = new JButton("Seleccionar Imagen");
        btnSeleccionarImagen.setFocusPainted(false);
        panelImagen.add(btnSeleccionarImagen, BorderLayout.SOUTH);
        btnSeleccionarImagen.addActionListener(this::seleccionarImagen);

        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 8, 8));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelFormulario.setBackground(fondo);

        txtPlaca = new JTextField();
        txtMarca = new JTextField();
        txtModelo = new JTextField();
        txtPropietario = new JTextField();

        JTextField[] campos = {txtPlaca, txtMarca, txtModelo, txtPropietario};
        for (JTextField campo : campos) {
            campo.setFont(fuente);
            campo.setBackground(campoFondo);
            campo.setForeground(texto);
            campo.setCaretColor(texto);
            campo.setBorder(BorderFactory.createLineBorder(borde));
        }

        JLabel[] etiquetas = {
            new JLabel("Placa:"), new JLabel("Marca:"),
            new JLabel("Modelo:"), new JLabel("Propietario:")
        };
        for (JLabel etiqueta : etiquetas) {
            etiqueta.setFont(fuente);
            etiqueta.setForeground(texto);
        }

        panelFormulario.add(etiquetas[0]); panelFormulario.add(txtPlaca);
        panelFormulario.add(etiquetas[1]); panelFormulario.add(txtMarca);
        panelFormulario.add(etiquetas[2]); panelFormulario.add(txtModelo);
        panelFormulario.add(etiquetas[3]); panelFormulario.add(txtPropietario);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBackground(fondo);
        panelPrincipal.add(panelImagen, BorderLayout.WEST);
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(fondo);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.setFocusPainted(false);
        btnCancelar.setFocusPainted(false);

        btnGuardar.setFont(fuente);
        btnCancelar.setFont(fuente);

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        btnGuardar.addActionListener(e -> guardarVehiculo());
        btnCancelar.addActionListener(e -> dispose());

        add(panelPrincipal, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Permite al usuario seleccionar una imagen desde su sistema.
     *
     * @param e Evento de acción del botón.
     */
    private void seleccionarImagen(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser("src/Imagenes/");
        fileChooser.setDialogTitle("Seleccionar imagen del vehículo");

        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                String name = f.getName().toLowerCase();
                return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".gif");
            }

            public String getDescription() {
                return "Imágenes (*.jpg, *.png, *.gif)";
            }
        });

        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try {
                File directorioImagenes = new File("imagenes");
                if (!directorioImagenes.exists()) {
                    directorioImagenes.mkdir();
                }

                String nombreArchivo = "vehiculo_" + System.currentTimeMillis() + "." + getExtension(archivo);
                File destino = new File("imagenes/" + nombreArchivo);
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

    /**
     * Obtiene la extensión del archivo.
     *
     * @param f Archivo del cual se extraerá la extensión.
     * @return Extensión del archivo (sin el punto), en minúscula.
     */
    private String getExtension(File f) {
        String name = f.getName();
        int lastDot = name.lastIndexOf(".");
        return (lastDot > 0) ? name.substring(lastDot + 1).toLowerCase() : "";
    }

    /**
     * Redimensiona imagen desde una ruta.
     *
     * @param ruta Ruta de la imagen.
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
     * Redimensiona imagen desde objeto BufferedImage.
     *
     * @param img Imagen original.
     * @param ancho Ancho deseado.
     * @param alto Alto deseado.
     * @return Imagen redimensionada.
     */
    private Image redimensionarImagen(BufferedImage img, int ancho, int alto) {
        return img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
    }

    /**
     * Guarda el vehículo ingresado y lo registra en la base de datos.
     */
    private void guardarVehiculo() {
        try {
            String placa = txtPlaca.getText().trim();
            String marca = txtMarca.getText().trim();
            String modelo = txtModelo.getText().trim();
            String propietario = txtPropietario.getText().trim();

            if (placa.isEmpty() || marca.isEmpty() || modelo.isEmpty() || propietario.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!placa.matches("[a-zA-Z0-9]+")) {
                JOptionPane.showMessageDialog(this, "La placa solo puede contener letras y números.", "Error de validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!propietario.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
                JOptionPane.showMessageDialog(this, "El nombre del propietario solo puede contener letras.", "Error de validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String resumen = "¿Deseas guardar el siguiente vehículo?\n\n" +
                             "Placa: " + placa + "\n" +
                             "Marca: " + marca + "\n" +
                             "Modelo: " + modelo + "\n" +
                             "Propietario: " + propietario;

            int confirmacion = JOptionPane.showConfirmDialog(this, resumen, "Confirmar registro", JOptionPane.YES_NO_OPTION);
            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            Vehiculo v = new Vehiculo(marca, modelo, placa, propietario);
            v.setImagenUrl(rutaImagenSeleccionada != null ? rutaImagenSeleccionada : "imagenes/vehiculo.png");

            VehiculoDAO vDao = new VehiculoDAO();
            vDao.insertarVehiculo(v);

            int numParqueo = asignarNumeroParqueo();
            mundo.Estacionamiento e = new mundo.Estacionamiento(
                java.time.LocalDateTime.now(), numParqueo, v, null
            );
            DAO.EstacionamientoDAO eDao = new DAO.EstacionamientoDAO();
            eDao.insertarEstacionamiento(e);

            panelLista.cargarVehiculos();
            dispose();

            JOptionPane.showMessageDialog(this, "Vehículo registrado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar vehículo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Asigna un número de parqueo (por ahora siempre devuelve 1).
     *
     * @return Número de parqueo asignado.
     */
    private int asignarNumeroParqueo() {
        return 1;
    }
}
