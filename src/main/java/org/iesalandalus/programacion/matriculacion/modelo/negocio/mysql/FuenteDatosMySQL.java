package org.iesalandalus.programacion.matriculacion.modelo.negocio.mysql;

import org.iesalandalus.programacion.matriculacion.modelo.negocio.*;

public class FuenteDatosMySQL implements IFuenteDatos {
    @Override
    public IAlumnos crearAlumnos() {
        return Alumnos.getInstancia();
    }

    @Override
    public ICiclosFormativos crearCiclosFormativos() {
        return CiclosFormativos.getInstancia();
    }

    @Override
    public IAsignaturas crearAsignaturas() {
        return Asignaturas.getInstancia();
    }

    @Override
    public IMatriculas crearMatriculas() {
        return Matriculas.getInstancia();
    }
}
