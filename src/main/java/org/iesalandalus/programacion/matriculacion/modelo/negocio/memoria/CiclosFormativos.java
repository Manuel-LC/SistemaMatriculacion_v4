package org.iesalandalus.programacion.matriculacion.modelo.negocio.memoria;

import org.iesalandalus.programacion.matriculacion.modelo.dominio.CicloFormativo;
import org.iesalandalus.programacion.matriculacion.modelo.negocio.ICiclosFormativos;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.List;

public class CiclosFormativos implements ICiclosFormativos {

    private static List<CicloFormativo> coleccionCiclosFormativos;

    public CiclosFormativos() {
        coleccionCiclosFormativos = new ArrayList<>();
    }

    @Override
    public void comenzar() {

    }

    @Override
    public void terminar() {

    }

    @Override
    public List<CicloFormativo> get() {
        return copiaProfundaCiclosFormativos();
    }

    private static List<CicloFormativo> copiaProfundaCiclosFormativos() {
        List<CicloFormativo> copiaCiclosFormativos = new ArrayList<>();

        for (CicloFormativo cicloFormativo : coleccionCiclosFormativos) {
            copiaCiclosFormativos.add(new CicloFormativo(cicloFormativo));
        }

        return copiaCiclosFormativos;
    }

    @Override
    public int getTamano() {
        return coleccionCiclosFormativos.size();
    }

    @Override
    public void insertar(CicloFormativo cicloFormativo) throws OperationNotSupportedException {
        if (cicloFormativo == null) {
            throw new NullPointerException("ERROR: No se puede insertar un ciclo formativo nulo.");
        }

        if (!coleccionCiclosFormativos.contains(cicloFormativo)) {
            coleccionCiclosFormativos.add(new CicloFormativo(cicloFormativo));
        } else {
            throw new OperationNotSupportedException("ERROR: Ya existe un ciclo formativo con ese código.");
        }
    }

    @Override
    public CicloFormativo buscar(CicloFormativo cicloFormativo) {
        if (cicloFormativo == null) {
            throw new NullPointerException("ERROR: No se puede buscar un ciclo formativo nulo.");
        }

        int i;

        if (coleccionCiclosFormativos.contains(cicloFormativo)) {
            i = coleccionCiclosFormativos.indexOf(cicloFormativo);
            cicloFormativo = coleccionCiclosFormativos.get(i);
            return new CicloFormativo(cicloFormativo);
        } else {
            return null;
        }
    }

    @Override
    public void borrar(CicloFormativo cicloFormativo) throws OperationNotSupportedException {
        if (cicloFormativo == null) {
            throw new NullPointerException("ERROR: No se puede borrar un ciclo formativo nulo.");
        }

        if (coleccionCiclosFormativos.contains(cicloFormativo)) {
            coleccionCiclosFormativos.remove(cicloFormativo);
        } else {
            throw new OperationNotSupportedException("ERROR: No existe ningún ciclo formativo como el indicado.");
        }
    }
}
