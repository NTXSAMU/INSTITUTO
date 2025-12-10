package Modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConexionDAOInstituto {
    private static final String URL = "jdbc:mysql://localhost:3306/instituto";
    private static final String USER = "root";
    private static final String PASSWORD = "usuario";

    public static List<Alumno> obtenerAlumnos() {
        List<Alumno> alumnos = new ArrayList<>();
        String sql = "SELECT id, nombre, direccion, estado_matricula, carnet_conducir FROM alumno";
        try (Connection conn = conectarseBD();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Alumno a = new Alumno(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("estado_matricula"),
                        rs.getInt("carnet_conducir"));
                alumnos.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alumnos;
    }

    public static List<Asignatura> obtenerAsignaturas() {
        List<Asignatura> asignaturas = new ArrayList<>(); // Eliminar variable estática
        try (Connection conexionBD = conectarseBD();
             Statement informe = conexionBD.createStatement();
             ResultSet conjuntoResultados = informe.executeQuery("SELECT id, nombre, curso FROM asignatura")) {
            while (conjuntoResultados.next()) {
                int idAsignatura = conjuntoResultados.getInt("id");
                String nombre = conjuntoResultados.getString("nombre");
                int curso = conjuntoResultados.getInt("curso");
                Asignatura a = new Asignatura(idAsignatura, nombre, curso);
                asignaturas.add(a);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return asignaturas;
    }

    public static List<Matricula> obtenerMatriculas() {
        List<Matricula> matriculas = new ArrayList<>(); // Eliminar variable estática
        try (Connection conexionBD = conectarseBD();
             Statement informe = conexionBD.createStatement();
             ResultSet conjuntoResultados = informe.executeQuery("SELECT * FROM matricula")) {
            while (conjuntoResultados.next()) {
                int idMatricula = conjuntoResultados.getInt("id");
                Alumno alumno = obtenerAlumnoPorId(conjuntoResultados.getInt("id_alumno"));
                Asignatura asignatura = obtenerAsignatura(conjuntoResultados.getInt("id_asignatura"));
                double nota = conjuntoResultados.getDouble("nota");
                Matricula m = new Matricula(idMatricula, alumno, asignatura, nota);
                matriculas.add(m);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return matriculas;
    }

    public static Alumno obtenerAlumnoPorId(int idAlumno) {
        String sql = "SELECT id, nombre, direccion, estado_matricula, carnet_conducir FROM alumno WHERE id = ?";
        try (Connection conn = conectarseBD();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAlumno);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Alumno(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("estado_matricula"),
                        rs.getInt("carnet_conducir"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Asignatura obtenerAsignatura(int idAsignatura) {
        String sql = "SELECT id, nombre, curso FROM asignatura WHERE id = ?";
        try (Connection conn = conectarseBD();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAsignatura);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Asignatura(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getInt("curso"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean insertarAlumno(Alumno a) {
        if (a == null || a.getNombre() == null || a.getNombre().trim().isEmpty()) {
            return false;
        }
        String sql = "INSERT INTO alumno (nombre, direccion, estado_matricula, carnet_conducir) VALUES (?, ?, ?, ?)";
        try (Connection conn = conectarseBD();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, a.getNombre());
            stmt.setString(2, a.getDireccion());
            stmt.setString(3, a.getEstadoMatricula());
            stmt.setInt(4, a.isCarnetConducir() ? 1 : 0);

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    a.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar alumno: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static boolean eliminarAlumno(int idAlumno) {
        String sql = "DELETE FROM alumno WHERE id = ?";
        try (Connection conn = conectarseBD();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAlumno);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean insertarAsignatura(Asignatura a) {
        boolean resultado = false;
        String consulta = "INSERT INTO asignatura (nombre, curso) VALUES (?, ?)";
        try (Connection conn = conectarseBD();
             PreparedStatement stmt = conn.prepareStatement(consulta)) {
            stmt.setString(1, a.getNombre());
            stmt.setInt(2, a.getCurso());
            resultado = stmt.executeUpdate() >= 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultado;
    }

    public static boolean eliminarAsignatura(int idAsignatura) {
        String sql = "DELETE FROM asignatura WHERE id = ?";
        try (Connection conn = conectarseBD();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAsignatura);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Connection conectarseBD() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static List<Matricula> obtenerMatriculasPorAlumno(int idAlumno) {
        List<Matricula> lista = new ArrayList<>();
        String sql = """
                    SELECT m.id, m.nota,
                           a.id AS id_alumno, a.nombre AS nombre_alumno, a.direccion, a.estado_matricula, a.carnet_conducir,
                           asig.id AS id_asig, asig.nombre AS nombre_asig, asig.curso
                    FROM matricula m
                    JOIN alumno a ON m.id_alumno = a.id
                    JOIN asignatura asig ON m.id_asignatura = asig.id
                    WHERE a.id = ?
                """;

        try (Connection conn = conectarseBD();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAlumno);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Alumno alumno = new Alumno(
                        rs.getInt("id_alumno"),
                        rs.getString("nombre_alumno"),
                        rs.getString("direccion"),
                        rs.getString("estado_matricula"),
                        rs.getInt("carnet_conducir"));

                Asignatura asignatura = new Asignatura(
                        rs.getInt("id_asig"),
                        rs.getString("nombre_asig"),
                        rs.getInt("curso"));

                Matricula m = new Matricula(
                        rs.getInt("id"),
                        alumno,
                        asignatura,
                        rs.getDouble("nota"));

                lista.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static boolean insertarMatricula(Matricula m) {
        String sql = "INSERT INTO matricula (id_alumno, id_asignatura, nota) VALUES (?, ?, ?)";
        try (Connection conn = conectarseBD();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, m.getAlumno().getId());
            stmt.setInt(2, m.getAsignatura().getId());
            stmt.setDouble(3, m.getNota());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Matricula> obtenerMatriculasPorAsignatura(int idAsignatura) {
        List<Matricula> lista = new ArrayList<>();
        String sql = """
                    SELECT m.id, m.nota,
                           a.id AS id_alumno, a.nombre AS nombre_alumno, a.direccion, a.estado_matricula, a.carnet_conducir,
                           asig.id AS id_asig, asig.nombre AS nombre_asig, asig.curso
                    FROM matricula m
                    JOIN alumno a ON m.id_alumno = a.id
                    JOIN asignatura asig ON m.id_asignatura = asig.id
                    WHERE asig.id = ?
                """;

        try (Connection conn = conectarseBD();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAsignatura);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Alumno alumno = new Alumno(
                        rs.getInt("id_alumno"),
                        rs.getString("nombre_alumno"),
                        rs.getString("direccion"),
                        rs.getString("estado_matricula"),
                        rs.getInt("carnet_conducir"));

                Asignatura asignatura = new Asignatura(
                        rs.getInt("id_asig"),
                        rs.getString("nombre_asig"),
                        rs.getInt("curso"));

                Matricula m = new Matricula(
                        rs.getInt("id"),
                        alumno,
                        asignatura,
                        rs.getDouble("nota"));

                lista.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static boolean eliminarMatricula(int idAlumno, int idAsignatura) {
        String sql = "DELETE FROM matricula WHERE id_alumno = ? AND id_asignatura = ?";
        try (Connection conn = conectarseBD();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAlumno);
            stmt.setInt(2, idAsignatura);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}