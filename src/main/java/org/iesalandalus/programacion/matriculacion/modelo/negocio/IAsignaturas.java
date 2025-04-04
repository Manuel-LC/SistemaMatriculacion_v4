package org.iesalandalus.programacion.matriculacion.modelo.negocio;

import org.iesalandalus.programacion.matriculacion.modelo.dominio.Asignatura;

import javax.naming.OperationNotSupportedException;
import java.util.List;

public interface IAsignaturas {
    void comenzar();
    void terminar();
    List<Asignatura> get();
    int getTamano();
    void insertar(Asignatura asignatura) throws OperationNotSupportedException;

    static Asignatura buscar(Asignatura asignatura) {
        return null;
    }

    void borrar(Asignatura asignatura) throws OperationNotSupportedException;
}
