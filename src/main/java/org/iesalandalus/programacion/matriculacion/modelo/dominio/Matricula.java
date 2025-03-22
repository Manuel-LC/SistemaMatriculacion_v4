package org.iesalandalus.programacion.matriculacion.modelo.dominio;

import javax.naming.OperationNotSupportedException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Matricula {

    public static final int MAXIMO_MESES_ANTERIOR_ANULACION = 6;
    public static final int MAXIMO_DIAS_ANTERIOR_MATRICULA = 15;
    public static final int MAXIMO_NUMERO_HORAS_MATRICULA = 1000;
    public static final int MAXIMO_NUMERO_ASIGNATURAS_POR_MATRICULA = 10;
    private static final String ER_CURSO_ACADEMICO = "[0-9]{2}-[0-9]{2}";
    public static final String FORMATO_FECHA = "dd/MM/yyyy";

    private int idMatricula;
    private String cursoAcademico;
    private LocalDate fechaMatriculacion;
    private LocalDate fechaAnulacion;
    private Alumno alumno;
    private List<Asignatura> coleccionAsignaturas;

    public Matricula(int idMatricula, String cursoAcademico, LocalDate fechaMatriculacion, Alumno alumno, List<Asignatura> coleccionAsignaturas) throws OperationNotSupportedException {
        setIdMatricula(idMatricula);
        setCursoAcademico(cursoAcademico);
        setFechaMatriculacion(fechaMatriculacion);
        setAlumno(alumno);
        setColeccionAsignaturas(coleccionAsignaturas);
    }

    public Matricula(Matricula matricula) {
        if (matricula == null) {
            throw new NullPointerException("ERROR: No es posible copiar una matrícula nula.");
        }

        this.idMatricula = matricula.idMatricula;
        this.cursoAcademico = matricula.cursoAcademico;
        this.fechaMatriculacion = matricula.fechaMatriculacion;
        this.fechaAnulacion = matricula.fechaAnulacion;
        this.alumno = matricula.alumno;
        this.coleccionAsignaturas = new ArrayList<>();
        for (Asignatura asignatura : matricula.coleccionAsignaturas) {
            this.coleccionAsignaturas.add(new Asignatura(asignatura));
        }
    }

    public int getIdMatricula() {
        return idMatricula;
    }

    public void setIdMatricula(int idMatricula) {
        if (idMatricula <= 0) {
            throw new IllegalArgumentException("ERROR: El identificador de una matrícula no puede ser menor o igual a 0.");
        }

        this.idMatricula = idMatricula;
    }

    public String getCursoAcademico() {
        return cursoAcademico;
    }

    public void setCursoAcademico(String cursoAcademico) {
        if (cursoAcademico == null) {
            throw new NullPointerException("ERROR: El curso académico de una matrícula no puede ser nulo.");
        }
        if (cursoAcademico.isBlank()) {
            throw new IllegalArgumentException("ERROR: El curso académico de una matrícula no puede estar vacío.");
        }
        if (!cursoAcademico.matches(ER_CURSO_ACADEMICO)) {
            throw new IllegalArgumentException("ERROR: El formato del curso académico no es correcto.");
        }

        this.cursoAcademico = cursoAcademico;
    }

    public LocalDate getFechaMatriculacion() {
        return fechaMatriculacion;
    }

    public void setFechaMatriculacion(LocalDate fechaMatriculacion) {
        if (fechaMatriculacion == null) {
            throw new NullPointerException("ERROR: La fecha de matriculación de una mátricula no puede ser nula.");
        }
        if (ChronoUnit.DAYS.between(fechaMatriculacion, LocalDate.now()) > MAXIMO_DIAS_ANTERIOR_MATRICULA) {
            throw new IllegalArgumentException("ERROR: La fecha de matriculación no puede ser anterior a 15 días.");
        }
        if (fechaMatriculacion.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("ERROR: La fecha de matriculación no puede ser posterior a hoy.");
        }

        LocalDate fechaLimite = LocalDate.now().minusDays(MAXIMO_DIAS_ANTERIOR_MATRICULA);

        if (fechaMatriculacion.isBefore(fechaLimite)) {
            throw new IllegalArgumentException("ERROR: La fecha de matriculación no puede ser anterior a " + MAXIMO_DIAS_ANTERIOR_MATRICULA + " días antes de hoy.");
        }

        this.fechaMatriculacion = fechaMatriculacion;
    }

    public LocalDate getFechaAnulacion() {
        return fechaAnulacion;
    }

    public void setFechaAnulacion(LocalDate fechaAnulacion) {
        if (fechaAnulacion == null) {
            throw new NullPointerException("ERROR: La fecha de anulación no puede ser nula.");
        }
        if (ChronoUnit.MONTHS.between(this.fechaMatriculacion, fechaAnulacion) > MAXIMO_MESES_ANTERIOR_ANULACION) {
            throw new IllegalArgumentException("ERROR: La fecha de anulación no puede superar los 6 meses.");
        }
        if (fechaAnulacion.isBefore(fechaMatriculacion)) {
            throw new IllegalArgumentException("ERROR: La fecha de anulación no puede ser anterior a la fecha de matriculación.");
        }
        if (fechaAnulacion.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("ERROR: La fecha de anulación de una matrícula no puede ser posterior a hoy.");
        }

        this.fechaAnulacion = fechaAnulacion;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        if (alumno == null) {
            throw new NullPointerException("ERROR: El alumno de una matrícula no puede ser nulo.");
        }

        this.alumno = alumno;
    }

    public List<Asignatura> getColeccionAsignaturas() {
        return coleccionAsignaturas;
    }

    public void setColeccionAsignaturas(List<Asignatura> coleccionAsignaturas) throws OperationNotSupportedException {
        if (coleccionAsignaturas == null) {
            throw new NullPointerException("ERROR: La lista de asignaturas de una matrícula no puede ser nula.");
        }
        if (coleccionAsignaturas.size() > MAXIMO_NUMERO_ASIGNATURAS_POR_MATRICULA) {
            throw new IllegalArgumentException("ERROR: El número máximo de asignaturas por matrícula es 10.");
        }
        if (superaMaximoNumeroHorasMatricula(coleccionAsignaturas)) {
            throw new OperationNotSupportedException("ERROR: No se puede realizar la matrícula ya que supera el máximo de horas permitidas (1000 horas).");
        }

        this.coleccionAsignaturas = new ArrayList<>();
        for (Asignatura asignatura : coleccionAsignaturas) {
            if (asignatura != null) {
                this.coleccionAsignaturas.add(new Asignatura(asignatura));
            }
        }
    }

    private static boolean superaMaximoNumeroHorasMatricula(List<Asignatura> asignaturasMatricula) {
        int totalHoras = 0;

        for (Asignatura asignatura : asignaturasMatricula) {
            if (asignatura != null) {
                totalHoras += asignatura.getHorasAnuales();
            }
        }

        return totalHoras > MAXIMO_NUMERO_HORAS_MATRICULA;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matricula matricula = (Matricula) o;
        return Objects.equals(this.idMatricula, matricula.idMatricula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.idMatricula);
    }

    private String asignaturasMatricula() {
        if (coleccionAsignaturas == null || coleccionAsignaturas.isEmpty()) {
            return "No hay asignaturas matriculadas.";
        }

        StringBuilder asignaturasMatriculadas = new StringBuilder();

        for (Asignatura asignatura : coleccionAsignaturas) {
            asignaturasMatriculadas.append(asignatura.imprimir()).append(", ");
        }

        if (asignaturasMatriculadas.toString().endsWith(", ")) {
            asignaturasMatriculadas.setLength(asignaturasMatriculadas.length() - 2);
        }

        return asignaturasMatriculadas.toString();
    }

    public String imprimir() {
        return "idMatricula=" + idMatricula + ", curso académico=" + cursoAcademico + ", fecha matriculación="
                + fechaMatriculacion.format(DateTimeFormatter.ofPattern(Matricula.FORMATO_FECHA)) + ", alumno={"
                + alumno + '}';
    }

    @Override
    public String toString() {

        if (fechaAnulacion == null) {

            return String.format("idMatricula=%d, curso académico=%s, fecha matriculación=%s, alumno=%s, Asignaturas={ %s}",
                    idMatricula, cursoAcademico,
                    fechaMatriculacion.format(DateTimeFormatter.ofPattern(Matricula.FORMATO_FECHA)),
                    alumno.imprimir(), asignaturasMatricula());
        } else {

            return String.format("idMatricula=%d, curso académico=%s, fecha matriculación=%s, fecha anulación=%s, alumno=%s, Asignaturas={ %s}",
                    idMatricula, cursoAcademico,
                    fechaMatriculacion.format(DateTimeFormatter.ofPattern(Matricula.FORMATO_FECHA)),
                    fechaAnulacion.format(DateTimeFormatter.ofPattern(Matricula.FORMATO_FECHA)),
                    alumno.imprimir(), asignaturasMatricula());
        }
    }
}
