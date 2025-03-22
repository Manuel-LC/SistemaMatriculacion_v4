package org.iesalandalus.programacion.matriculacion;

import org.iesalandalus.programacion.matriculacion.controlador.Controlador;
import org.iesalandalus.programacion.matriculacion.modelo.Modelo;
import org.iesalandalus.programacion.matriculacion.vista.Vista;

public class MainApp {

    public static void main(String[] args) {
        System.out.println("Iniciando la aplicación...");

        Modelo modelo = new Modelo();
        Vista vista = new Vista();
        Controlador controlador = new Controlador(modelo, vista);
        controlador.comenzar();

        System.out.println("Aplicación cerrada.");
    }
}
