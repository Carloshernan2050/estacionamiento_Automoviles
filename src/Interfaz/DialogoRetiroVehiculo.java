package Interfaz;

import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.*;

import DAO.EstacionamientoDAO;

/**
 * Diálogo que permite retirar un vehículo del estacionamiento.
 * Incluye validación, confirmación y actualización de la lista.
 */
public class DialogoRetiroVehiculo extends JDialog {
    private final PanelListaVehiculos panelLista;

    /**
     * Constructor que recibe el panel de lista para actualizarlo tras el retiro.
     * 
     * @param panelLista Panel de lista que debe actualizarse al retirar un vehículo.
     */
    public DialogoRetiroVehiculo(PanelListaVehiculos panelLista) {
        this.panelLista = panelLista;
        configurarDialogo();
        inicializarComponentes();
    }

    /**
     * Configura la apariencia básica del diálogo.
     */
    private void configurarDialogo() {
        setTitle("Retirar Vehículo");
        setSize(480, 300);
        setModal(true); // bloquea otras ventanas hasta que se cierre
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));
        getContentPane().setBackground(new Color(15, 20, 30));
    }

    /**
     * Inicializa y organiza los componentes gráficos del diálogo.
     */
    private void inicializarComponentes() {
        Font fuente = new Font("SansSerif", Font.PLAIN, 14);
        Color fondo = new Color(25, 30, 40);
        Color texto = Color.WHITE;

        // Panel de formulario
        JPanel panelFormulario = new JPanel(new GridLayout(2, 2, 8, 8));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panelFormulario.setBackground(fondo);

        // Etiquetas
        JLabel lblPlaca = new JLabel("Placa:");
        JLabel lblFecha = new JLabel("Fecha Retiro:");

        // Estilo de etiquetas
        lblPlaca.setForeground(texto);
        lblFecha.setForeground(texto);
        lblPlaca.setFont(fuente);
        lblFecha.setFont(fuente);

        // Campos de texto
        JTextField txtPlaca = new JTextField();
        JTextField txtFechaRetiro = new JTextField(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );
        txtFechaRetiro.setFont(fuente);
        txtFechaRetiro.setEditable(false); // evita que el usuario lo modifique
        txtFechaRetiro.setBackground(new Color(35, 40, 50));
        txtFechaRetiro.setForeground(Color.WHITE);
        txtFechaRetiro.setBorder(BorderFactory.createLineBorder(new Color(60, 70, 80)));

        // Agrega componentes al panel de formulario
        panelFormulario.add(lblPlaca);
        panelFormulario.add(txtPlaca);
        panelFormulario.add(lblFecha);
        panelFormulario.add(txtFechaRetiro);

        // Área para mensajes o errores
        JTextArea txtResultado = new JTextArea(4, 30);
        txtResultado.setEditable(false);
        txtResultado.setLineWrap(true);
        txtResultado.setWrapStyleWord(true);
        txtResultado.setBackground(fondo);
        txtResultado.setForeground(Color.ORANGE);
        txtResultado.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollResultado = new JScrollPane(txtResultado);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelBotones.setBackground(new Color(20, 25, 35));

        JButton btnRetirar = new JButton("Retirar");
        JButton btnCancelar = new JButton("Cancelar");

        btnRetirar.setFocusPainted(false);
        btnCancelar.setFocusPainted(false);

        // Acciones
        btnRetirar.addActionListener(e -> procesarRetiro(
            txtPlaca.getText(), txtFechaRetiro.getText(), txtResultado
        ));
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnRetirar);
        panelBotones.add(btnCancelar);

        // Agregar a la ventana
        add(panelFormulario, BorderLayout.NORTH);
        add(scrollResultado, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Lógica para procesar el retiro del vehículo.
     * Valida entrada, confirma con el usuario y actualiza la base de datos.
     *
     * @param placa Placa del vehículo a retirar.
     * @param fechaRetiroStr Fecha de retiro en formato texto.
     * @param txtResultado Área de texto donde se muestran los mensajes de estado.
     */
    private void procesarRetiro(String placa, String fechaRetiroStr, JTextArea txtResultado) {
        try {
            if (placa.isEmpty() || fechaRetiroStr.isEmpty()) {
                throw new IllegalArgumentException("Placa y fecha de retiro son obligatorios");
            }

            // Validar y convertir fecha
            LocalDateTime fechaRetiro;
            try {
                fechaRetiro = LocalDateTime.parse(fechaRetiroStr,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Formato de fecha inválido (yyyy-MM-dd HH:mm)");
            }

            // Confirmación antes de proceder
            int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de retirar el vehículo con placa: " + placa + "?",
                "Confirmar retiro",
                JOptionPane.YES_NO_OPTION
            );

            if (confirmacion != JOptionPane.YES_OPTION) {
                return; // usuario canceló
            }

            // Ejecutar retiro
            EstacionamientoDAO eDao = new EstacionamientoDAO();
            eDao.retirarVehiculoYEliminarDatos(placa, fechaRetiro);

            // Actualizar la lista lateral
            panelLista.cargarVehiculos();

            // Mostrar mensaje de éxito
            StringBuilder sb = new StringBuilder();
            sb.append("Vehículo retirado correctamente\n\n");
            sb.append(String.format("%-12s: %s%n", "Placa", placa));
            sb.append(String.format("%-12s: %s%n", "Fecha retiro", fechaRetiroStr));

            JTextArea texto = new JTextArea(sb.toString());
            texto.setEditable(false);
            texto.setBackground(new Color(245, 245, 245));
            texto.setFont(new Font("SansSerif", Font.PLAIN, 13));

            JOptionPane.showMessageDialog(
                this,
                texto,
                "Retiro exitoso",
                JOptionPane.INFORMATION_MESSAGE
            );

            dispose(); // cerrar diálogo

        } catch (IllegalArgumentException | SQLException e) {
            txtResultado.setText("Error al retirar vehículo: " + e.getMessage());
        }
    }
}
