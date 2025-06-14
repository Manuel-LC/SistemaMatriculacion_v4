package org.iesalandalus.programacion.matriculacion.modelo.negocio.memoria;

import org.iesalandalus.programacion.matriculacion.modelo.dominio.Alumno;
import org.iesalandalus.programacion.matriculacion.modelo.dominio.Asignatura;
import org.iesalandalus.programacion.matriculacion.modelo.dominio.CicloFormativo;
import org.iesalandalus.programacion.matriculacion.modelo.dominio.Matricula;
import org.iesalandalus.programacion.matriculacion.modelo.negocio.IMatriculas;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.List;

public class Matriculas implements IMatriculas {

    private static List<Matricula> coleccionMatriculas;

    public Matriculas() {
        coleccionMatriculas = new ArrayList<>();
    }

    @Override
    public void comenzar() {

    }

    @Override
    public void terminar() {

    }

    @Override
    public List<Matricula> get() {
        return copiaProfundaMatriculas();
    }

    private List<Matricula> copiaProfundaMatriculas() {
        List<Matricula> copiaMatriculas = new ArrayList<>();

        for (Matricula matricula : coleccionMatriculas) {
            copiaMatriculas.add(new Matricula(matricula));
        }

        return copiaMatriculas;
    }

    @Override
    public int getTamano() {
        return coleccionMatriculas.size();
    }

    @Override
    public void insertar(Matricula matricula) throws OperationNotSupportedException {
        if (matricula == null) {
            throw new NullPointerException("ERROR: No se puede insertar una matrícula nula.");
        }

        if (!coleccionMatriculas.contains(matricula)) {
            coleccionMatriculas.add(new Matricula(matricula));
        } else {
            throw new OperationNotSupportedException("ERROR: Ya existe una matrícula con ese identificador.");
        }
    }

    @Override
    public Matricula buscar(Matricula matricula) {
        if (matricula == null) {
            throw new NullPointerException("ERROR: No se puede buscar una matrícula nula.");
        }

        int i;

        if (coleccionMatriculas.contains(matricula)) {
            i = coleccionMatriculas.indexOf(matricula);
            matricula = coleccionMatriculas.get(i);
            return matricula;
        } else {
            return null;
        }
    }

    @Override
    public void borrar(Matricula matricula) throws OperationNotSupportedException {
        if (matricula == null) {
            throw new NullPointerException("ERROR: No se puede borrar una matrícula nula.");
        }

        if (coleccionMatriculas.contains(matricula)) {
            coleccionMatriculas.remove(matricula);
        } else {
            throw new OperationNotSupportedException("ERROR: No existe ninguna matrícula como la indicada.");
        }
    }

    @Override
    public List<Matricula> get(Alumno alumno) {
        List<Matricula> coleccionMatriculasAlumno = new ArrayList<>();

        for (Matricula matricula : coleccionMatriculas) {
            if (matricula.getAlumno().equals(alumno)) {
                coleccionMatriculasAlumno.add(matricula);
            }
        }
        return coleccionMatriculasAlumno;
    }

    @Override
    public List<Matricula> get(String cursoAcademico) {
        List<Matricula> coleccionMatriculasCurso = new ArrayList<>();

        for (Matricula matricula : coleccionMatriculas) {
            if (matricula.getCursoAcademico().equals(cursoAcademico)) {
                coleccionMatriculasCurso.add(matricula);
            }
        }
        return coleccionMatriculasCurso;
    }

    @Override
    public List<Matricula> get(CicloFormativo cicloFormativo) {
        List<Matricula> coleccionMatriculasCiclo = new ArrayList<>();

        if (cicloFormativo == null) {
            throw new IllegalArgumentException("ERROR: El ciclo formativo no puede ser nulo.");
        }

        for (Matricula matricula : coleccionMatriculas) {
            List<Asignatura> asignaturasMatricula = matricula.getColeccionAsignaturas();

            for (Asignatura asignatura : asignaturasMatricula) {
                if (asignatura.getCicloFormativo().equals(cicloFormativo)) {
                    coleccionMatriculasCiclo.add(matricula);
                    break;
                }
            }
        }

        return coleccionMatriculasCiclo;
    }
}
