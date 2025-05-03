package org.iesalandalus.programacion.matriculacion;

import org.iesalandalus.programacion.matriculacion.controlador.Controlador;
import org.iesalandalus.programacion.matriculacion.modelo.FactoriaFuenteDatos;
import org.iesalandalus.programacion.matriculacion.modelo.Modelo;
import org.iesalandalus.programacion.matriculacion.vista.Vista;

public class MainApp {

    public static void main(String[] args) {
        System.out.println("Iniciando la aplicación...");

        Modelo modelo = procesarArgumentosFuenteDatos(args);
        Vista vista = new Vista();
        Controlador controlador = new Controlador(modelo, vista);
        controlador.comenzar();

        System.out.println("Aplicación cerrada.");
    }

    private static Modelo procesarArgumentosFuenteDatos(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("ERROR: Debe especificarse una fuente de datos (-fdmemoria o -fdmysql).");
        }

        switch (args[0].toLowerCase()) {
            case "-fdmemoria":
                return new Modelo(FactoriaFuenteDatos.MEMORIA);
            case "-fdmysql":
                return new Modelo(FactoriaFuenteDatos.MYSQL);
            default:
                throw new IllegalArgumentException("ERROR: Parámetro de fuente de datos no válido. Use -fdmemoria o -fdmysql.");
        }
    }
}
