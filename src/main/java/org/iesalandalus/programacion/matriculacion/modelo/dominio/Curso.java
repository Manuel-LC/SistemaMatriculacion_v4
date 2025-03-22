package org.iesalandalus.programacion.matriculacion.modelo.dominio;

public enum Curso {
    PRIMERO("Primero"), SEGUNDO("Segundo");

    private String cadenaAMostrar;

    private Curso(String cadenaAMostrar) {
        this.cadenaAMostrar = cadenaAMostrar;
    }

    public String imprimir() {
        return String.format("%d .- %s", this.ordinal(), this.cadenaAMostrar);
    }

    @Override
    public String toString() {
        return cadenaAMostrar;
    }
}
