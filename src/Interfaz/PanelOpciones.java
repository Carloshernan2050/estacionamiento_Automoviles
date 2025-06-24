package Interfaz;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class PanelOpciones extends JPanel {
    private final PanelListaVehiculos panelLista;
    private final PanelDetalleVehiculo panelDetalle;

    public PanelOpciones(PanelListaVehiculos panelLista, PanelDetalleVehiculo panelDetalle) {
        this.panelLista = panelLista;
        this.panelDetalle = panelDetalle;
        
        configurarPanel();
        inicializarComponentes();
    }

    private void configurarPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    private void inicializarComponentes() {
        // Botón Ingresar Vehículo
        JButton btnIngresar = crearBoton("Ingresar Vehículo", "add.png");
        btnIngresar.addActionListener(this::mostrarDialogoIngreso);
        
        // Botón Buscar Vehículo
        JButton btnBuscar = crearBoton("Buscar Vehículo", "search.png");
        btnBuscar.addActionListener(this::mostrarDialogoBusqueda);
        
        // Botón Retirar Vehículo
        JButton btnRetirar = crearBoton("Retirar Vehículo", "remove.png");
        btnRetirar.addActionListener(this::mostrarDialogoRetiro);
        
        
        // Botón Salir
        JButton btnSalir = crearBoton("Salir", "exit.png");
        btnSalir.addActionListener(e -> System.exit(0));
        
        // Agregar botones al panel
        add(btnIngresar);
        add(btnBuscar);
        add(btnRetirar);
        add(btnSalir);
    }

    private JButton crearBoton(String texto, String icono) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(180, 40));
        
        // Aquí podrías cargar el icono si lo tienes en recursos
        // boton.setIcon(new ImageIcon(getClass().getResource("/imagenes/" + icono)));
        
        return boton;
    }

    private void mostrarDialogoIngreso(ActionEvent e) {
        DialogoIngresoVehiculo dialogo = new DialogoIngresoVehiculo(panelLista);
        dialogo.setVisible(true);
    }

    private void mostrarDialogoBusqueda(ActionEvent e) {
        DialogoBusquedaVehiculo dialogo = new DialogoBusquedaVehiculo();
        dialogo.setVisible(true);
    }

    private void mostrarDialogoRetiro(ActionEvent e) {
        DialogoRetiroVehiculo dialogo = new DialogoRetiroVehiculo(panelLista);
        dialogo.setVisible(true);
    }
}