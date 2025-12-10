package Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Ventana extends JFrame {
    protected JTable tablaAlumnos;
    protected DefaultTableModel modeloTablaAlumnos;
    protected JMenuBar barraMenu;
    protected JMenu menuAlumnos, menuAsignaturas, menuMatriculas, menuUsuario;
    protected JMenuItem itemAgregarAlumno, itemActualizarAlumno, itemEliminarAlumno;
    protected JMenuItem itemAgregarAsignatura, itemAgregarMatricula;
    protected JMenuItem itemCerrarSesion;

    public Ventana(String titulo) {
        super(titulo);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear la tabla de alumnos
        modeloTablaAlumnos = new DefaultTableModel();
        modeloTablaAlumnos.addColumn("ID");
        modeloTablaAlumnos.addColumn("Nombre");
        modeloTablaAlumnos.addColumn("Carnet de Conducir");

        tablaAlumnos = new JTable(modeloTablaAlumnos);
        JScrollPane scrollPane = new JScrollPane(tablaAlumnos);
        add(scrollPane, BorderLayout.CENTER);

        // Menú
        barraMenu = new JMenuBar();
        setJMenuBar(barraMenu);

        menuAlumnos = new JMenu("Alumnos");
        menuAsignaturas = new JMenu("Asignaturas");
        menuMatriculas = new JMenu("Matrículas");
        menuUsuario = new JMenu("Usuario");

        barraMenu.add(menuAlumnos);
        barraMenu.add(menuAsignaturas);
        barraMenu.add(menuMatriculas);
        barraMenu.add(menuUsuario);

        itemAgregarAlumno = new JMenuItem("Agregar Alumno");
        itemActualizarAlumno = new JMenuItem("Actualizar Alumno");
        itemEliminarAlumno = new JMenuItem("Eliminar Alumno");

        menuAlumnos.add(itemAgregarAlumno);
        menuAlumnos.add(itemActualizarAlumno);
        menuAlumnos.add(itemEliminarAlumno);

        itemAgregarAsignatura = new JMenuItem("Agregar Asignatura");
        itemAgregarMatricula = new JMenuItem("Agregar Matrícula");

        menuAsignaturas.add(itemAgregarAsignatura);
        menuMatriculas.add(itemAgregarMatricula);

        itemCerrarSesion = new JMenuItem("Cerrar Sesión");
        menuUsuario.add(itemCerrarSesion);

        // Acciones de los menús
        itemAgregarAlumno.addActionListener(e -> abrirVentanaAgregarAlumno());
        itemAgregarAsignatura.addActionListener(e -> abrirVentanaAgregarAsignatura());
        itemAgregarMatricula.addActionListener(e -> abrirVentanaAgregarMatricula());
        itemCerrarSesion.addActionListener(e -> cerrarSesion());

        // Evento de clic derecho para menú contextual
        tablaAlumnos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int fila = tablaAlumnos.rowAtPoint(e.getPoint());
                    if (fila >= 0) {
                        tablaAlumnos.setRowSelectionInterval(fila, fila);
                        JPopupMenu menu = new JPopupMenu();
                        JMenuItem actualizar = new JMenuItem("Actualizar");
                        JMenuItem eliminar = new JMenuItem("Eliminar");

                        actualizar.addActionListener(e1 -> actualizarAlumno());
                        eliminar.addActionListener(e1 -> eliminarAlumno());

                        menu.add(actualizar);
                        menu.add(eliminar);
                        menu.show(tablaAlumnos, e.getX(), e.getY());
                    }
                }
            }
        });
    }

    // Métodos auxiliares. Deben ser sobrescritos por clases hijas
    protected void abrirVentanaAgregarAlumno() {
        // Implementación por defecto
    }

    protected void abrirVentanaAgregarAsignatura() {
        // Implementación por defecto
    }

    protected void abrirVentanaAgregarMatricula() {
        // Implementación por defecto
    }

    protected void cerrarSesion() {
        this.dispose();
        new VentanaLogin().setVisible(true);
    }

    protected void actualizarAlumno() {
        // Implementación por defecto
    }

    protected void eliminarAlumno() {
        // Implementación por defecto
    }

    public void agregarFilaAlumno(Object[] datos) {
        modeloTablaAlumnos.addRow(datos);
    }

    public void actualizarFilaAlumno(int fila, Object[] datos) {
        for (int i = 0; i < datos.length; i++) {
            modeloTablaAlumnos.setValueAt(datos[i], fila, i);
        }
    }

    public void refrescarTablaAlumnos(Object[][] datos) {
        modeloTablaAlumnos.setRowCount(0);
        for (Object[] fila : datos) {
            modeloTablaAlumnos.addRow(fila);
        }
    }
}
