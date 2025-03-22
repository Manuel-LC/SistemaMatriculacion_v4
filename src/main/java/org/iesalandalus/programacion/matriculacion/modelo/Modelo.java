package org.iesalandalus.programacion.matriculacion.modelo;

import org.iesalandalus.programacion.matriculacion.modelo.dominio.Alumno;
import org.iesalandalus.programacion.matriculacion.modelo.dominio.Asignatura;
import org.iesalandalus.programacion.matriculacion.modelo.dominio.CicloFormativo;
import org.iesalandalus.programacion.matriculacion.modelo.dominio.Matricula;
import org.iesalandalus.programacion.matriculacion.modelo.negocio.*;

import javax.naming.OperationNotSupportedException;
import java.util.List;

public class Modelo {

    private Alumnos alumnos;
    private Asignaturas asignaturas;
    private CiclosFormativos ciclosFormativos;
    private Matriculas matriculas;

    public void comenzar() {
        alumnos = new Alumnos();
        asignaturas = new Asignaturas();
        ciclosFormativos = new CiclosFormativos();
        matriculas = new Matriculas();
        System.out.println("Modelo iniciado.");
    }

    public void terminar() {
        System.out.println("Modelo terminado.");
    }

    public void insertar(Alumno alumno) throws OperationNotSupportedException {
        alumnos.insertar(alumno);
    }

    public Alumno buscar(Alumno alumno) {
        return alumnos.buscar(alumno);
    }

    public void borrar(Alumno alumno) throws OperationNotSupportedException {
        alumnos.borrar(alumno);
    }

    public List<Alumno> getAlumnos() {
        return alumnos.get();
    }

    public void insertar(Asignatura asignatura) throws OperationNotSupportedException {
        asignaturas.insertar(asignatura);
    }

    public Asignatura buscar(Asignatura asignatura) {
        return asignaturas.buscar(asignatura);
    }

    public void borrar(Asignatura asignatura) throws OperationNotSupportedException {
        asignaturas.borrar(asignatura);
    }

    public List<Asignatura> getAsignaturas() {
        return asignaturas.get();
    }

    public void insertar(CicloFormativo cicloFormativo) throws OperationNotSupportedException {
        ciclosFormativos.insertar(cicloFormativo);
    }

    public CicloFormativo buscar(CicloFormativo cicloFormativo) {
        return ciclosFormativos.buscar(cicloFormativo);
    }

    public void borrar(CicloFormativo cicloFormativo) throws OperationNotSupportedException {
        ciclosFormativos.borrar(cicloFormativo);
    }

    public List<CicloFormativo> getCiclosFormativos() {
        return ciclosFormativos.get();
    }

    public void insertar(Matricula matricula) throws OperationNotSupportedException {
        matriculas.insertar(matricula);
    }

    public Matricula buscar(Matricula matricula) {
        return matriculas.buscar(matricula);
    }

    public void borrar(Matricula matricula) throws OperationNotSupportedException {
        matriculas.borrar(matricula);
    }

    public List<Matricula> getMatriculas() {
        return matriculas.get();
    }

    public List<Matricula> getMatriculas(Alumno alumno) {
        return matriculas.get(alumno);
    }

    public List<Matricula> getMatriculas(CicloFormativo cicloFormativo) {
        return matriculas.get(cicloFormativo);
    }

    public List<Matricula> getMatriculas(String cursoAcademico) {
        return matriculas.get(cursoAcademico);
    }
}
