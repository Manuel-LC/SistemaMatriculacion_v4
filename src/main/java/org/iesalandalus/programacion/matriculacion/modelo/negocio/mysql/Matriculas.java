package org.iesalandalus.programacion.matriculacion.modelo.negocio.mysql;

import org.iesalandalus.programacion.matriculacion.modelo.dominio.*;
import org.iesalandalus.programacion.matriculacion.modelo.negocio.IMatriculas;
import org.iesalandalus.programacion.matriculacion.modelo.negocio.mysql.utilidades.MySQL;

import javax.naming.OperationNotSupportedException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Matriculas implements IMatriculas {

    private static List<Matricula> coleccionMatriculas;
    private Connection conexion;
    private static Matriculas instancia;

    public Matriculas() {
        coleccionMatriculas = new ArrayList<>();

        comenzar();
    }

    static Matriculas getInstancia() {
        if (instancia == null) {
            instancia = new Matriculas();
        }

        return instancia;
    }

    @Override
    public void comenzar() {
        conexion = MySQL.establecerConexion();
    }

    @Override
    public void terminar() {
        MySQL.cerrarConexion();
    }

    private List<Asignatura> getAsignaturasMatricula(int idMatricula) {
        List<Asignatura> asignaturasMatricula = new ArrayList<>();
        String consulta = "SELECT * FROM asignatura a JOIN asignaturasMatricula am ON a.codigo = am.codigo WHERE am.idMatricula = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idMatricula);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                String codigo = resultado.getString("codigo");
                String nombre = resultado.getString("nombre");
                int horasAnuales = resultado.getInt("horasAnuales");
                Curso curso = Curso.valueOf(resultado.getString("curso"));
                int horasDesdoble = resultado.getInt("horasDesdoble");
                EspecialidadProfesorado especialidadProf = EspecialidadProfesorado.valueOf(resultado.getString("especialidadProfesorado"));
                int codigoCicloFormativo = resultado.getInt("codigoCicloFormativo");

                asignaturasMatricula.add(new Asignatura(codigo, nombre, horasAnuales, curso, horasDesdoble, especialidadProf, Asignatura.getCicloFormativo()));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener las asignaturas de la matrícula: " + e.getMessage());
        }
        return asignaturasMatricula;
    }

    @Override
    public List<Matricula> get() throws OperationNotSupportedException {
        List<Matricula> matriculas = new ArrayList<>();
        String consulta = "SELECT m.* FROM matricula m JOIN alumno a ON m.dni = a.dni ORDER BY m.fechaMatriculacion DESC, a.nombre ASC";

        try (Statement sentencia = conexion.createStatement();
             ResultSet resultado = sentencia.executeQuery(consulta)) {

            while (resultado.next()) {
                int idMatricula = resultado.getInt("idMatricula");
                String cursoAcademico = resultado.getString("cursoAcademico");
                LocalDate fechaMatriculacion = resultado.getDate("fechaMatriculacion").toLocalDate();

                Date fechaAnulacionSQL = resultado.getDate("fechaAnulacion");
                LocalDate fechaAnulacion = (fechaAnulacionSQL != null) ? fechaAnulacionSQL.toLocalDate() : null;

                String dni = resultado.getString("dni");
                String nombre = resultado.getString("nombre");
                String correo = resultado.getString("correo");
                String telefono = resultado.getString("telefono");
                LocalDate fechaNacimiento = resultado.getDate("fechaNacimiento").toLocalDate();
                Alumno alumno = new Alumno(nombre, dni, correo, telefono, fechaNacimiento);

                List<Asignatura> asignaturasMatricula = getAsignaturasMatricula(idMatricula);

                Matricula matricula = new Matricula(idMatricula,cursoAcademico,fechaMatriculacion,alumno,asignaturasMatricula);
                matriculas.add(matricula);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al mostrar matrículas: " + e.getMessage());
        }
        return matriculas;
    }

    @Override
    public int getTamano() {
        String consulta = "SELECT COUNT(*) FROM matricula";

        try (Statement sentencia = conexion.createStatement(); ResultSet resultado = sentencia.executeQuery(consulta)) {
            if (resultado.next()) {
                return resultado.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al contar matrículas: " + e.getMessage());
        }
        return 0;
    }

    private void insertarAsignaturasMatricula(int idMatricula, List<Asignatura> coleccionAsignaturas) {
        String insercion = "INSERT INTO asignaturasMatricula (idMatricula, codigo) VALUES (?, ?)";

        try (PreparedStatement sentencia = conexion.prepareStatement(insercion)) {
            for (Asignatura asignatura : coleccionAsignaturas) {
                sentencia.setInt(1, idMatricula);
                sentencia.setString(2, asignatura.getCodigo());
                sentencia.addBatch();
            }
            sentencia.executeBatch();

        } catch (SQLException e) {
            System.err.println("Error al insertar las asignaturas de la matrícula: " + e.getMessage());
        }
    }

    @Override
    public void insertar(Matricula matricula) throws OperationNotSupportedException {
        if (matricula == null) {
            throw new NullPointerException("ERROR: No se puede insertar una matrícula nula.");
        }

        String sql = "INSERT INTO matricula (idMatricula, cursoAcademico, fechaMatriculacion, fechaAnulacion, dni) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, matricula.getIdMatricula());
            ps.setString(2, matricula.getCursoAcademico());
            ps.setDate(3, Date.valueOf(matricula.getFechaMatriculacion()));
            ps.setDate(4, matricula.getFechaAnulacion() != null ? Date.valueOf(matricula.getFechaAnulacion()) : null);
            ps.setString(5, matricula.getAlumno().getDni());
            ps.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new OperationNotSupportedException("ERROR: Ya existe una matrícula con ese identificador.");
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar la matrícula: " + e.getMessage());
        }
    }

    @Override
    public Matricula buscar(Matricula matricula) throws OperationNotSupportedException {
        if (matricula == null) {
            throw new NullPointerException("ERROR: No se puede buscar una matrícula nula.");
        }

        String consulta = "SELECT * FROM matricula WHERE idMatricula = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, matricula.getIdMatricula());
            ResultSet resultado = sentencia.executeQuery();

            if (resultado.next()) {
                String dni = resultado.getString("dni");
                String nombre = resultado.getString("nombre");
                String correo = resultado.getString("correo");
                String telefono = resultado.getString("telefono");
                LocalDate fechaNacimiento = resultado.getDate("fechaNacimiento").toLocalDate();
                Alumno alumno = new Alumno(nombre, dni, correo, telefono, fechaNacimiento);

                LocalDate fechaAnulacion = null;
                Date fechaSQL = resultado.getDate("fechaAnulacion");
                if (fechaSQL != null) {
                    fechaAnulacion = fechaSQL.toLocalDate();
                }

                return new Matricula(
                        resultado.getInt("idMatricula"),
                        resultado.getString("cursoAcademico"),
                        resultado.getDate("fechaMatriculacion").toLocalDate(),
                        alumno,
                        getAsignaturasMatricula(resultado.getInt("idMatricula"))
                );
            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar la matrícula: " + e.getMessage());
        }
    }

    @Override
    public void borrar(Matricula matricula) throws OperationNotSupportedException {
        if (matricula == null) {
            throw new NullPointerException("ERROR: No se puede borrar una matrícula nula.");
        }

        String consulta = "DELETE FROM matricula WHERE idMatricula = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, matricula.getIdMatricula());
            sentencia.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("No existe ninguna matrícula como la indicadaError al borrar la matrícula: " + e.getMessage());
        }
    }

    @Override
    public List<Matricula> get(Alumno alumno) throws OperationNotSupportedException {
        List<Matricula> coleccionMatriculasAlumno = new ArrayList<>();
        String consulta = "SELECT * FROM matricula WHERE dni = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setString(1, alumno.getDni());
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                coleccionMatriculasAlumno.add(new Matricula(
                        resultado.getInt("idMatricula"),
                        resultado.getString("cursoAcademico"),
                        resultado.getDate("fechaMatriculacion").toLocalDate(),
                        alumno,
                        getAsignaturasMatricula(resultado.getInt("idMatricula"))
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al mostrar las matrículas del alumno: " + e.getMessage());
        }
        return coleccionMatriculasAlumno;
    }

    @Override
    public List<Matricula> get(String cursoAcademico) throws OperationNotSupportedException {
        List<Matricula> coleccionMatriculasCurso = new ArrayList<>();

        String sql = "SELECT * FROM matricula WHERE cursoAcademico = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, cursoAcademico);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                String dni = resultado.getString("dni");
                String nombre = resultado.getString("nombre");
                String correo = resultado.getString("correo");
                String telefono = resultado.getString("telefono");
                LocalDate fechaNacimiento = resultado.getDate("fechaNacimiento").toLocalDate();
                Alumno alumno = new Alumno(nombre, dni, correo, telefono, fechaNacimiento);

                coleccionMatriculasCurso.add(new Matricula(
                        resultado.getInt("idMatricula"),
                        resultado.getString("cursoAcademico"),
                        resultado.getDate("fechaMatriculacion").toLocalDate(),
                        alumno,
                        getAsignaturasMatricula(resultado.getInt("idMatricula"))
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al mostrar las matrículas del curso académico: " + e.getMessage());
        }
        return coleccionMatriculasCurso;
    }

    @Override
    public List<Matricula> get(CicloFormativo cicloFormativo) throws OperationNotSupportedException {
        List<Matricula> coleccionMatriculasCiclo = new ArrayList<>();

        String sql = "SELECT m.* FROM matricula m JOIN alumno a ON m.dni = a.dni WHERE a.cicloFormativo = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, cicloFormativo.getNombre());
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                String dni = resultado.getString("dni");
                String nombre = resultado.getString("nombre");
                String correo = resultado.getString("correo");
                String telefono = resultado.getString("telefono");
                LocalDate fechaNacimiento = resultado.getDate("fechaNacimiento").toLocalDate();
                Alumno alumno = new Alumno(nombre, dni, correo, telefono, fechaNacimiento);

                coleccionMatriculasCiclo.add(new Matricula(
                        resultado.getInt("idMatricula"),
                        resultado.getString("cursoAcademico"),
                        resultado.getDate("fechaMatriculacion").toLocalDate(),
                        alumno,
                        getAsignaturasMatricula(resultado.getInt("idMatricula"))
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al mostrar las matrículas del ciclo formativo: " + e.getMessage());
        }
        return coleccionMatriculasCiclo;
    }
}
