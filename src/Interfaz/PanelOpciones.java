package Interfaz;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class PanelOpciones extends JPanel {
    private final PanelListaVehiculos panelLista;
    private final PanelDetallesVehiculo panelDetalle;

    public PanelOpciones(PanelListaVehiculos panelLista, PanelDetallesVehiculo panelDetalle)
    {
        this.panelLista = panelLista;
        this.panelDetalle = panelDetalle;
        
        VentanaOpciones();
        InicializarBotones();
    }
    
    private void VentanaOpciones() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }
    
    private void InicializarBotones() {
    	   
        JButton btnIngresar = crearBoton("Ingresar Vehículo", "add.png");
        btnIngresar.addActionListener(this::mostrarDialogoIngreso);
        

        JButton btnBuscar = crearBoton("Buscar Vehículo", "search.png");
        btnBuscar.addActionListener(this::mostrarDialogoBusqueda);
        
  
        JButton btnRetirar = crearBoton("Retirar Vehículo", "remove.png");
        btnRetirar.addActionListener(this::mostrarDialogoRetiro);
        
      
        JButton btnActualizar = crearBoton("Actualizar Lista", "refresh.png");
        btnActualizar.addActionListener(e -> panelLista.cargarVehiculos());
        
  
        JButton btnSalir = crearBoton("Salir", "exit.png");
        btnSalir.addActionListener(e -> System.exit(0));
        
  
        add(btnIngresar);
        add(btnBuscar);
        add(btnRetirar);
        add(btnActualizar);
        add(btnSalir);
    }
    
    private JButton crearBoton(String texto, String icono) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(180, 40));
        
        return boton;
    }

    private void mostrarDialogoIngreso(ActionEvent e) {
    	VentanaIngresoVehiculo  dialogo = new VentanaIngresoVehiculo (panelLista);
        dialogo.setVisible(true);
    }

    private void mostrarDialogoBusqueda(ActionEvent e) {
    	VentanaBusquedaVehiculo dialogo = new VentanaBusquedaVehiculo();
        dialogo.setVisible(true);
    }

    private void mostrarDialogoRetiro(ActionEvent e) {
        VentanaRetiroVehiculo dialogo = new VentanaRetiroVehiculo(panelLista);
        dialogo.setVisible(true);
    }
}