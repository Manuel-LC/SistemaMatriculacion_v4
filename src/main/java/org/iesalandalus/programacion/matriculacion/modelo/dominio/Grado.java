package org.iesalandalus.programacion.matriculacion.modelo.dominio;

public abstract class Grado {

    protected String nombre;
    protected String iniciales;
    protected int numAnios;

    public Grado(String nombre) {
        if (nombre == null) {
            throw new NullPointerException("ERROR: El nombre de un grado no puede ser nulo.");
        }
        if (nombre.isBlank()) {
            throw new IllegalArgumentException("ERROR: El nombre de un grado no puede estar vacío.");
        }

        setNombre(nombre);
        setIniciales();
    }

    public String getNombre() {
        return nombre;
    }

    protected void setNombre(String nombre) {
        if (nombre == null) {
            throw new NullPointerException("ERROR: El nombre de un grado no puede ser nulo.");
        }
        if (nombre.isBlank()) {
            throw new IllegalArgumentException("ERROR: El nombre de un grado no puede estar vacío.");
        }
        this.nombre = nombre;
    }

    private void setIniciales() {

        StringBuilder iniciales = new StringBuilder();
        String[] palabras = nombre.split("\\s+");

        for (String palabra : palabras) {
            if (!palabra.isEmpty()) {
                iniciales.append(palabra.charAt(0));
            }
        }

        this.iniciales = iniciales.toString().toUpperCase();
    }

    public abstract void setNumAnios(int numAnios);

    @Override
    public String toString() {
        return "(" + iniciales + ") - " + nombre;
    }
}
