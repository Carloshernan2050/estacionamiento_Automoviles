package Interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import DAO.EstacionamientoDAO;
import DAO.VehiculoDAO;
import mundo.Estacionamiento;
import mundo.Vehiculo;

/**
 * Panel que muestra una lista de vehículos registrados y su estado (estacionado o no).
 */
public class PanelListaVehiculos extends JPanel {

    private JList<String> listaVehiculos;
    private DefaultListModel<String> modeloLista;
    private PanelDetalleVehiculo panelDetalle;

    /**
     * Constructor del panel. Configura apariencia, componentes y carga los datos.
     */
    public PanelListaVehiculos() {
        configurarPanel();
        inicializarComponentes();
        cargarVehiculos();
    }

    /**
     * Configura el diseño y aspecto general del panel.
     */
    private void configurarPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(10, 15, 26)); // fondo oscuro
        setPreferredSize(new java.awt.Dimension(350, 0)); // ancho definido

        // Título del panel
        TitledBorder borde = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(60, 90, 120)), "Vehículos Registrados"
        );
        borde.setTitleColor(new Color(224, 224, 224)); // texto claro
        setBorder(borde);
    }

    /**
     * Inicializa la lista de vehículos y la configura visualmente.
     */
    private void inicializarComponentes() {
        modeloLista = new DefaultListModel<>();
        listaVehiculos = new JList<>(modeloLista);
        listaVehiculos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaVehiculos.setBackground(new Color(28, 31, 43)); // fondo oscuro
        listaVehiculos.setForeground(new Color(224, 224, 224)); // texto claro
        listaVehiculos.setSelectionBackground(new Color(70, 90, 120)); // color selección
        listaVehiculos.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        listaVehiculos.setFont(new Font("Monospaced", Font.PLAIN, 13));

        // Evento al seleccionar un vehículo
        listaVehiculos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && panelDetalle != null) {
                String seleccion = listaVehiculos.getSelectedValue();
                if (seleccion != null) {
                    String placa = seleccion.split(" - ")[0]; // se extrae la placa
                    panelDetalle.mostrarDetalle(placa); // se muestra en detalle
                }
            }
        });

        JScrollPane scroll = new JScrollPane(listaVehiculos);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(60, 90, 120)));

        add(scroll, BorderLayout.CENTER);
    }

    /**
     * Carga los vehículos desde la base de datos y los muestra en la lista.
     * Incluye el estado "Estacionado" si aplica.
     *
     * @return void. Muestra los vehículos en la lista del panel.
     */
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

                String textoLista = v.getPlaca() + " - " + v.getMarca() + " " + v.getModelo() + estado;
                modeloLista.addElement(textoLista);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar vehículos: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Establece el panel de detalle asociado para mostrar información detallada.
     *
     * @param panelDetalle PanelDetalleVehiculo que se actualizará al seleccionar un vehículo.
     */
    public void setPanelDetalle(PanelDetalleVehiculo panelDetalle) {
        this.panelDetalle = panelDetalle;
    }
}
