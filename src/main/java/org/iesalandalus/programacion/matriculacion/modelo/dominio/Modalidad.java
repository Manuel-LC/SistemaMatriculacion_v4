package org.iesalandalus.programacion.matriculacion.modelo.dominio;

public enum Modalidad {
    PRESENCIAL("Presencial"), SEMIPRESENCIAL("Semipresencial");

    private String cadenaAMostrar;

    private Modalidad(String cadenaAMostrar) {
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
