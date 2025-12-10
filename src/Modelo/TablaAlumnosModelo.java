package Modelo;

import ControladorBBDD.Controlador;
import Vista.RenderizadorCabecera;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TablaAlumnosModelo extends AbstractTableModel {

    private Controlador controlador = new Controlador();
    private List<Alumno> alumnos;

    public final static int NOMBRE = 0;
    public final static int DIRECCION = 1;
    public final static int ESTADOMATRICULA = 2;
    public final static int TIENE_CARNET = 3;

    public final static String[] nombresColumnas = {
            "Nombre del alumno",
            "Direccion",
            "Estado de la matricula",
            "Carnet de conducir"
    };

    public TablaAlumnosModelo() {
        this.alumnos = obtenerAlumnos();
    }

    private List<Alumno> obtenerAlumnos() {
        return controlador.obtenerAlumnos();
    }

    @Override
    public int getRowCount() {
        return alumnos != null ? alumnos.size() : 0;
    }

    @Override
    public int getColumnCount() {
        return nombresColumnas.length;
    }

    @Override
    public String getColumnName(int column) {
        return nombresColumnas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Alumno a = alumnos.get(rowIndex);

        switch (columnIndex) {
            case NOMBRE:
                return a.getNombre();
            case DIRECCION:
                return a.getDireccion();
            case ESTADOMATRICULA:
                return a.getEstadoMatricula();
            case TIENE_CARNET:
                return a.isCarnetConducir();
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case NOMBRE:
            case DIRECCION:
            case ESTADOMATRICULA:
                return String.class;
            case TIENE_CARNET:
                return Boolean.class;
        }
        return Object.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; // Cambiar si necesitas edición
    }

    public void setAlumnos(List<Alumno> alumnos) {
        this.alumnos = alumnos;
        fireTableDataChanged();
    }

    public Alumno getAlumnoAt(int rowIndex) {
        return alumnos.get(rowIndex);
    }

    public static void aplicarEstiloCabeceras(JTable tabla) {
        tabla.getTableHeader().setDefaultRenderer(new RenderizadorCabecera());
    }
}