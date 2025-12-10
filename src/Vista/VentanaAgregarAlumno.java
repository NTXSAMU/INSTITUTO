package Vista;




import ControladorBBDD.Controlador;
import Modelo.Alumno;

import javax.swing.*;
import java.awt.*;

public class VentanaAgregarAlumno extends JFrame {
    private VentanaPrincipal ventanaPrincipal;
    private Controlador controlador;
    private JTextField txtNombre, txtDireccion, txtEstadoMatricula;
    private JCheckBox chkCarnet;
    private JButton btnGuardar, btnCancelar;

    public VentanaAgregarAlumno(VentanaPrincipal ventanaPrincipal, Controlador controlador) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.controlador = controlador;
        setTitle("Agregar Alumno");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(ventanaPrincipal);

        // Componentes
        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panel.add(txtNombre);

        panel.add(new JLabel("Dirección:"));
        txtDireccion = new JTextField();
        panel.add(txtDireccion);

        panel.add(new JLabel("Estado Matrícula:"));
        txtEstadoMatricula = new JTextField();
        panel.add(txtEstadoMatricula);

        panel.add(new JLabel("Carnet de Conducir:"));
        chkCarnet = new JCheckBox();
        panel.add(chkCarnet);

        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        panel.add(btnGuardar);
        panel.add(btnCancelar);

        add(panel);

        // Acciones
        btnGuardar.addActionListener(e -> guardarAlumno());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void guardarAlumno() {
        String nombre = txtNombre.getText();
        String direccion = txtDireccion.getText();
        String estado = txtEstadoMatricula.getText();
        boolean carnet = chkCarnet.isSelected();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.");
            return;
        }

        Alumno alumno = new Alumno(0, nombre, direccion, estado, carnet ? 1 : 0);
        if (controlador.agregarAlumno(alumno)) {
            JOptionPane.showMessageDialog(this, "Alumno guardado exitosamente.");
            ventanaPrincipal.cargarDatosAlumnos();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar alumno.");
        }
    }
}
