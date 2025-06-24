package Interfaz;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.*;

import DAO.EstacionamientoDAO;

public class DialogoRetiroVehiculo extends JDialog {
    private final PanelListaVehiculos panelLista;

    public DialogoRetiroVehiculo(PanelListaVehiculos panelLista) {
        this.panelLista = panelLista;
        configurarDialogo();
        inicializarComponentes();
    }

    private void configurarDialogo() {
        setTitle("Retirar Vehículo");
        setSize(450, 300);
        setModal(true);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));
    }

    private void inicializarComponentes() {
        JPanel panelFormulario = new JPanel(new GridLayout(2, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField txtPlaca = new JTextField();
        JTextField txtFechaRetiro = new JTextField(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        panelFormulario.add(new JLabel("Placa:"));
        panelFormulario.add(txtPlaca);
        panelFormulario.add(new JLabel("Fecha Retiro:"));
        panelFormulario.add(txtFechaRetiro);

        JTextArea txtResultado = new JTextArea(5, 30);
        txtResultado.setEditable(false);
        txtResultado.setLineWrap(true);
        txtResultado.setWrapStyleWord(true);
        JScrollPane scrollResultado = new JScrollPane(txtResultado);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnRetirar = new JButton("Retirar");
        JButton btnCancelar = new JButton("Cancelar");

        btnRetirar.addActionListener(e -> procesarRetiro(
                txtPlaca.getText(),
                txtFechaRetiro.getText(),
                txtResultado
        ));

        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnRetirar);
        panelBotones.add(btnCancelar);

        add(panelFormulario, BorderLayout.NORTH);
        add(scrollResultado, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void procesarRetiro(String placa, String fechaRetiroStr, JTextArea txtResultado) {
        try {
            if (placa.isEmpty() || fechaRetiroStr.isEmpty()) {
                throw new IllegalArgumentException("Placa y fecha de retiro son obligatorios");
            }

            LocalDateTime fechaRetiro;
            try {
                fechaRetiro = LocalDateTime.parse(fechaRetiroStr,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Formato de fecha inválido (yyyy-MM-dd HH:mm)");
            }

            EstacionamientoDAO eDao = new EstacionamientoDAO();

            mundo.Estacionamiento estacionamiento = eDao.buscarEstacionamientoPorPlaca(placa);
            if (estacionamiento == null || estacionamiento.getFechaRetiro() != null) {
                txtResultado.setText("No se encontró un vehículo estacionado actualmente con la placa: " + placa);
                return;
            }

            eDao.retirarVehiculo(placa, fechaRetiro);

            DAO.VehiculoDAO vDao = new DAO.VehiculoDAO();
            vDao.eliminarVehiculo(placa);

            panelLista.cargarVehiculos();

            StringBuilder sb = new StringBuilder();
            sb.append("Vehículo retirado correctamente\n\n");
            sb.append(String.format("%-12s: %s%n", "Placa", placa));
            sb.append(String.format("%-12s: %s%n", "Fecha retiro", fechaRetiroStr));

            JTextArea texto = new JTextArea(sb.toString());
            texto.setEditable(false);
            texto.setBackground(null);
            texto.setFont(new JLabel().getFont());

            JPanel panelMensaje = new JPanel(new BorderLayout());
            panelMensaje.add(texto, BorderLayout.CENTER);

            JOptionPane.showMessageDialog(
                    this,
                    panelMensaje,
                    "Retiro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } catch (IllegalArgumentException | SQLException e) {
            txtResultado.setText("Error al retirar vehículo: " + e.getMessage());
        }
    }

}
