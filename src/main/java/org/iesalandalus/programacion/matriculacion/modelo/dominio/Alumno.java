package org.iesalandalus.programacion.matriculacion.modelo.dominio;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Alumno {

    private static final String ER_TELEFONO = "[0-9]{9}";
    private static final String ER_CORREO = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String ER_DNI = "([0-9]{8})([a-zA-Z])";
    public static final String FORMATO_FECHA = "dd/MM/yyyy";
    private static final String ER_NIA = "[a-z]{4}+[0-9]{3}";
    private static final int MIN_EDAD_ALUMNADO = 16;

    private String nombre;
    private String telefono;
    private String correo;
    private String dni;
    private LocalDate fechaNacimiento;
    private String nia;

    public Alumno(String nombre, String dni, String correo, String telefono, LocalDate fechaNacimiento) {
        setNombre(nombre);
        setDni(dni);
        setTelefono(telefono);
        setCorreo(correo);
        setFechaNacimiento(fechaNacimiento);
        setNia();
    }

    public Alumno(Alumno alumno) {
        if (alumno == null) {
            throw new NullPointerException("ERROR: No es posible copiar un alumno nulo.");
        }

        this.nombre = alumno.nombre;
        this.dni = alumno.dni;
        this.correo = alumno.correo;
        this.telefono = alumno.telefono;
        this.fechaNacimiento = alumno.fechaNacimiento;
        this.nia = alumno.nia;
    }

    public String getNia() {
        return nia;
    }

    private void setNia() {
        if (dni == null || nombre == null) {
            throw new NullPointerException("ERROR: El nombre o el dni de un alumno no pueden ser nulos.");
        }

        if (dni.isBlank() || nombre.isBlank()) {
            throw new IllegalArgumentException("ERROR: El nombre o el dni de un alumno no pueden estar vacíos.");
        }
        
        String letrasNombre = this.nombre.toLowerCase().substring(0, 4);

        String digitosDni = this.dni.substring(5, 8);

        this.nia = letrasNombre.trim() + digitosDni;
    }

    private void setNia(String nia) {
        if (nia == null) {
            throw new NullPointerException("ERROR: El NIA de un alumno no puede ser nulo.");
        }

        if (!nia.matches(ER_NIA) || nia.isBlank()) {
            throw new IllegalArgumentException("ERROR: El NIA no tiene un formato válido.");
        }

        this.nia = nia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null) {
            throw new NullPointerException("ERROR: El nombre de un alumno no puede ser nulo.");
        }
        if (nombre.isBlank()) {
            throw new IllegalArgumentException("ERROR: El nombre de un alumno no puede estar vacío.");
        }
        this.nombre = formateaNombre(nombre);
    }

    private static String formateaNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return "";
        }

        nombre = nombre.trim();
        String[] palabras = nombre.split("\\s+");
        StringBuilder nombreFormateado = new StringBuilder();


        for (String palabra : palabras) {
            if (!palabra.isEmpty()) {
                nombreFormateado.append(palabra.substring(0, 1).toUpperCase())
                        .append(palabra.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        return nombreFormateado.toString().trim();
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        if (telefono == null) {
            throw new NullPointerException("ERROR: El teléfono de un alumno no puede ser nulo.");
        }
        if (!telefono.matches(ER_TELEFONO)) {
            throw new IllegalArgumentException("ERROR: El teléfono del alumno no tiene un formato válido.");
        }
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        if (correo == null) {
            throw new NullPointerException("ERROR: El correo de un alumno no puede ser nulo.");
        }
        if (!correo.matches(ER_CORREO) || correo.isBlank()) {
            throw new IllegalArgumentException("ERROR: El correo del alumno no tiene un formato válido.");
        }
        this.correo = correo.toLowerCase();
    }

    public String getDni() {
        return dni;
    }

    private void setDni(String dni) {
        if (dni == null){
            throw new NullPointerException("ERROR: El dni de un alumno no puede ser nulo.");
        }
        if (!dni.matches(ER_DNI) || dni.isBlank()) {
            throw new IllegalArgumentException("ERROR: El dni del alumno no tiene un formato válido.");
        }
        if (!comprobarLetraDni(dni)) {
            throw new IllegalArgumentException("ERROR: La letra del dni del alumno no es correcta.");
        }

        this.dni = dni.toUpperCase();
    }

    private static boolean comprobarLetraDni(String dni) {
        if (dni == null || dni.isBlank()) {
            return false;
        }

        final String LETRAS_DNI = "TRWAGMYFPDXBNJZSQVHLCKE";
        Pattern pattern = Pattern.compile(ER_DNI);
        Matcher matcher = pattern.matcher(dni.toUpperCase());

        if (!matcher.matches()) {
            return false;
        }

        String numeroStr = matcher.group(1);
        char letraDni = matcher.group(2).charAt(0);

        int numero = Integer.parseInt(numeroStr);

        char letraCalculada = LETRAS_DNI.charAt(numero % 23);

        return letraDni == letraCalculada;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    private void setFechaNacimiento(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            throw new NullPointerException("ERROR: La fecha de nacimiento de un alumno no puede ser nula.");
        }

        LocalDate fechaActual = LocalDate.now();
        Period edad = Period.between(fechaNacimiento, fechaActual);

        if (edad.getYears() < MIN_EDAD_ALUMNADO) {
            throw new IllegalArgumentException("ERROR: La edad del alumno debe ser mayor o igual a 16 años.");
        }
        this.fechaNacimiento = fechaNacimiento;
    }

    private String getIniciales() {

        StringBuilder iniciales = new StringBuilder();
        String[] palabras = nombre.split("\\s+");

        for (String palabra : palabras) {
            if (!palabra.isEmpty()) {
                iniciales.append(palabra.charAt(0));
            }
        }

        return iniciales.toString().toUpperCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alumno alumno = (Alumno) o;
        return this.dni != null && this.dni.equals(alumno.dni);
    }

    @Override
    public int hashCode() {
        return (dni != null) ? dni.hashCode() : 0;
    }

    public String imprimir() {
        return "Número de Identificación del Alumnado (NIA)=" + nia + " nombre=" + nombre + " (" + getIniciales() + "), " +
                "DNI=" + dni + ", correo=" + correo + ", teléfono=" + telefono + ", fecha nacimiento=" +
                fechaNacimiento.format(DateTimeFormatter.ofPattern(FORMATO_FECHA));
    }

    @Override
    public String toString() {
        return "Número de Identificación del Alumnado (NIA)=" + nia + " nombre=" + nombre + " (" + getIniciales() + "), " +
                "DNI=" + dni + ", correo=" + correo + ", teléfono=" + telefono + ", fecha nacimiento=" +
                fechaNacimiento.format(DateTimeFormatter.ofPattern(FORMATO_FECHA));
    }
}
