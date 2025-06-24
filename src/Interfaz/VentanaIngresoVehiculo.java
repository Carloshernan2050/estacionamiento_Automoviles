package Interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import DAO.VehiculoDAO;
import mundo.Vehiculo;


public class VentanaIngresoVehiculo extends JDialog {
    private JTextField txtPlaca, txtMarca, txtModelo, txtPropietario;
    private JLabel lblImagen;
    private String rutaImagenSeleccionada;
    private final PanelListaVehiculos panelLista;

    public VentanaIngresoVehiculo(PanelListaVehiculos panelLista) {
        this.panelLista = panelLista;
        VentanaIngreso();

    }
    private void VentanaIngreso() {
        setTitle("Ingresar Vehículo");
        setSize(600, 400);
        setModal(true);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }
}