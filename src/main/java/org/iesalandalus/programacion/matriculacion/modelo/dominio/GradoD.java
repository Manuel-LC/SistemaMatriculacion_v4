package org.iesalandalus.programacion.matriculacion.modelo.dominio;

public class GradoD extends Grado {

    private Modalidad modalidad;

    public GradoD(String nombre, int numAnios, Modalidad modalidad) {
        super(nombre);
        setNumAnios(numAnios);
        setModalidad(modalidad);
    }

    public Modalidad getModalidad() {
        return modalidad;
    }

    public void setModalidad(Modalidad modalidad) {
        if (modalidad == null) {
            throw new NullPointerException("ERROR: La modalidad de un grado no puede ser nula.");
        }
        if (modalidad != Modalidad.PRESENCIAL && modalidad != Modalidad.SEMIPRESENCIAL) {
            throw new IllegalArgumentException("ERROR: La modalidad tiene que ser presencial o semipresencial.");
        }

        this.modalidad = modalidad;
    }

    public void setNumAnios(int numAnios) {
        if (numAnios < 2 || numAnios > 3) {
            throw new IllegalArgumentException("ERROR: El número de años del grado tipo D tiene que ser 2 o 3");
        }

        this.numAnios = numAnios;
    }

    @Override
    public String toString() {
        return "GradoD{" + super.toString() + ", Años= " + numAnios + ", modalidad=" + modalidad + "} ";
    }
}
