package Interfaz;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import DAO.EstacionamientoDAO;

public class VentanaRetiroVehiculo extends JDialog {
	private final PanelListaVehiculos panelLista;

    public VentanaRetiroVehiculo(PanelListaVehiculos panelLista) {
        this.panelLista = panelLista;
        ventanaRetiro();
        InicializarCampos();
    }

    private void ventanaRetiro() {
        setTitle("Retirar Vehículo");
        setSize(450, 300);
        setModal(true);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));
    }
    
    private void InicializarCampos() {
        
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
}