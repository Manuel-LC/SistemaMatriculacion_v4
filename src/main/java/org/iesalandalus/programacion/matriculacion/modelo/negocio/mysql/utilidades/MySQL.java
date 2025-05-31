package org.iesalandalus.programacion.matriculacion.modelo.negocio.mysql.utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private static final String HOST = "dbsistemamatriculacion.cjg9c61u4wra.us-east-1.rds.amazonaws.com";
    private static final String ESQUEMA = "sistemamatriculacion";
    private static final String USUARIO = "sistemamatriculacion";
    private static final String CONTRASENA = "sistemamatriculacion-2025";

    private static Connection conexion;

    private MySQL() {}

    public static Connection establecerConexion() {
        try {
            String conexionUrl = "jdbc:mysql://" + HOST + "/" + ESQUEMA + "?useSSL=false&serverTimezone=UTC";
            conexion = DriverManager.getConnection(conexionUrl, USUARIO, CONTRASENA);

        } catch (SQLException e) {
            System.err.println("ERROR: No se puede conectar con la base de datos: " + e.getMessage());
        }

        return conexion;
    }

    public static void cerrarConexion() {
        try {
            conexion.close();

        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
