package org.iesalandalus.programacion.matriculacion.modelo.negocio.memoria;

import org.iesalandalus.programacion.matriculacion.modelo.negocio.*;

public class FuenteDatosMemoria implements IFuenteDatos {
    public IAlumnos crearAlumnos() {
        return new Alumnos();
    }
    public ICiclosFormativos crearCiclosFormativos() {
        return new CiclosFormativos();
    }
    public IAsignaturas crearAsignaturas() {
        return new Asignaturas();
    }
    public IMatriculas crearMatriculas() {
        return new Matriculas();
    }
}
