package Interfaz;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;
import javax.swing.*;

import DAO.EstacionamientoDAO;
import DAO.VehiculoDAO;
import mundo.Estacionamiento;
import mundo.Vehiculo;

/**
 * Panel que muestra los detalles del vehículo seleccionado.
 * Incluye imagen, datos del vehículo y su estado en el parqueadero.
 */
public class PanelDetalleVehiculo extends JPanel {
    private JTextArea txtDetalle;
    private JLabel lblImagen;
    private Vehiculo vehiculoActual;

    /**
     * Constructor. Configura y prepara el panel.
     */
    public PanelDetalleVehiculo() {
        configurarPanel();
        inicializarComponentes();
    }

    /**
     * Configura el aspecto general del panel.
     */
    private void configurarPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(60, 90, 120)),
            "Detalles del Vehículo",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("SansSerif", Font.BOLD, 14),
            new Color(200, 220, 255) // color del título
        ));
        setBackground(new Color(10, 15, 26)); // fondo oscuro
    }

    /**
     * Inicializa los componentes visuales del panel: imagen, texto y botón.
     */
    private void inicializarComponentes() {
        // Panel de imagen del vehículo
        JPanel panelImagen = new JPanel(new BorderLayout());
        panelImagen.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelImagen.setBackground(new Color(10, 15, 26));

        lblImagen = new JLabel(new ImageIcon(redimensionarImagen("imagenes/vehiculo.png", 500, 350)));
        lblImagen.setPreferredSize(new Dimension(500, 350));
        lblImagen.setHorizontalAlignment(JLabel.CENTER);
        panelImagen.add(lblImagen, BorderLayout.CENTER);

        // Panel de texto con detalles
        JPanel panelTexto = new JPanel(new BorderLayout());
        panelTexto.setBackground(new Color(10, 15, 26));

        txtDetalle = new JTextArea(10, 30);
        txtDetalle.setEditable(false);
        txtDetalle.setLineWrap(true);
        txtDetalle.setWrapStyleWord(true);
        txtDetalle.setBackground(new Color(30, 30, 47));
        txtDetalle.setForeground(new Color(220, 240, 255));
        txtDetalle.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txtDetalle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollDetalle = new JScrollPane(txtDetalle);
        scrollDetalle.setBorder(BorderFactory.createLineBorder(new Color(60, 90, 120)));
        panelTexto.add(scrollDetalle, BorderLayout.CENTER);

        // Botón para editar el vehículo
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(new Color(10, 15, 26));

        JButton btnEditar = new JButton("Editar Datos");
        btnEditar.setFocusPainted(false);
        btnEditar.setBackground(new Color(80, 120, 180));
        btnEditar.setForeground(new Color(30, 30, 47));
        btnEditar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnEditar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEditar.addActionListener(e -> editarVehiculo());

        panelBotones.add(btnEditar);
        panelTexto.add(panelBotones, BorderLayout.SOUTH);

        // Agregar secciones al panel principal
        add(panelImagen, BorderLayout.WEST);
        add(panelTexto, BorderLayout.CENTER);
    }

    /**
     * Redimensiona una imagen desde un archivo dado.
     *
     * @param ruta Ruta de la imagen a redimensionar.
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
     * Abre el diálogo para editar los datos del vehículo actualmente mostrado.
     */
    private void editarVehiculo() {
        if (vehiculoActual != null) {
            DialogoEditarVehiculo dialogo = new DialogoEditarVehiculo(vehiculoActual, this);
            dialogo.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                "No hay un vehículo seleccionado para editar.",
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Muestra los detalles del vehículo especificado por su placa.
     *
     * @param placa Placa del vehículo a mostrar.
     */
    public void mostrarDetalle(String placa) {
        try {
            StringBuilder sb = new StringBuilder();

            VehiculoDAO vDao = new VehiculoDAO();
            EstacionamientoDAO eDao = new EstacionamientoDAO();

            vehiculoActual = vDao.buscarVehiculoPorPlaca(placa);
            Estacionamiento e = eDao.buscarEstacionamientoPorPlaca(placa);

            if (vehiculoActual != null) {
                // Imagen del vehículo
                String rutaImagen = (vehiculoActual.getImagenUrl() != null)
                        ? vehiculoActual.getImagenUrl()
                        : "imagenes/vehiculo.png";
                lblImagen.setIcon(new ImageIcon(redimensionarImagen(rutaImagen, 500, 350)));

                // Datos básicos del vehículo
                sb.append(" Información del Vehículo\n");
                sb.append(String.format("%-12s: %s%n", "Placa", vehiculoActual.getPlaca()));
                sb.append(String.format("%-12s: %s%n", "Marca", vehiculoActual.getMarca()));
                sb.append(String.format("%-12s: %s%n", "Modelo", vehiculoActual.getModelo()));
                sb.append(String.format("%-12s: %s%n", "Propietario", vehiculoActual.getPropietario()));
                sb.append("\n");

                // Información del estacionamiento actual o último
                if (e != null && e.getFechaRetiro() == null) {
                    sb.append(" Estacionamiento actual\n");
                    sb.append(String.format("%-20s: %d%n", "Número de estacionamiento", e.getNumParqueo()));
                    sb.append(String.format("%-20s: %s%n", "Hora de ingreso",
                            e.getFechaIngreso().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
                    java.time.Duration tiempo = e.calcularTiempoEnParqueo();
                    sb.append(String.format("%-20s: %d h %d min%n", "Tiempo en parqueo",
                            tiempo.toHours(), tiempo.toMinutes() % 60));
                } else if (e != null) {
                    sb.append(" Último estacionamiento\n");
                    sb.append(String.format("%-20s: %d%n", "Número de estacionamiento", e.getNumParqueo()));
                    sb.append(String.format("%-20s: %s%n", "Hora de ingreso",
                            e.getFechaIngreso().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
                    sb.append(String.format("%-20s: %s%n", "Hora de retiro",
                            e.getFechaRetiro().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
                    java.time.Duration duracion = e.calcularDuracionTotal();
                    sb.append(String.format("%-20s: %d h %d min%n", "Duración",
                            duracion.toHours(), duracion.toMinutes() % 60));
                } else {
                    sb.append("No se encontró información de estacionamiento.\n");
                }
            } else {
                sb.append("No se encontró el vehículo con placa: " + placa);
            }

            txtDetalle.setText(sb.toString());

        } catch (SQLException e) {
            txtDetalle.setText("Error al cargar detalles: " + e.getMessage());
            lblImagen.setIcon(new ImageIcon(redimensionarImagen("imagenes/vehiculo.png", 500, 350)));
        }
    }
}
