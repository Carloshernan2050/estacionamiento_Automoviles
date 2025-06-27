package Interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import javax.swing.*;

import DAO.EstacionamientoDAO;
import DAO.VehiculoDAO;
import mundo.Estacionamiento;
import mundo.Vehiculo;

/**
 * Diálogo que permite buscar un vehículo por su placa
 * y mostrar su información junto con el estado del estacionamiento.
 * 
 * Esta ventana se muestra como un JDialog modal.
 */
public class DialogoBusquedaVehiculo extends JDialog {

    /**
     * Constructor que configura e inicializa la interfaz del diálogo.
     */
    public DialogoBusquedaVehiculo() {
        configurarDialogo();
        inicializarComponentes();
    }

    /**
     * Configura las propiedades básicas del diálogo como tamaño, título y diseño.
     */
    private void configurarDialogo() {
        setTitle("Buscar Vehículo");
        setSize(500, 400);
        setModal(true);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));
    }

    /**
     * Inicializa todos los componentes visuales y define sus comportamientos.
     */
    private void inicializarComponentes() {
        // Estilos visuales
        Font fuente = new Font("SansSerif", Font.PLAIN, 14);
        Color fondo = new Color(20, 24, 32);
        Color texto = Color.WHITE;
        Color campoFondo = new Color(35, 40, 50);
        Color borde = new Color(60, 70, 80);

        // Panel para búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBusqueda.setBackground(fondo);
        panelBusqueda.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblPlaca = new JLabel("Placa:");
        lblPlaca.setForeground(texto);
        lblPlaca.setFont(fuente);

        JTextField txtPlaca = new JTextField(15);
        txtPlaca.setFont(fuente);
        txtPlaca.setBackground(campoFondo);
        txtPlaca.setForeground(texto);
        txtPlaca.setCaretColor(texto);
        txtPlaca.setBorder(BorderFactory.createLineBorder(borde));

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(fuente);
        btnBuscar.setFocusPainted(false);

        panelBusqueda.add(lblPlaca);
        panelBusqueda.add(txtPlaca);
        panelBusqueda.add(btnBuscar);

        // Área para mostrar el resultado
        JTextArea txtResultado = new JTextArea(10, 30);
        txtResultado.setEditable(false);
        txtResultado.setLineWrap(true);
        txtResultado.setWrapStyleWord(true);
        txtResultado.setFont(fuente);
        txtResultado.setBackground(campoFondo);
        txtResultado.setForeground(texto);
        txtResultado.setBorder(BorderFactory.createLineBorder(borde));

        JScrollPane scrollResultado = new JScrollPane(txtResultado);
        scrollResultado.setBorder(BorderFactory.createEmptyBorder());

        // Botón de cerrar
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.setBackground(fondo);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(fuente);
        btnCerrar.setFocusPainted(false);
        btnCerrar.addActionListener(e -> dispose());

        panelBotones.add(btnCerrar);

        // Acción del botón Buscar
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

        // Agregar los paneles al diálogo
        getContentPane().setBackground(fondo);
        add(panelBusqueda, BorderLayout.NORTH);
        add(scrollResultado, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Busca un vehículo en la base de datos por su placa y muestra los datos
     * del vehículo y su estado de estacionamiento en un área de texto.
     *
     * @param placa        La placa del vehículo a buscar.
     * @param txtResultado El área de texto donde se mostrarán los resultados.
     * @throws SQLException Si ocurre un error de base de datos.
     */
    private void buscarVehiculo(String placa, JTextArea txtResultado) throws SQLException {
        StringBuilder sb = new StringBuilder();

        VehiculoDAO vDao = new VehiculoDAO();
        Vehiculo vehiculo = vDao.buscarVehiculoPorPlaca(placa);

        if (vehiculo == null) {
            sb.append("No se encontró el vehículo con la placa: ").append(placa);
        } else {
            sb.append("Información del Vehículo\n");
            sb.append(String.format("%-12s: %s%n", "Placa", vehiculo.getPlaca()));
            sb.append(String.format("%-12s: %s%n", "Marca", vehiculo.getMarca()));
            sb.append(String.format("%-12s: %s%n", "Modelo", vehiculo.getModelo()));
            sb.append(String.format("%-12s: %s%n", "Propietario", vehiculo.getPropietario()));
            sb.append("\n");

            // Buscar información del estacionamiento
            EstacionamientoDAO eDao = new EstacionamientoDAO();
            Estacionamiento estacionamiento = eDao.buscarEstacionamientoPorPlaca(placa);

            if (estacionamiento != null) {
                sb.append("Información del Estacionamiento\n");
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
                    sb.append(String.format("%-12s: %s%n", "Estado", "En el estacionamiento"));
                }
            } else {
                sb.append("No hay información de estacionamiento para este vehículo");
            }
        }

        txtResultado.setText(sb.toString());
    }
}
