package org.iesalandalus.programacion.matriculacion.modelo.dominio;

public class GradoE extends Grado{

    private int numEdiciones;

    public GradoE(String nombre, int numAnios, int numEdiciones) {
        super(nombre);
        setNumAnios(numAnios);
        setNumEdiciones(numEdiciones);
    }

    public int getNumEdiciones() {
        return numEdiciones;
    }

    public void setNumEdiciones(int numEdiciones) {
        if (numEdiciones < 0) {
            throw new NullPointerException("ERROR: El número de ediciones de un grado no puede ser menor que 0.");
        }

        this.numEdiciones = numEdiciones;
    }

    public void setNumAnios(int numAnios) {
        if (numAnios != 1) {
            throw new IllegalArgumentException("ERROR: El número de años del grado tipo E tiene que ser 1.");
        }

        this.numAnios = numAnios;
    }

    @Override
    public String toString() {
        return "GradoE{" + super.toString() + ", Años= " + numAnios + ", Ediciones=" + numEdiciones + "} ";
    }
}
