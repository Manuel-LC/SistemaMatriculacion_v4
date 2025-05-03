package org.iesalandalus.programacion.matriculacion.modelo.negocio.mysql;

import org.iesalandalus.programacion.matriculacion.modelo.dominio.Asignatura;
import org.iesalandalus.programacion.matriculacion.modelo.dominio.CicloFormativo;
import org.iesalandalus.programacion.matriculacion.modelo.dominio.Curso;
import org.iesalandalus.programacion.matriculacion.modelo.dominio.EspecialidadProfesorado;
import org.iesalandalus.programacion.matriculacion.modelo.negocio.IAsignaturas;
import org.iesalandalus.programacion.matriculacion.modelo.negocio.mysql.utilidades.MySQL;

import javax.naming.OperationNotSupportedException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Asignaturas implements IAsignaturas {

    private static List<Asignatura> coleccionAsignaturas;
    private Connection conexion;
    private static Asignaturas instancia;

    public Asignaturas() {
        coleccionAsignaturas = new ArrayList<>();

        comenzar();
    }

    static Asignaturas getInstancia() {
        if (instancia == null) {
            instancia = new Asignaturas();
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

    private Curso getCurso(String curso) {
        if (curso.equalsIgnoreCase("primero")) {
            return Curso.PRIMERO;
        } else if (curso.equalsIgnoreCase("segundo")) {
            return Curso.SEGUNDO;
        } else {
            throw new IllegalArgumentException("ERROR: Curso no válido.");
        }
    }

    private EspecialidadProfesorado getEspecialidadProfesorado(String especialidad) {
        return switch (especialidad.toLowerCase()) {
            case "informatica" -> EspecialidadProfesorado.INFORMATICA;
            case "sistemas" -> EspecialidadProfesorado.SISTEMAS;
            case "fol" -> EspecialidadProfesorado.FOL;
            default -> throw new IllegalArgumentException("ERROR: Especialidad no válida.");
        };
    }

    @Override
    public List<Asignatura> get() {
        List<Asignatura> asignaturas = new ArrayList<>();
        String consulta = "SELECT * FROM asignatura a JOIN cicloFormativo cf ON a.codigoCicloFormativo = cf.codigo ORDER BY nombre";

        try (Statement sentencia = conexion.createStatement();
             ResultSet resultado = sentencia.executeQuery(consulta)) {

            while (resultado.next()) {
                String codigo = resultado.getString("codigo");
                String nombre = resultado.getString("nombre");
                int horasAnuales = resultado.getInt("horasAnuales");
                Curso curso = getCurso(resultado.getString("curso"));
                int horasDesdoble = resultado.getInt("horasDesdoble");
                EspecialidadProfesorado especialidad = getEspecialidadProfesorado(resultado.getString("especialidadProfesorado"));
                int codigoCicloFormativo = resultado.getInt("codigoCicloFormativo");

                Asignatura asignatura = new Asignatura(codigo, nombre, horasAnuales, curso, horasDesdoble, especialidad, Asignatura.getCicloFormativo());
                asignaturas.add(asignatura);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al mostrar asignaturas: " + e.getMessage());
        }

        return asignaturas;
    }

    @Override
    public int getTamano() {
        String consulta = "SELECT COUNT(*) FROM asignatura";

        try (Statement sentencia = conexion.createStatement();
             ResultSet resultado = sentencia.executeQuery(consulta)) {
            if (resultado.next()) {
                return resultado.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al contar asignaturas: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public void insertar(Asignatura asignatura) throws OperationNotSupportedException {
        if (asignatura == null) {
            throw new NullPointerException("ERROR: No se puede insertar una asignatura nula.");
        }

        String consulta = "INSERT INTO asignatura (codigo, nombre, horasAnuales, curso, horasDesdoble, especialidadProfesorado, codigoCicloFormativo) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setString(1, asignatura.getCodigo());
            sentencia.setString(2, asignatura.getNombre());
            sentencia.setInt(3, asignatura.getHorasAnuales());
            sentencia.setString(4, asignatura.getCurso().name().toLowerCase());
            sentencia.setInt(5, asignatura.getHorasDesdoble());
            sentencia.setString(6, asignatura.getEspecialidadProfesorado().name().toLowerCase());
            sentencia.setInt(7, asignatura.getCicloFormativo().getCodigo());

            sentencia.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new OperationNotSupportedException("ERROR: Ya existe un asignatura con ese código.");
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar la asignatura: " + e.getMessage());
        }
    }

    public Asignatura buscar(Asignatura asignatura) {
        if (asignatura == null) {
            throw new NullPointerException("ERROR: No se puede buscar una asignatura nula.");
        }

        String consulta = "SELECT * FROM asignatura WHERE codigo = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setString(1, asignatura.getCodigo());

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    String nombre = resultado.getString("nombre");
                    int horasAnuales = resultado.getInt("horasAnuales");
                    Curso curso = getCurso(resultado.getString("curso"));
                    int horasDesdoble = resultado.getInt("horasDesdoble");
                    EspecialidadProfesorado especialidad = getEspecialidadProfesorado(resultado.getString("especialidadProfesorado"));
                    int codigoCicloFormativo = resultado.getInt("codigoCicloFormativo");

                    return new Asignatura(asignatura.getCodigo(), nombre, horasAnuales, curso, horasDesdoble, especialidad, asignatura.getCicloFormativo());
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar la asignatura: " + e.getMessage());
        }
    }

    @Override
    public void borrar(Asignatura asignatura) throws OperationNotSupportedException {
        if (asignatura == null) {
            throw new NullPointerException("ERROR: No se puede borrar una asignatura nula.");
        }

        String consulta = "DELETE FROM asignatura WHERE codigo = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setString(1, asignatura.getCodigo());

            sentencia.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al borrar la asignatura: " + e.getMessage());
        }
    }
}
