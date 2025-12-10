
package Vista;

import ControladorBBDD.Controlador;
import Modelo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    private Controlador controlador;
    private JTable tablaAlumnos;
    private JTable tablaAsignaturas;
    private JTable tablaMatriculas;

    // Componentes de la GUI
    private JTabbedPane menuInterior;
    private JSplitPane panelDivisorCentral;
    private JPanel panelPrincipal;
    private JMenuBar menuBar;
    private JMenu menuAlumno, menuAsignatura, menuMatricula, menuVista;
    private JMenuItem menuItemAgregarAlumno, menuItemEliminarAlumno;
    private JMenuItem menuItemAgregarAsignatura, menuItemEliminarAsignatura;
    private JMenuItem menuItemAgregarMatricula, menuItemEliminarMatricula;
    private JCheckBoxMenuItem menuItemVista;

    // Iconos
    private ImageIcon iconPapel, iconAsignatura, iconAlumno, iconMatricula;

    // PDF - Nuevo sistema
    private VisorPDF visorPDF;
    private GestorPDF gestorPDF;

    public VentanaPrincipal() {
        EstiloAplicacion.aplicarEstilo();
        controlador = new Controlador();

        setTitle("Sistema Escolar - Ventana Principal");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initGUI();

        // Cargar datos iniciales
        cargarDatosAlumnos();
        cargarDatosAsignaturas();
        cargarDatosMatriculas();
    }

    public void initGUI() {
        // Cargar y escalar iconos
        iconPapel = new ImageIcon("src/main/java/MM/Layouts/Práctica6/Imagenes/papellapiz.png");
        iconAsignatura = new ImageIcon("src/main/java/MM/Layouts/Práctica6/Imagenes/asignatura.png");
        iconAlumno = new ImageIcon("src/main/java/MM/Layouts/Práctica6/Imagenes/alumno.png");
        iconMatricula = new ImageIcon("src/main/java/MM/Layouts/Práctica6/Imagenes/matricula.png");

        iconAlumno = new ImageIcon(iconAlumno.getImage().getScaledInstance(36, 36, Image.SCALE_DEFAULT));
        iconAsignatura = new ImageIcon(iconAsignatura.getImage().getScaledInstance(36, 36, Image.SCALE_DEFAULT));
        iconMatricula = new ImageIcon(iconMatricula.getImage().getScaledInstance(36, 36, Image.SCALE_DEFAULT));
        iconPapel = new ImageIcon(iconPapel.getImage().getScaledInstance(36, 36, Image.SCALE_DEFAULT));

        // Crear paneles para las tablas
        JPanel panelTablaAlumnos = crearPanelAlumnos();
        JPanel panelTablaAsignaturas = crearPanelAsignaturas();
        JPanel panelTablaMatriculas = crearPanelMatriculas();

        // Configurar JTabbedPane
        menuInterior = new JTabbedPane();
        menuInterior.addTab("Alumnos", iconAlumno, panelTablaAlumnos);
        menuInterior.addTab("Asignatura", iconAsignatura, panelTablaAsignaturas);
        menuInterior.addTab("Matricula", iconMatricula, panelTablaMatriculas);

        // Configurar JSplitPane
        panelDivisorCentral = new JSplitPane();
        panelDivisorCentral.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        panelDivisorCentral.setDividerSize(5);
        panelDivisorCentral.setDividerLocation(200);
        panelDivisorCentral.setResizeWeight(0.0);

        // Panel izquierdo con visor PDF
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setBackground(Color.LIGHT_GRAY);

        // Crear el visor PDF
        visorPDF = new VisorPDF();

        // Crear el gestor PDF
        gestorPDF = new GestorPDF(visorPDF, this);

        // Crear panel de botones para PDF
        JPanel panelBotonesPDF = new JPanel(new FlowLayout());

        JButton btnCargarPDF = new JButton("Cargar PDF");
        btnCargarPDF.setToolTipText("Cargar un archivo PDF para visualizar");
        btnCargarPDF.addActionListener(e -> gestorPDF.cargarPDFConDialogo());

        JButton btnCerrarPDF = new JButton("Cerrar");
        btnCerrarPDF.setToolTipText("Cerrar el PDF actual");
        btnCerrarPDF.addActionListener(e -> gestorPDF.cerrarDocumento());

        panelBotonesPDF.add(btnCargarPDF);
        panelBotonesPDF.add(btnCerrarPDF);

        // Añadir componentes al panel izquierdo
        panelIzquierdo.add(panelBotonesPDF, BorderLayout.NORTH);
        panelIzquierdo.add(visorPDF, BorderLayout.CENTER);

        panelDivisorCentral.setLeftComponent(panelIzquierdo);

        // Configurar el panel derecho (tablas)
        panelDivisorCentral.setRightComponent(menuInterior);

        // Añadir el split pane al panel principal
        panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.add(panelDivisorCentral, BorderLayout.CENTER);

        // Crear barra de herramientas
        crearBarraHerramientas();

        // Añadir panel principal a la ventana
        add(panelPrincipal);

        // Mostrar ventana
        setVisible(true);
    }

    private void crearMenu() {
        menuBar = new JMenuBar();

        // Menú Alumnos
        menuAlumno = new JMenu("Alumnos");
        menuItemAgregarAlumno = new JMenuItem("Añadir alumno");
        menuItemEliminarAlumno = new JMenuItem("Eliminar alumno");
        menuItemAgregarAlumno.addActionListener(e -> abrirVentanaAgregarAlumno());
        menuItemEliminarAlumno.addActionListener(e -> eliminarAlumnoSeleccionado());
        menuAlumno.add(menuItemAgregarAlumno);
        menuAlumno.add(menuItemEliminarAlumno);

        // Menú Asignatura
        menuAsignatura = new JMenu("Asignatura");
        menuItemAgregarAsignatura = new JMenuItem("Añadir asignatura");
        menuItemEliminarAsignatura = new JMenuItem("Eliminar asignatura");
        menuItemAgregarAsignatura.addActionListener(e -> abrirVentanaAgregarAsignatura());
        menuItemEliminarAsignatura.addActionListener(e -> eliminarAsignaturaSeleccionada());
        menuAsignatura.add(menuItemAgregarAsignatura);
        menuAsignatura.add(menuItemEliminarAsignatura);

        // Menú Matricula
        menuMatricula = new JMenu("Matricula");
        menuItemAgregarMatricula = new JMenuItem("Nueva matrícula");
        menuItemEliminarMatricula = new JMenuItem("Eliminar matrícula");
        menuItemAgregarMatricula.addActionListener(e -> abrirVentanaAgregarMatricula());
        menuItemEliminarMatricula.addActionListener(e -> eliminarMatriculaSeleccionada());
        menuMatricula.add(menuItemAgregarMatricula);
        menuMatricula.add(menuItemEliminarMatricula);

        // Menú Vista
        menuVista = new JMenu("Vista");
        menuItemVista = new JCheckBoxMenuItem("Vista alumno");
        menuVista.add(menuItemVista);

        menuBar.add(menuAlumno);
        menuBar.add(menuAsignatura);
        menuBar.add(menuMatricula);
        menuBar.add(menuVista);
    }

    private JPanel crearPanelAlumnos() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnas = { "ID", "Nombre", "Dirección", "Estado Matrícula", "Carnet" };
        tablaAlumnos = new JTable(new DefaultTableModel(columnas, 0));
        TablaAlumnosModelo.aplicarEstiloCabeceras(tablaAlumnos);
        JScrollPane scrollPane = new JScrollPane(tablaAlumnos);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelAsignaturas() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnas = { "ID", "Nombre", "Curso" };
        tablaAsignaturas = new JTable(new DefaultTableModel(columnas, 0));
        TablaAlumnosModelo.aplicarEstiloCabeceras(tablaAsignaturas);
        JScrollPane scrollPane = new JScrollPane(tablaAsignaturas);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelMatriculas() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnas = { "ID", "Alumno", "Asignatura", "Nota" };
        tablaMatriculas = new JTable(new DefaultTableModel(columnas, 0));
        TablaAlumnosModelo.aplicarEstiloCabeceras(tablaAsignaturas);
        JScrollPane scrollPane = new JScrollPane(tablaMatriculas);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    // Métodos para cargar datos
    public void cargarDatosAlumnos() {
        DefaultTableModel modelo = (DefaultTableModel) tablaAlumnos.getModel();
        modelo.setRowCount(0);
        List<Alumno> alumnos = controlador.obtenerAlumnos();
        for (Alumno a : alumnos) {
            modelo.addRow(new Object[] { a.getId(), a.getNombre(), a.getDireccion(), a.getEstadoMatricula(),
                    a.isCarnetConducir() ? "Sí" : "No" });
        }
    }

    public void cargarDatosAsignaturas() {
        DefaultTableModel modelo = (DefaultTableModel) tablaAsignaturas.getModel();
        modelo.setRowCount(0);
        List<Asignatura> asignaturas = controlador.obtenerAsignaturas();
        for (Asignatura a : asignaturas) {
            modelo.addRow(new Object[] { a.getId(), a.getNombre(), a.getCurso() });
        }
    }

    public void cargarDatosMatriculas() {
        DefaultTableModel modelo = (DefaultTableModel) tablaMatriculas.getModel();
        modelo.setRowCount(0);
        List<Alumno> alumnos = controlador.obtenerAlumnos();
        for (Alumno alumno : alumnos) {
            List<Matricula> matriculas = controlador.obtenerMatriculasPorAlumno(alumno.getId());
            for (Matricula m : matriculas) {
                modelo.addRow(new Object[] { m.getId(), m.getAlumno().getNombre(), m.getAsignatura().getNombre(),
                        m.getNota() });
            }
        }
    }

    // Métodos para abrir ventanas
    private void abrirVentanaAgregarAlumno() {
        new VentanaAgregarAlumno(this, controlador).setVisible(true);
    }

    private void abrirVentanaAgregarAsignatura() {
        new VentanaAgregarAsignatura(this, controlador).setVisible(true);
    }

    private void abrirVentanaAgregarMatricula() {
        new VentanaAgregarMatricula(this, controlador).setVisible(true);
    }

    // Métodos para eliminar
    private void eliminarAlumnoSeleccionado() {
        int fila = tablaAlumnos.getSelectedRow();
        if (fila != -1) {
            int id = (int) tablaAlumnos.getValueAt(fila, 0);
            if (controlador.eliminarAlumno(id)) {
                cargarDatosAlumnos();
                cargarDatosMatriculas();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar alumno");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un alumno para eliminar");
        }
    }

    private void eliminarAsignaturaSeleccionada() {
        int fila = tablaAsignaturas.getSelectedRow();
        if (fila != -1) {
            int id = (int) tablaAsignaturas.getValueAt(fila, 0);
            if (controlador.eliminarAsignatura(id)) {
                cargarDatosAsignaturas();
                cargarDatosMatriculas();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar asignatura");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una asignatura para eliminar");
        }
    }

    private void eliminarMatriculaSeleccionada() {
        int fila = tablaMatriculas.getSelectedRow();
        String alumnoNombre = (String) tablaMatriculas.getValueAt(fila, 1);
        String asignaturaNombre = (String) tablaMatriculas.getValueAt(fila, 2);
        Alumno alumno = null;
        Asignatura asignatura = null;

        // Buscar alumno por nombre
        List<Alumno> alumnos = controlador.obtenerAlumnos();
        for (Alumno a : alumnos) {
            if (a.getNombre().equals(alumnoNombre)) {
                alumno = a;
            }
        }

        List<Asignatura> asignaturas = controlador.obtenerAsignaturas();
        for (Asignatura a : asignaturas) {
            if (a.getNombre().equals(asignaturaNombre)) {
                asignatura = a;
            }
        }

        if (fila != -1) {
            if (controlador.eliminarMatricula(alumno.getId(), asignatura.getId())) {
                cargarDatosMatriculas();
                JOptionPane.showMessageDialog(this, "Matricula eliminada");
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar asignatura");
                JOptionPane.showMessageDialog(this,
                        "Eliminación de matrícula desde aquí requiere refactorización para obtener IDs.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una matrícula para eliminar");
        }
    }

    public void refrescarTablaAlumnos(Object[][] datos) {
        cargarDatosAlumnos();
    }

    public void crearBarraHerramientas() {
        JToolBar barraHerramientas = new JToolBar();


        JButton btnCargarPDF = new JButton("Cargar PDF");
        btnCargarPDF.setToolTipText("Cargar PDF existente");
        btnCargarPDF.addActionListener(e -> gestorPDF.cargarPDFConDialogo());

        barraHerramientas.addSeparator();
        barraHerramientas.addSeparator();
        barraHerramientas.add(btnCargarPDF);
        barraHerramientas.setFloatable(false);

        panelPrincipal.add(barraHerramientas, BorderLayout.NORTH);
    }
    }