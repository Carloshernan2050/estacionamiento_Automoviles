package Interfaz;

import java.awt.BorderLayout;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import DAO.EstacionamientoDAO;
import DAO.VehiculoDAO;
import mundo.Estacionamiento;
import mundo.Vehiculo;

public class PanelListaVehiculos extends JPanel {
    private JList<String> listaVehiculos;
    private DefaultListModel<String> modeloLista;
    private PanelDetalleVehiculo panelDetalle;

    public PanelListaVehiculos() {
        configurarPanel();
        inicializarComponentes();
        cargarVehiculos();
    }

    private void configurarPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Vehículos Registrados"));
    }

    private void inicializarComponentes() {
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

    public void cargarVehiculos() {
        modeloLista.clear();
        try {
            VehiculoDAO vDao = new VehiculoDAO();
            EstacionamientoDAO eDao = new EstacionamientoDAO();
            
            List<Vehiculo> vehiculos = vDao.obtenerTodosVehiculos();
            for (Vehiculo v : vehiculos) {
                Estacionamiento e = eDao.buscarEstacionamientoPorPlaca(v.getPlaca());
                String estado = (e != null && e.getFechaRetiro() == null) 
                    ? " - Estacionado (" + e.getFechaIngreso().format(DateTimeFormatter.ofPattern("HH:mm")) + ")"
                    : "";
                modeloLista.addElement(v.getPlaca() + " - " + v.getMarca() + " " + v.getModelo() + estado);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar vehículos: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setPanelDetalle(PanelDetalleVehiculo panelDetalle) {
        this.panelDetalle = panelDetalle;
    }
}