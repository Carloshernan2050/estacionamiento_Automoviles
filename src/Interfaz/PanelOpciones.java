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
    }
    
    private void VentanaOpciones() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }
}