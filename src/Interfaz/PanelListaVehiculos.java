package Interfaz;

import java.awt.BorderLayout;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import DAO.VehiculoDAO;

public class PanelListaVehiculos extends JPanel {
    private JList<String> listaVehiculos;
    private DefaultListModel<String> modeloLista;
    private PanelDetallesVehiculo panelDetalle;

    public PanelListaVehiculos() {
        configurarPanel();
        inicializarCampos();
    }

    private void configurarPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Vehículos Registrados"));
    }

    private void inicializarCampos() {
        modeloLista = new DefaultListModel<>();
        listaVehiculos = new JList<>(modeloLista);
        listaVehiculos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        listaVehiculos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && panelDetalle != null) {
                String seleccion = listaVehiculos.getSelectedValue();
                if (seleccion != null) {
                    String placa = seleccion.split(" - ")[0];
                    panelDetalle.mostrarDetalle(placa);
                }
            }
        });
        
        add(new JScrollPane(listaVehiculos), BorderLayout.CENTER);
    }
}