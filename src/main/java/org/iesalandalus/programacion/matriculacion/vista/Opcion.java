package org.iesalandalus.programacion.matriculacion.vista;

public enum Opcion {

    SALIR("Salir") {
        @Override
        public void ejecutar() {
            vista.terminar();
        }
    },
    INSERTAR_ALUMNO("Insertar alumno") {
        @Override
        public void ejecutar() {
            vista.insertarAlumno();
        }
    },
    BUSCAR_ALUMNO("Buscar alumno") {
        @Override
        public void ejecutar() {
            vista.buscarAlumno();
        }
    },
    BORRAR_ALUMNO("Borrar alumno") {
        @Override
        public void ejecutar() {
            vista.borrarAlumno();
        }
    },
    MOSTRAR_ALUMNOS("Mostrar alumnos") {
        @Override
        public void ejecutar() {
            vista.mostrarAlumnos();
        }
    },
    INSERTAR_CICLO_FORMATIVO("Insertar ciclo formativo") {
        @Override
        public void ejecutar() {
            vista.insertarCicloFormativo();
        }
    },
    BUSCAR_CICLO_FORMATIVO("Buscar ciclo formativo") {
        @Override
        public void ejecutar() {
            vista.buscarCicloFormativo();
        }
    },
    BORRAR_CICLO_FORMATIVO("Borrar ciclo formativo") {
        @Override
        public void ejecutar() {
            vista.borrarCicloFormativo();
        }
    },
    MOSTRAR_CICLOS_FORMATIVOS("Mostrar ciclos formativos") {
        @Override
        public void ejecutar() {
            vista.mostrarCiclosFormativos();
        }
    },
    INSERTAR_ASIGNATURA("Insertar asignatura") {
        @Override
        public void ejecutar() {
            vista.insertarAsignatura();
        }
    },
    BUSCAR_ASIGNATURA("Buscar asignatura") {
        @Override
        public void ejecutar() {
            vista.buscarAsignatura();
        }
    },
    BORRAR_ASIGNATURA("Borrar asignatura") {
        @Override
        public void ejecutar() {
            vista.borrarAsignatura();
        }
    },
    MOSTRAR_ASIGNATURAS("Mostrar asignaturas") {
        @Override
        public void ejecutar() {
            vista.mostrarAsignaturas();
        }
    },
    INSERTAR_MATRICULA("Insertar matrícula") {
        @Override
        public void ejecutar() {
            vista.insertarMatricula();
        }
    },
    BUSCAR_MATRICULA("Buscar matrícula") {
        @Override
        public void ejecutar() {
            vista.buscarMatricula();
        }
    },
    ANULAR_MATRICULA("Anular matrícula") {
        @Override
        public void ejecutar() {
            vista.anularMatricula();
        }
    },
    MOSTRAR_MATRICULAS("Mostrar matrículas") {
        @Override
        public void ejecutar() {
            vista.mostrarMatriculas();
        }
    },
    MOSTRAR_MATRICULAS_ALUMNO("Mostrar matrículas de un alumno") {
        @Override
        public void ejecutar() {
            vista.mostrarMatriculasPorAlumno();
        }
    },
    MOSTRAR_MATRICULAS_CICLO_FORMATIVO("Mostrar matrículas de un ciclo formativo") {
        @Override
        public void ejecutar() {
            vista.mostrarMatriculasPorCicloFormativo();
        }
    },
    MOSTRAR_MATRICULAS_CURSO_ACADEMICO("Mostrar matrículas de un curso académico") {
        @Override
        public void ejecutar() {
            vista.mostrarMatriculasPorCursoAcademico();
        }
    };

    private final String cadenaAMostrar;
    private static Vista vista;

    private Opcion(String cadenaAMostrar) {
        this.cadenaAMostrar = cadenaAMostrar;
    }

    public static void setVista(Vista vista) {
        if (vista == null) {
            throw new NullPointerException("ERROR: El controlador no puede ser nulo.");
        }
        Opcion.vista = vista;
    }

    public abstract void ejecutar();

    @Override
    public String toString() {
        return String.format("%d.- %s", this.ordinal(), this.cadenaAMostrar);
    }
}
