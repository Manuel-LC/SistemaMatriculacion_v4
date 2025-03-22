package org.iesalandalus.programacion.matriculacion.controlador;

import org.iesalandalus.programacion.matriculacion.modelo.Modelo;
import org.iesalandalus.programacion.matriculacion.modelo.dominio.Alumno;
import org.iesalandalus.programacion.matriculacion.modelo.dominio.Asignatura;
import org.iesalandalus.programacion.matriculacion.modelo.dominio.CicloFormativo;
import org.iesalandalus.programacion.matriculacion.modelo.dominio.Matricula;
import org.iesalandalus.programacion.matriculacion.vista.Vista;

import javax.naming.OperationNotSupportedException;
import java.util.List;

public class Controlador {

    private Modelo modelo;
    private Vista vista;

    public Controlador(Modelo modelo, Vista vista) {
        if (modelo == null)
            throw new NullPointerException("ERROR: El modelo no puede ser nulo.");

        if (vista == null)
            throw new NullPointerException("ERROR: La vista no puede ser nula.");

        this.modelo = modelo;
        this.vista = vista;
        vista.setControlador(this);
    }

    public void comenzar() {
        modelo.comenzar();
        vista.comenzar();
    }

    public void terminar() {
        modelo.terminar();
    }

    public void insertar(Alumno alumno) throws OperationNotSupportedException {
        modelo.insertar(alumno);
    }

    public Alumno buscar(Alumno alumno) {
        return modelo.buscar(alumno);
    }

    public void borrar(Alumno alumno) throws OperationNotSupportedException {
        modelo.borrar(alumno);
    }

    public List<Alumno> getAlumnos() {
        return modelo.getAlumnos();
    }

    public void insertar(Asignatura asignatura) throws OperationNotSupportedException {
        modelo.insertar(asignatura);
    }

    public Asignatura buscar(Asignatura asignatura) {
        return modelo.buscar(asignatura);
    }

    public void borrar(Asignatura asignatura) throws OperationNotSupportedException {
        modelo.borrar(asignatura);
    }

    public List<Asignatura> getAsignaturas() {
        return modelo.getAsignaturas();
    }

    public void insertar(CicloFormativo cicloFormativo) throws OperationNotSupportedException {
        modelo.insertar(cicloFormativo);
    }

    public CicloFormativo buscar(CicloFormativo cicloFormativo) {
        return modelo.buscar(cicloFormativo);
    }

    public void borrar(CicloFormativo cicloFormativo) throws OperationNotSupportedException {
        modelo.borrar(cicloFormativo);
    }

    public List<CicloFormativo> getCiclosFormativos() {
        return modelo.getCiclosFormativos();
    }

    public void insertar(Matricula matricula) throws OperationNotSupportedException {
        modelo.insertar(matricula);
    }

    public Matricula buscar(Matricula matricula) {
        return modelo.buscar(matricula);
    }

    public void borrar(Matricula matricula) throws OperationNotSupportedException {
        modelo.borrar(matricula);
    }

    public List<Matricula> getMatriculas() {
        return modelo.getMatriculas();
    }

    public List<Matricula> getMatriculas(Alumno alumno) {
        return modelo.getMatriculas(alumno);
    }

    public List<Matricula> getMatriculas(CicloFormativo cicloFormativo) {
        return modelo.getMatriculas(cicloFormativo);
    }

    public List<Matricula> getMatriculas(String cursoAcademico) {
        return modelo.getMatriculas(cursoAcademico);
    }
}
