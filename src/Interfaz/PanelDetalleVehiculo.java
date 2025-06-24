package Interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import DAO.EstacionamientoDAO;
import DAO.VehiculoDAO;
import mundo.Estacionamiento;
import mundo.Vehiculo;

public class PanelDetalleVehiculo extends JPanel {
    private JTextArea txtDetalle;
    private JLabel lblImagen;

    public PanelDetalleVehiculo() {
        configurarPanel();
        inicializarComponentes();
    }

    private void configurarPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Detalles del Vehículo"));
    }

    private void inicializarComponentes() {
        // Panel para la imagen
        JPanel panelImagen = new JPanel(new BorderLayout());
        panelImagen.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblImagen = new JLabel(new ImageIcon(redimensionarImagen("imagenes/vehiculo.png", 200, 150)));
        lblImagen.setHorizontalAlignment(JLabel.CENTER);
        panelImagen.add(lblImagen, BorderLayout.CENTER);

        // Panel para el texto
        JPanel panelTexto = new JPanel(new BorderLayout());
        txtDetalle = new JTextArea(10, 30);
        txtDetalle.setEditable(false);
        txtDetalle.setLineWrap(true);
        txtDetalle.setWrapStyleWord(true);
        txtDetalle.setBackground(new Color(240, 240, 240));
        txtDetalle.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollDetalle = new JScrollPane(txtDetalle);
        panelTexto.add(scrollDetalle, BorderLayout.CENTER);

        // Agregar componentes al panel principal
        add(panelImagen, BorderLayout.WEST);
        add(panelTexto, BorderLayout.CENTER);
    }

    private Image redimensionarImagen(String ruta, int ancho, int alto) {
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
            Estacionamiento e = eDao.buscarEstacionamientoPorPlaca(placa);

            if (v != null) {
                // Mostrar imagen del vehículo
                String rutaImagen = v.getImagenUrl() != null ? v.getImagenUrl() : "imagenes/vehiculo.png";
                lblImagen.setIcon(new ImageIcon(redimensionarImagen(rutaImagen, 200, 150)));

                // Información del vehículo
                sb.append(" Información del vehiculo \n");
                sb.append(String.format("%-12s: %s%n", "Placa", v.getPlaca()));
                sb.append(String.format("%-12s: %s%n", "Marca", v.getMarca()));
                sb.append(String.format("%-12s: %s%n", "Modelo", v.getModelo()));
                sb.append(String.format("%-12s: %s%n", "Propietario", v.getPropietario()));
                sb.append("\n");

                // Información del estacionamiento
                if (e != null && e.getFechaRetiro() == null) {
                    sb.append(" Lugar de estacionamiento actual \n");
                    sb.append(String.format("%-20s: %d%n", "Número de estacionamiento", e.getNumParqueo()));
                    sb.append(String.format("%-20s: %s%n", "Hora de ingreso",
                            e.getFechaIngreso().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));

                    java.time.Duration tiempoEnParqueo = e.calcularTiempoEnParqueo();
                    long horas = tiempoEnParqueo.toHours();
                    long minutos = tiempoEnParqueo.toMinutes() % 60;
                    sb.append(String.format("%-20s: %d h %d min%n", "Tiempo en parqueo", horas, minutos));
                } else if (e != null && e.getFechaRetiro() != null) {
                    sb.append(" Ultimo estacionamiento \n");
                    sb.append(String.format("%-20s: %d%n", "Número de estacionamiento", e.getNumParqueo()));
                    sb.append(String.format("%-20s: %s%n", "Hora de ingreso",
                            e.getFechaIngreso().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
                    sb.append(String.format("%-20s: %s%n", "Hora de retiro",
                            e.getFechaRetiro().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));

                    java.time.Duration duracion = e.calcularDuracionTotal();
                    long horas = duracion.toHours();
                    long minutos = duracion.toMinutes() % 60;
                    sb.append(String.format("%-20s: %d h %d min%n", "Duración", horas, minutos));
                } else {
                    sb.append("No se encontró información de estacionamiento.\n");
                }
            } else {
                sb.append("No se encontró el vehículo con placa: " + placa);
            }

            txtDetalle.setText(sb.toString());
        } catch (SQLException e) {
            txtDetalle.setText("Error al cargar detalles: " + e.getMessage());
            lblImagen.setIcon(new ImageIcon(redimensionarImagen("imagenes/vehiculo.png", 200, 150)));
        }
    }
}
