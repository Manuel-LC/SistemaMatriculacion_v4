package org.iesalandalus.programacion.matriculacion.modelo.dominio;

public enum EspecialidadProfesorado {
    INFORMATICA("Infórmatica"), SISTEMAS("Sistemas"), FOL("FOL");

    private String cadenaAMostrar;

    private EspecialidadProfesorado(String cadenaAMostrar) {
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
