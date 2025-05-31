package org.iesalandalus.programacion.matriculacion.modelo.negocio.mysql;

import org.iesalandalus.programacion.matriculacion.modelo.dominio.Alumno;
import org.iesalandalus.programacion.matriculacion.modelo.negocio.IAlumnos;
import org.iesalandalus.programacion.matriculacion.modelo.negocio.mysql.utilidades.MySQL;

import javax.naming.OperationNotSupportedException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Alumnos implements IAlumnos {

    private static List<Alumno> coleccionAlumnos;
    private Connection conexion;
    private static Alumnos instancia;

    public Alumnos() {
        coleccionAlumnos = new ArrayList<>();

        comenzar();
    }

    static Alumnos getInstancia() {
        if (instancia == null) {
            instancia = new Alumnos();
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

    @Override
    public List<Alumno> get() {
        List<Alumno> alumnos = new ArrayList<>();
        String consulta = "SELECT * FROM alumno ORDER BY dni";

        try (Statement sentencia = conexion.createStatement();
             ResultSet resultado = sentencia.executeQuery(consulta)) {

            while (resultado.next()) {
                String nombre = resultado.getString("nombre");
                String dni = resultado.getString("dni");
                String correo = resultado.getString("correo");
                String telefono = resultado.getString("telefono");
                LocalDate fechaNacimiento = resultado.getDate("fechaNacimiento").toLocalDate();

                Alumno alumno = new Alumno(nombre, dni, correo, telefono, fechaNacimiento);
                alumnos.add(alumno);
            }

        } catch (SQLException e) {
            System.err.println("Error al mostrar alumnos: " + e.getMessage());
        }
        return alumnos;
    }

    @Override
    public int getTamano() {
        String consulta = "SELECT COUNT(*) FROM alumno";

        try (Statement sentencia = conexion.createStatement();
             ResultSet resultado = sentencia.executeQuery(consulta)) {
            if (resultado.next()) {
                return resultado.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Error al contar alumnos: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public void insertar(Alumno alumno) throws OperationNotSupportedException {
        if (alumno == null) {
            throw new NullPointerException("ERROR: No se puede insertar un alumno nulo.");
        }

        String insercion = "INSERT INTO alumno (nombre, telefono, correo, dni, fechaNacimiento) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement sentencia = conexion.prepareStatement(insercion)) {
            sentencia.setString(1, alumno.getNombre());
            sentencia.setString(2, alumno.getTelefono());
            sentencia.setString(3, alumno.getCorreo());
            sentencia.setString(4, alumno.getDni());
            sentencia.setDate(5, Date.valueOf(alumno.getFechaNacimiento()));

            sentencia.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new OperationNotSupportedException("ERROR: Ya existe un alumno con ese dni.");
        } catch (SQLException e) {
            System.err.println("Error al insertar alumno: " + e.getMessage());
        }
    }

    @Override
    public Alumno buscar(Alumno alumno) {
        if (alumno == null) {
            throw new NullPointerException("ERROR: No se puede buscar un alumno nulo.");
        }

        String consulta = "SELECT * FROM alumno WHERE dni = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setString(1, alumno.getDni());

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    String nombre = resultado.getString("nombre");
                    String correo = resultado.getString("correo");
                    String telefono = resultado.getString("telefono");
                    LocalDate fechaNacimiento = resultado.getDate("fechaNacimiento").toLocalDate();

                    return new Alumno(nombre, alumno.getDni(), correo, telefono, fechaNacimiento);
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar alumno: " + e.getMessage());
        }
    }

    @Override
    public void borrar(Alumno alumno) throws OperationNotSupportedException {
        if (alumno == null) {
            throw new NullPointerException("ERROR: No se puede borrar un alumno nulo.");
        }

        String eliminacion = "DELETE FROM alumno WHERE dni = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(eliminacion)) {
            sentencia.setString(1, alumno.getDni());

            sentencia.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al borrar alumno: " + e.getMessage());
        }
    }
}
