
package ControladorBBDD;

import Modelo.Alumno;
import Modelo.Asignatura;
import Modelo.Matricula;
import Modelo.ConexionDAOInstituto;

import java.util.Comparator;
import java.util.List;

public class Controlador {

    // ALUMNOS
    public List<Alumno> obtenerAlumnos() {
        try {
            return ConexionDAOInstituto.obtenerAlumnos();
        } catch (Exception e) {
            System.err.println("Error al obtener alumnos: " + e.getMessage());
            return List.of(); // Devolver lista vacía en lugar de null
        }
    }

    public boolean agregarAlumno(Alumno a) {
        if (a == null) {
            System.out.println("Error: Alumno nulo");
            return false;
        }
        if (a.getNombre() == null || a.getNombre().trim().isEmpty()) {
            System.out.println("Error: Nombre de alumno inválido");
            return false;
        }
        return ConexionDAOInstituto.insertarAlumno(a);
    }

    public boolean eliminarAlumno(int idAlumno) {
        return ConexionDAOInstituto.eliminarAlumno(idAlumno);
    }

    public void mostrarAlumnosPorNombre() {
        List<Alumno> alumnos = obtenerAlumnos();
        alumnos.sort(Comparator.comparing(Alumno::getNombre, String.CASE_INSENSITIVE_ORDER));
        alumnos.forEach(System.out::println);
    }

    public Alumno obtenerAlumnoPorId(int idAlumno) {
        Alumno alumno = ConexionDAOInstituto.obtenerAlumnoPorId(idAlumno);
        if (alumno == null) {
            System.out.println("Alumno con ID " + idAlumno + " no encontrado");
        }
        return alumno;
    }

    // ASIGNATURAS
    public List<Asignatura> obtenerAsignaturas() {
        return ConexionDAOInstituto.obtenerAsignaturas();
    }

    public boolean agregarAsignatura(Asignatura a) {
        return ConexionDAOInstituto.insertarAsignatura(a);
    }

    public boolean eliminarAsignatura(int idAsignatura) {
        return ConexionDAOInstituto.eliminarAsignatura(idAsignatura);
    }

    public int ultimoIdAsignaturas() {
        List<Asignatura> asignaturas = obtenerAsignaturas();
        if (asignaturas.isEmpty())
            return 0;
        return asignaturas.stream()
                .mapToInt(Asignatura::getId)
                .max()
                .orElse(0);
    }

    public Asignatura obtenerAsignaturaPorId(int idAsignatura) {
        return ConexionDAOInstituto.obtenerAsignatura(idAsignatura);
    }

    // Obtener promedio de notas por alumno
    public double obtenerPromedioNotasPorAlumno(int idAlumno) {
        List<Matricula> matriculas = obtenerMatriculasPorAlumno(idAlumno);
        if (matriculas.isEmpty())
            return 0.0;
        return matriculas.stream()
                .mapToDouble(Matricula::getNota)
                .average()
                .orElse(0.0);
    }

    // Obtener asignaturas de un alumno
    public List<Asignatura> obtenerAsignaturasPorAlumno(int idAlumno) {
        List<Matricula> matriculas = obtenerMatriculasPorAlumno(idAlumno);
        return matriculas.stream()
                .map(matricula -> obtenerAsignaturaPorId(matricula.getAsignatura().getId()))
                .filter(asignatura -> asignatura != null)
                .toList();
    }

    // MATRICULAS
    public List<Matricula> obtenerMatriculasPorAlumno(int idAlumno) {
        return ConexionDAOInstituto.obtenerMatriculasPorAlumno(idAlumno);
    }

    public List<Matricula> obtenerMatriculasPorAsignatura(int idAsignatura) {
        return ConexionDAOInstituto.obtenerMatriculasPorAsignatura(idAsignatura);
    }

    public boolean insertarMatricula(Matricula m) {
        return ConexionDAOInstituto.insertarMatricula(m);
    }

    public boolean eliminarMatricula(int idAlumno, int idAsignatura) {
        return ConexionDAOInstituto.eliminarMatricula(idAlumno, idAsignatura);
    }

}
