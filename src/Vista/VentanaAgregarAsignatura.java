
package Vista;

import ControladorBBDD.Controlador;
import Modelo.Asignatura;

import javax.swing.*;
import java.awt.*;

public class VentanaAgregarAsignatura extends JFrame {
    private JTextField txtNombre;
    private JTextField txtCurso;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private VentanaPrincipal ventanaPrincipal;
    private Controlador controlador;

    public VentanaAgregarAsignatura(VentanaPrincipal ventanaPrincipal, Controlador controlador) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.controlador = controlador;

        initComponentes();
        configurarVentana();
    }

    private void initComponentes() {
        setTitle("Agregar Asignatura");
        setLayout(new BorderLayout());

        // Panel de formulario
        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Nombre
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelForm.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(20);
        panelForm.add(txtNombre, gbc);

        // Curso
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelForm.add(new JLabel("Curso:"), gbc);
        gbc.gridx = 1;
        txtCurso = new JTextField(20);
        panelForm.add(txtCurso, gbc);

        // Botones
        JPanel panelBotones = new JPanel();
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelForm, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // Eventos
        btnGuardar.addActionListener(e -> guardarAsignatura());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void configurarVentana() {
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(ventanaPrincipal);
        setResizable(false);
    }

    private void guardarAsignatura() {
        String nombre = txtNombre.getText().trim();
        String cursoStr = txtCurso.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int curso = 0;
        try {
            curso = Integer.parseInt(cursoStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El curso debe ser un número", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Asignatura asignatura = new Asignatura(0, nombre, curso);
        if (controlador.agregarAsignatura(asignatura)) {
            JOptionPane.showMessageDialog(this, "Asignatura guardada correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            ventanaPrincipal.cargarDatosAsignaturas();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar asignatura", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
