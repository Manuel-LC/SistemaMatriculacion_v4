package org.iesalandalus.programacion.matriculacion.modelo.negocio.memoria;

import org.iesalandalus.programacion.matriculacion.modelo.dominio.Alumno;
import org.iesalandalus.programacion.matriculacion.modelo.negocio.IAlumnos;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.List;

public class Alumnos implements IAlumnos {

    private static List<Alumno> coleccionAlumnos;

    public Alumnos() {
        coleccionAlumnos = new ArrayList<>();
    }

    @Override
    public void comenzar() {

    }

    @Override
    public void terminar() {

    }

    @Override
    public List<Alumno> get() {
        return copiaProfundaAlumnos();
    }

    private List<Alumno> copiaProfundaAlumnos() {
        List<Alumno> copiaAlumnos = new ArrayList<>();

        for (Alumno alumno : coleccionAlumnos) {
            copiaAlumnos.add(new Alumno(alumno));
        }

        return copiaAlumnos;
    }

    @Override
    public int getTamano() {
        return coleccionAlumnos.size();
    }

    @Override
    public void insertar(Alumno alumno) throws OperationNotSupportedException {
        if (alumno == null) {
            throw new NullPointerException("ERROR: No se puede insertar un alumno nulo.");
        }

        if (!coleccionAlumnos.contains(alumno)) {
            coleccionAlumnos.add(new Alumno(alumno));
        } else {
            throw new OperationNotSupportedException("ERROR: Ya existe un alumno con ese dni.");
        }
    }

    @Override
    public Alumno buscar(Alumno alumno) {
        if (alumno == null) {
            throw new NullPointerException("ERROR: No se puede buscar un alumno nulo.");
        }

        int i;

        if (coleccionAlumnos.contains(alumno)) {
            i = coleccionAlumnos.indexOf(alumno);
            alumno = coleccionAlumnos.get(i);
            return new Alumno(alumno);
        } else {
            return null;
        }
    }

    @Override
    public void borrar(Alumno alumno) throws OperationNotSupportedException {
        if (alumno == null) {
            throw new NullPointerException("ERROR: No se puede borrar un alumno nulo.");
        }

        if (coleccionAlumnos.contains(alumno)) {
            coleccionAlumnos.remove(alumno);
        } else {
            throw new OperationNotSupportedException("ERROR: No existe ningún alumno como el indicado.");
        }
    }
}
