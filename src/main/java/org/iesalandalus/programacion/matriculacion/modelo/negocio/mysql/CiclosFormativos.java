package org.iesalandalus.programacion.matriculacion.modelo.negocio.mysql;

import org.iesalandalus.programacion.matriculacion.modelo.dominio.*;
import org.iesalandalus.programacion.matriculacion.modelo.negocio.ICiclosFormativos;
import org.iesalandalus.programacion.matriculacion.modelo.negocio.mysql.utilidades.MySQL;

import javax.naming.OperationNotSupportedException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CiclosFormativos implements ICiclosFormativos {

    private static List<CicloFormativo> coleccionCiclosFormativos;
    private Connection conexion;
    private static CiclosFormativos instancia;

    public CiclosFormativos() {
        coleccionCiclosFormativos = new ArrayList<>();

        comenzar();
    }

    static CiclosFormativos getInstancia() {
        if (instancia == null) {
            instancia = new CiclosFormativos();
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

    public static Grado getGrado(String tipoGrado, String nombreGrado, int numAniosGrado, String modalidad, int numEdiciones) {
        Grado grado = null;
        if (tipoGrado.equalsIgnoreCase("GradoD")) {
            if (modalidad.equalsIgnoreCase("Presencial")) {
                grado = new GradoD(nombreGrado, numAniosGrado, Modalidad.PRESENCIAL);
            } else if (modalidad.equalsIgnoreCase("Semipresencial")){
                grado = new GradoD(nombreGrado, numAniosGrado, Modalidad.SEMIPRESENCIAL);
            }
        } else if (tipoGrado.equalsIgnoreCase("GradoE")) {
            grado = new GradoE(nombreGrado, numAniosGrado, numEdiciones);
        } else {
            throw new IllegalArgumentException("ERROR: El tipo de grado no es válido.");
        }

        return grado;
    }

    @Override
    public List<CicloFormativo> get() {
        List<CicloFormativo> ciclosFormativos = new ArrayList<>();
        String consulta = "SELECT * FROM cicloFormativo ORDER BY nombre";

        try (Statement sentencia = conexion.createStatement();
             ResultSet resultado = sentencia.executeQuery(consulta)) {

            while (resultado.next()) {
                int codigo = resultado.getInt("codigo");
                String familiaProfesional = resultado.getString("familiaProfesional");
                String tipoGrado = resultado.getString("grado");
                String nombre = resultado.getString("nombre");
                int horas = resultado.getInt("horas");
                String nombreGrado = resultado.getString("nombreGrado");
                int numAniosGrado = resultado.getInt("numAniosGrado");
                String modalidad = resultado.getString("modalidad");
                int numEdiciones = resultado.getInt("numEdiciones");

                CicloFormativo cicloFormativo = new CicloFormativo (codigo, familiaProfesional,
                        getGrado(tipoGrado, nombreGrado, numAniosGrado, modalidad, numEdiciones), nombre, horas);
                ciclosFormativos.add(cicloFormativo);
            }

        } catch (SQLException e) {
            System.err.println("Error al mostrar ciclos formativos: " + e.getMessage());
        }

        return ciclosFormativos;
    }

    @Override
    public int getTamano() {
        String consulta = "SELECT COUNT(*) FROM cicloFormativo";

        try (Statement sentencia = conexion.createStatement();
             ResultSet resultado = sentencia.executeQuery(consulta)) {
            if (resultado.next()) {
                return resultado.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Error al contar ciclos formativos: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public void insertar(CicloFormativo cicloFormativo) throws OperationNotSupportedException {
        if (cicloFormativo == null) {
            throw new NullPointerException("ERROR: No se puede insertar un ciclo formativo nulo.");
        }

        String insercion = "INSERT INTO cicloFormativo (codigo, familiaProfesional, grado, nombre, horas, nombreGrado, numAniosGrado, modalidad, numEdiciones) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement sentencia = conexion.prepareStatement(insercion)) {
            sentencia.setInt(1, cicloFormativo.getCodigo());
            sentencia.setString(2, cicloFormativo.getFamiliaProfesional());
            sentencia.setString(3, cicloFormativo.getGrado() instanceof GradoD ? "gradod" : "gradoe");
            sentencia.setString(4, cicloFormativo.getNombre());
            sentencia.setInt(5, cicloFormativo.getHoras());
            sentencia.setString(6, cicloFormativo.getGrado().getNombre());
            sentencia.setInt(7, cicloFormativo.getGrado().getNumAnios());

            sentencia.setString(8, cicloFormativo.getGrado() instanceof GradoD ?
                    ((GradoD) cicloFormativo.getGrado()).getModalidad().name().toLowerCase() : null);

            sentencia.setObject(9, cicloFormativo.getGrado() instanceof GradoE ?
                    ((GradoE) cicloFormativo.getGrado()).getNumEdiciones() : null);

            sentencia.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new OperationNotSupportedException("ERROR: Ya existe un ciclo formativo con ese código.");
        } catch (SQLException e) {
            System.err.println("Error al insertar ciclo formativo: " + e.getMessage());
        }
    }

    @Override
    public CicloFormativo buscar(CicloFormativo cicloFormativo) {
        if (cicloFormativo == null) {
            throw new NullPointerException("ERROR: No se puede buscar un ciclo formativo nulo.");
        }

        String consulta = "SELECT * FROM cicloFormativo WHERE codigo = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, cicloFormativo.getCodigo());

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    String familiaProfesional = resultado.getString("familiaProfesional");
                    String tipoGrado = resultado.getString("grado");
                    String nombre = resultado.getString("nombre");
                    int horas = resultado.getInt("horas");
                    String nombreGrado = resultado.getString("nombreGrado");
                    int numAniosGrado = resultado.getInt("numAniosGrado");

                    Grado grado;
                    if ("gradod".equalsIgnoreCase(tipoGrado)) {
                        Modalidad modalidad = Modalidad.valueOf(resultado.getString("modalidad").toUpperCase());
                        grado = new GradoD(nombreGrado, numAniosGrado, modalidad);
                    } else {
                        int numEdiciones = resultado.getInt("numEdiciones");
                        grado = new GradoE(nombreGrado, numAniosGrado, numEdiciones);
                    }

                    return new CicloFormativo(cicloFormativo.getCodigo(), familiaProfesional, grado, nombre, horas);
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar ciclo formativo: " + e.getMessage());
        }
    }

    @Override
    public void borrar(CicloFormativo cicloFormativo) throws OperationNotSupportedException {
        if (cicloFormativo == null) {
            throw new NullPointerException("ERROR: No se puede borrar un ciclo formativo nulo.");
        }

        String eliminacion = "DELETE FROM cicloFormativo WHERE codigo = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(eliminacion)) {
            sentencia.setInt(1, cicloFormativo.getCodigo());

            sentencia.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error al borrar ciclo formativo: " + e.getMessage());
        }
    }
}
