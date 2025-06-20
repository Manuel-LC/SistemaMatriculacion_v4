package org.iesalandalus.programacion.matriculacion.modelo.negocio.memoria;

import org.iesalandalus.programacion.matriculacion.modelo.dominio.Asignatura;
import org.iesalandalus.programacion.matriculacion.modelo.negocio.IAsignaturas;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.List;

public class Asignaturas implements IAsignaturas {

    private static List<Asignatura> coleccionAsignaturas;

    public Asignaturas() {
        coleccionAsignaturas = new ArrayList<>();
    }

    @Override
    public void comenzar() {

    }

    @Override
    public void terminar() {

    }

    @Override
    public List<Asignatura> get() {
        return copiaProfundaAsignaturas();
    }

    private static List<Asignatura> copiaProfundaAsignaturas() {
        List<Asignatura> copiaAsignaturas = new ArrayList<>();

        for (Asignatura asignatura : coleccionAsignaturas) {
            copiaAsignaturas.add(new Asignatura(asignatura));
        }

        return copiaAsignaturas;
    }

    @Override
    public int getTamano() {
        return coleccionAsignaturas.size();
    }

    @Override
    public void insertar(Asignatura asignatura) throws OperationNotSupportedException {
        if (asignatura == null) {
            throw new NullPointerException("ERROR: No se puede insertar una asignatura nula.");
        }

        if (!coleccionAsignaturas.contains(asignatura)) {
            coleccionAsignaturas.add(new Asignatura(asignatura));
        } else {
            throw new OperationNotSupportedException("ERROR: Ya existe una asignatura con ese código.");
        }
    }

    public static Asignatura buscar(Asignatura asignatura) {
        if (asignatura == null) {
            throw new NullPointerException("ERROR: No se puede buscar una asignatura nula.");
        }

        int i;

        if (coleccionAsignaturas.contains(asignatura)) {
            i = coleccionAsignaturas.indexOf(asignatura);
            asignatura = coleccionAsignaturas.get(i);
            return new Asignatura(asignatura);
        } else {
            return null;
        }
    }

    @Override
    public void borrar(Asignatura asignatura) throws OperationNotSupportedException {
        if (asignatura == null) {
            throw new NullPointerException("ERROR: No se puede borrar una asignatura nula.");
        }

        if (coleccionAsignaturas.contains(asignatura)) {
            coleccionAsignaturas.remove(asignatura);
        } else {
            throw new OperationNotSupportedException("ERROR: No existe ninguna asignatura como la indicada.");
        }
    }
}
