
package Vista;

import ControladorBBDD.Controlador;
import Modelo.Alumno;
import Modelo.Asignatura;
import Modelo.Matricula;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VentanaAgregarMatricula extends JFrame {
    private JComboBox<Alumno> cmbAlumno;
    private JComboBox<Asignatura> cmbAsignatura;
    private JTextField txtNota;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private VentanaPrincipal ventanaPrincipal;
    private Controlador controlador;

    public VentanaAgregarMatricula(VentanaPrincipal ventanaPrincipal, Controlador controlador) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.controlador = controlador;

        initComponentes();
        configurarVentana();
        cargarDatos();
    }

    private void initComponentes() {
        setTitle("Agregar Matrícula");
        setLayout(new BorderLayout());

        // Panel de formulario
        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Alumno
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelForm.add(new JLabel("Alumno:"), gbc);
        gbc.gridx = 1;
        cmbAlumno = new JComboBox<>();
        panelForm.add(cmbAlumno, gbc);

        // Asignatura
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelForm.add(new JLabel("Asignatura:"), gbc);
        gbc.gridx = 1;
        cmbAsignatura = new JComboBox<>();
        panelForm.add(cmbAsignatura, gbc);

        // Nota
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelForm.add(new JLabel("Nota:"), gbc);
        gbc.gridx = 1;
        txtNota = new JTextField(10);
        panelForm.add(txtNota, gbc);

        // Botones
        JPanel panelBotones = new JPanel();
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelForm, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // Eventos
        btnGuardar.addActionListener(e -> guardarMatricula());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void configurarVentana() {
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(ventanaPrincipal);
        setResizable(false);
    }

    private void cargarDatos() {
        List<Alumno> alumnos = controlador.obtenerAlumnos();
        for (Alumno a : alumnos) {
            cmbAlumno.addItem(a);
        }

        List<Asignatura> asignaturas = controlador.obtenerAsignaturas();
        for (Asignatura a : asignaturas) {
            cmbAsignatura.addItem(a);
        }
    }

    private void guardarMatricula() {
        Alumno alumno = (Alumno) cmbAlumno.getSelectedItem();
        Asignatura asignatura = (Asignatura) cmbAsignatura.getSelectedItem();
        String notaStr = txtNota.getText().trim();

        if (alumno == null || asignatura == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar alumno y asignatura", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        double nota = 0.0;
        try {
            nota = Double.parseDouble(notaStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La nota debe ser un número válido", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Matricula matricula = new Matricula(0, alumno, asignatura, nota);
        if (controlador.insertarMatricula(matricula)) {
            JOptionPane.showMessageDialog(this, "Matrícula guardada correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            ventanaPrincipal.cargarDatosMatriculas();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar matrícula", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
