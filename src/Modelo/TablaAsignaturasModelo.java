package Modelo;



import ControladorBBDD.Controlador;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class TablaAsignaturasModelo extends AbstractTableModel {
    private Controlador controlador = new Controlador();
    private List<Asignatura> asignaturas;

    public final static int ID = 0;
    public final static int NOMBRE = 1;
    public final static int CURSO =  2;

    public final static String[] nombresColumnas = {
            "Id", "Nombre", "Curso"
    };

    public TablaAsignaturasModelo() {
        this.asignaturas = controlador.obtenerAsignaturas();
    }

    private List<Asignatura> obtenerAsignaturas() {
        return controlador.obtenerAsignaturas();
    }

    @Override
    public int getRowCount() {
        return asignaturas != null ? asignaturas.size() : 0;
    }

    @Override
    public String getColumnName(int column) {
        return nombresColumnas[column];
    }

    @Override
    public int getColumnCount() {
        return nombresColumnas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Asignatura a = asignaturas.get(rowIndex);

        switch (columnIndex) {
            case ID:
                return a.getId();
            case NOMBRE:
                return a.getNombre();
            case CURSO:
                return a.getCurso();
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case NOMBRE:
                return String.class;
            case CURSO:
            case ID:
                return Integer.class;
        }
        return Object.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; // Cambiar si necesitas edición
    }
}
