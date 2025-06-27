package Interfaz;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 * Panel inferior que contiene los botones principales de acción:
 * ingresar, buscar, retirar vehículo y salir del sistema.
 */
public class PanelOpciones extends JPanel {

    // Referencias a otros paneles para interacción
    private final PanelListaVehiculos panelLista;
    private final PanelDetalleVehiculo panelDetalle;

    /**
     * Constructor del panel de opciones.
     *
     * @param panelLista Panel donde se muestra la lista de vehículos.
     * @param panelDetalle Panel donde se muestran los detalles del vehículo seleccionado.
     */
    public PanelOpciones(PanelListaVehiculos panelLista, PanelDetalleVehiculo panelDetalle) {
        this.panelLista = panelLista;
        this.panelDetalle = panelDetalle;

        configurarPanel();
        inicializarComponentes();
    }

    /**
     * Estilo general del panel.
     */
    private void configurarPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        setBackground(new Color(10, 15, 26)); // fondo oscuro
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    /**
     * Inicializa los botones de acción y define sus comportamientos.
     */
    private void inicializarComponentes() {
        // Botón para ingresar un nuevo vehículo
        JButton btnIngresar = crearBoton("Ingresar Vehículo");
        btnIngresar.addActionListener(this::mostrarDialogoIngreso);

        // Botón para buscar un vehículo
        JButton btnBuscar = crearBoton("Buscar Vehículo");
        btnBuscar.addActionListener(this::mostrarDialogoBusqueda);

        // Botón para retirar un vehículo
        JButton btnRetirar = crearBoton("Retirar Vehículo");
        btnRetirar.addActionListener(this::mostrarDialogoRetiro);

        // Botón para salir del sistema
        JButton btnSalir = crearBoton("Salir");
        btnSalir.addActionListener(e -> System.exit(0));

        // Agregar los botones al panel
        add(btnIngresar);
        add(btnBuscar);
        add(btnRetirar);
        add(btnSalir);
    }

    /**
     * Crea un botón estilizado con el texto indicado.
     *
     * @param texto Texto a mostrar en el botón.
     * @return JButton estilizado.
     */
    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(180, 40));
        boton.setFocusPainted(false);
        boton.setBackground(Color.WHITE); // fondo blanco
        boton.setForeground(new Color(30, 30, 30)); // texto oscuro
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 80))); // borde elegante
        return boton;
    }

    /**
     * Muestra el diálogo para registrar un nuevo vehículo.
     *
     * @param e Evento de acción generado por el botón.
     */
    private void mostrarDialogoIngreso(ActionEvent e) {
        DialogoIngresoVehiculo dialogo = new DialogoIngresoVehiculo(panelLista);
        dialogo.setVisible(true);
    }

    /**
     * Muestra el diálogo para buscar vehículos.
     *
     * @param e Evento de acción generado por el botón.
     */
    private void mostrarDialogoBusqueda(ActionEvent e) {
        DialogoBusquedaVehiculo dialogo = new DialogoBusquedaVehiculo();
        dialogo.setVisible(true);
    }

    /**
     * Muestra el diálogo para registrar el retiro de un vehículo.
     *
     * @param e Evento de acción generado por el botón.
     */
    private void mostrarDialogoRetiro(ActionEvent e) {
        DialogoRetiroVehiculo dialogo = new DialogoRetiroVehiculo(panelLista);
        dialogo.setVisible(true);
    }
}
