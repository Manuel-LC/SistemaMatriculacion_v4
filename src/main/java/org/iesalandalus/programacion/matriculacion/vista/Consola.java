package org.iesalandalus.programacion.matriculacion.vista;

import org.iesalandalus.programacion.matriculacion.modelo.dominio.*;
import org.iesalandalus.programacion.matriculacion.modelo.negocio.Asignaturas;
import org.iesalandalus.programacion.utilidades.Entrada;

import javax.naming.OperationNotSupportedException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Consola {

    // Constructor privado para evitar instanciación
    private Consola() {
    }

    // Mostrar menú basado en las opciones del enumerado Opcion
    public static void mostrarMenu() {
        System.out.println();
        System.out.println("===============================================================================================");
        System.out.println("Sistema de matriculación del IES Al-Andalus");
        System.out.println("===============================================================================================");
        for (Opcion opcion : Opcion.values()) {
            System.out.println(opcion);
        }
    }

    // Elegir una opción del enumerado
    public static Opcion elegirOpcion() {
        int opcion;

        do {
            System.out.print("Elige una opción: ");
            opcion = Entrada.entero();
            System.out.println();
        } while (opcion < 0 || opcion > Opcion.values().length);

        return Opcion.values()[opcion];
    }

    // Leer datos de un alumno
    public static Alumno leerAlumno() {

        System.out.print("Introduce el DNI del alumno: ");
        String dni = Entrada.cadena();
        System.out.print("Introduce el nombre del alumno: ");
        String nombre = Entrada.cadena();
        System.out.print("Introduce el correo del alumno: ");
        String correo = Entrada.cadena();
        System.out.print("Introduce el número de teléfono del alumno: ");
        String telefono = Entrada.cadena();

        LocalDate fechaNacimiento;
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        System.out.print("Introduce la fecha de nacimiento del alumno: ");
        String fecha = Entrada.cadena();

        fechaNacimiento = LocalDate.parse(fecha, formato);

        return new Alumno(nombre, dni, correo, telefono, fechaNacimiento);
    }

    // Leer un alumno por DNI
    public static Alumno getAlumnoPorDni() {
        System.out.print("Introduce el DNI del alumno: ");
        String dni = Entrada.cadena();
        System.out.println();

        return new Alumno("Pablo garcía", dni, "pg123@gmail.com", "123456789", LocalDate.of(2000, 3, 25));
    }

    // Leer una fecha
    public static LocalDate leerFecha(String mensaje) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fecha = null;
        boolean fechaValida = false;

         do {
            System.out.print(mensaje + " (dd/MM/yyyy): ");
            String cad = Entrada.cadena();

            try {
                fecha = LocalDate.parse(String.format(cad, formato), formato);
                fechaValida = true;
            }
            catch (DateTimeParseException e) {
                System.out.println("ERROR: Fecha no válida.");
            }
         } while (!fechaValida);

        return fecha;
    }

    // Tipo de grado
    public static TiposGrado leerTiposGrado() {
        for (TiposGrado grado : TiposGrado.values()) {
            System.out.println(grado.imprimir());
        }

        System.out.print("Introduce el tipo de grado (0 o 1): ");
        int opcion = Entrada.entero();

        if (opcion < 0 || opcion > 1) {
            throw new IllegalArgumentException("ERROR: Opción incorrecta.");
        }

        return TiposGrado.values()[opcion];
    }

    // Modalidad del grado
    public static Modalidad leerModalidad() {
        for (Modalidad modalidad : Modalidad.values()) {
            System.out.println(modalidad.imprimir());
        }

        System.out.print("Introduce la modalidad del grado (0 o 1): ");
        int opcion = Entrada.entero();

        if (opcion < 0 || opcion > 1) {
            throw new IllegalArgumentException("ERROR: Opción incorrecta.");
        }

        return Modalidad.values()[opcion];
    }

    // Leer grado de un tipo
    public static Grado leerGrado() {

        System.out.print("Introduce el nombre del grado: ");
        String nombre = Entrada.cadena().trim();
        TiposGrado tipoGrado = leerTiposGrado();
        System.out.print("Introduce el número de años (Grado D = 2 o 3, Grado E = 1):");
        int numAnios = Entrada.entero();

        // Dependiendo del tipo de grado crea un objeto
        if (tipoGrado == TiposGrado.GRADOD) {
            Modalidad modalidad = leerModalidad();

            return new GradoD(nombre, numAnios, modalidad);
        } else {
            System.out.print("Introduce el número de ediciones:");
            int numEdiciones = Entrada.entero();

            return new GradoE(nombre, numAnios, numEdiciones);
        }
    }

    // Leer un ciclo formativo
    public static CicloFormativo leerCicloFormativo() {
        System.out.print("Introduce el código del ciclo formativo (4 dígitos): ");
        int codigo = Entrada.entero();
        System.out.print("Introduce el nombre del ciclo formativo: ");
        String nombre = Entrada.cadena();
        System.out.print("Introduce la familia profesional del ciclo formativo: ");
        String familiaProfesional = Entrada.cadena();
        System.out.print("Introduce las horas del ciclo formativo (máx. 2000): ");
        int horas = Entrada.entero();
        System.out.println("-- Grado del ciclo formativo --");
        Grado grado = leerGrado();
        System.out.println();

        return new CicloFormativo(codigo, familiaProfesional, grado, nombre, horas);
    }

    // Mostrar ciclos formativos
    public static void mostrarCiclosFormativos(List<CicloFormativo> ciclosFormativos) {
        if (ciclosFormativos.isEmpty()) {
            System.out.println("No hay ciclos formativos registrados.");
        } else {
            System.out.println("Lista de ciclos formativos disponibles:");
            ciclosFormativos.forEach(System.out::println);
        }
    }

    // Obtener un ciclo formativo por código
    public static CicloFormativo getCicloFormativoPorCodigo() {
        System.out.print("Introduce el código del ciclo formativo: ");
        int codigo = Entrada.entero();

        return new CicloFormativo(codigo, "Informática y Comunicaciones",  new GradoE("Diseño 3D", 1, 2), "DAW", 1300);
    }

    // Leer un curso
    public static Curso leerCurso() {

        for (Curso curso : Curso.values()) {
            System.out.println(curso.imprimir());
        }

        System.out.println("Introduce el curso (0 ó 1):");
        int opcion = Entrada.entero();

        if (opcion < 0 || opcion > 1) {
            throw new IllegalArgumentException("ERROR: Opción incorrecta.");
        }

        return Curso.values()[opcion];
    }

    // Leer especialidad del profesorado
    public static EspecialidadProfesorado leerEspecialidadProfesorado() {

        for (EspecialidadProfesorado especialidad : EspecialidadProfesorado.values()) {
            System.out.println(especialidad.imprimir());
        }

        System.out.println("Introduce la especialidad del profesorado (0-2):");
        int opcion = Entrada.entero();

        if (opcion < 0 || opcion > 2) {
            throw new IllegalArgumentException("ERROR: Opción incorrecta.");
        }

        return EspecialidadProfesorado.values()[opcion];
    }

    // Leer una asignatura
    public static Asignatura leerAsignatura(CicloFormativo cicloFormativo) {
        System.out.print("Introduce el código de la asignatura (4 dígitos): ");
        String codigo = Entrada.cadena();
        System.out.print("Introduce el nombre de la asignatura: ");
        String nombre = Entrada.cadena();
        System.out.print("Introduce las horas anuales para la asignatura (máx. 300): ");
        int horasAnuales = Entrada.entero();
        System.out.print("Introduce las horas desdoble para la asignatura (máx. 6): ");
        int horasDesdoble = Entrada.entero();
        Curso curso = leerCurso();
        EspecialidadProfesorado especialidadProfesorado = leerEspecialidadProfesorado();

        System.out.println();

        return new Asignatura(codigo, nombre, horasAnuales, curso, horasDesdoble, especialidadProfesorado, cicloFormativo);
    }

    // Obtener una asignatura por código
    public static Asignatura getAsignaturaPorCodigo() {
        System.out.print("Introduce el código de la asignatura: ");
        String codigo = Entrada.cadena();

        return new Asignatura(codigo, "Programación", 300, Curso.PRIMERO, 4, EspecialidadProfesorado.INFORMATICA, new CicloFormativo(1111, "Informática y comunicaciones", new GradoD("Superior", 2, Modalidad.SEMIPRESENCIAL), "DAW", 1000));
    }

    // Mostrar asignaturas
    private static void mostrarAsignaturas(List<Asignatura> asignaturas) {
        System.out.println("Lista de asignaturas disponibles:");
        for (Asignatura asignatura : asignaturas) {
            System.out.println(asignatura);
        }
    }

    // Comprobar si la asignatura ya está matriculada
    private static boolean asignaturaYaMatriculada(List<Asignatura> asignaturasMatricula, Asignatura asignatura) {
        boolean encontrada = false;

        for (Asignatura asignaturaMatriculada : asignaturasMatricula) {
            if (asignaturaMatriculada.equals(asignatura)) {
                encontrada = true;
                break;
            }
        }
        return encontrada;
    }

    // Leer una matrícula
    public static Matricula leerMatricula(Alumno alumno, List<Asignatura> asignaturas) throws OperationNotSupportedException {
        System.out.print("Introduce el identificador de la matrícula: ");
        int idMatricula = Entrada.entero();
        System.out.print("Introduce el curso académico: ");
        String cursoAcademico = Entrada.cadena();
        LocalDate fechaMatriculacion = leerFecha("Introduce la fecha de matriculación");
        System.out.println();

        return new Matricula(idMatricula, cursoAcademico, fechaMatriculacion, alumno, asignaturas);
    }

    public static List<Asignatura> elegirAsignaturasMatricula(List<Asignatura> asignaturas) throws OperationNotSupportedException {
        // Añadir asignaturas a la matrícula
        List<Asignatura> asignaturasMatricula = new ArrayList<>();

        int opcion;
        do {
            mostrarAsignaturas(asignaturas);
            System.out.println();
            Asignatura asignatura = getAsignaturaPorCodigo();
            asignatura = Asignaturas.buscar(asignatura);

            if (asignaturaYaMatriculada(asignaturasMatricula, asignatura)) {
                throw new OperationNotSupportedException("ERROR: Esa asignatura ya está matriculada.");
            } else {
                asignaturasMatricula.add(new Asignatura(asignatura));
                System.out.println("\nAsignatura añadida.");
            }

            System.out.println();
            System.out.println("¿Quieres añadir otra asignatura?");
            System.out.println("0.- No");
            System.out.println("1.- Si");
            opcion = Entrada.entero();

        } while (opcion == 1);

        return asignaturasMatricula;
    }

    // Obtener una matrícula por identificador
    public static Matricula getMatriculaPorIdentificador() throws OperationNotSupportedException {
        System.out.print("Introduce el identificador de la matrícula: ");
        int idMatricula = Entrada.entero();

        return new Matricula(idMatricula, "24-25", LocalDate.now().minusDays(10), new Alumno("Antonio rodríguez cuenca", "77241354V", "arc123@hotmail.com", "676873431", LocalDate.of(1999, 6, 14)), new ArrayList<>());
    }
}
