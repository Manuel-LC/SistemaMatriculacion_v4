# Tarea: Sistema de Matriculación
## Profesor: Andrés Rubio del Río
## Alumno: Manuel López Castillo

Nuestro cliente está muy satisfecho de como está quedando la aplicación. Sin embargo, nos acaba de comentar que 
por favor añadamos persistencia a los datos introducidos, ya que tal y como está ahora la aplicación no es 
**funcional**. Por tanto, en este **quinto spring** añadiremos persistencia a los datos utilizando para ello bases de 
datos relacionales. Por tal motivo, añadiremos al modelo que teníamos de memoria, un modelo de base de datos. 
Además, el cliente ha añadido un nuevo requerimiento: quiere que la aplicación no permita borrar un alumno, 
una asignatura o un ciclo formativo si existe alguna matrícula para dicho alumno, ciclo formativo o asignatura.

En definitiva, lo que nos pide el cliente es lo siguiente:

- Que la aplicación almacene los datos en una base de datos creada para tal efecto.
- La base de datos debe estar implementada en **MySQL**.
- Acomodar el proyecto para que gradle gestione la dependencia con el driver para java de MySQL en su última versión.
- Modificar el proyecto para que se puedan ejecutar las dos versiones: memoria con vista textual y BD con vista textual.
- Gestionar los alumnos para que su persistencia se lleve a cabo por medio de dicha BD.
- Gestionar los ciclos formativos para que su persistencia se lleve a cabo por medio de dicha BD.
- Gestionar las asignaturas para que su persistencia se lleve a cabo por medio de dicha BD.
- Impedir el borrado de un alumno si éste tiene ya una matrícula.
- Impedir el borrado de una asignatura si ésta tiene ya una matrícula.
- Impedir el borrado de un ciclo formativo si éste tiene ya una matrícula.

Para todo esto, te muestro un diagrama de clases y poco a poco te iré explicando los diferentes pasos a realizar:

![](https://www.juntadeandalucia.es/educacion/gestionafp/datos/tareas/DAW/PRO_18966254/2024-25/DAW_PRO_10_2024-25_Individual__480067/SistemaMatriculacion_v4.png)


#### Primeros Pasos
1. Lo primero que debes hacer es realizar un **clone** de tu repositorio a un nuevo repositorio para que contenga la versión 4 de la aplicación.
2. Clona tu repositorio remoto recién copiado en GitHub a un repositorio local que será donde irás realizando lo que a continuación se te pide.
3. Modifica el fichero gradle para que incluya entre sus dependencias la versión del driver de mysql que se usará en el proyecto:
     ```
   // Driver MySQL
   implementation 'mysql:mysql-connector-java:8.0.28'

4. Realiza tu **primer commit**.

#### Creación de base de datos MySQL en la nube
1. Crea en AWS una nueva base de datos MySQL. La base de datos debe llamarse **dbsistemamatriculacion**.
2. Crea cinco tablas en dicha base de datos llamadas respectivamente **alumno, cicloFormativo, asignatura, matricula y asignaturasMatricula**. Para ello haz uso del script sql (sql - 1,81 KB) proporcionado.
3. Crea un nuevo usuario llamado **admin** y con password **sistemamatriculacion-2025**. Este usuario deberá tener **permisos de lectura y escritura** sobre la base de datos.

#### Paquete negocio
1. Extrae las interfaces correspondientes a las clases ``Alumnos``, ``Asignaturas``, ``CiclosFormativos`` y ``Matriculas``, 
llamándolas ``IAlumnos``, ``IAsignaturas``, ``ICiclosFormativos`` e ``IMatriculas``, respectivamente.
2. Añade los métodos ``comenzar`` y ``terminar`` a cada una de las interfaces creadas.
3. Haz que las clases ``Alumnos``, ``Asignaturas``, ``CiclosFormativos`` y ``Matriculas`` implementen la interfaz que le corresponda. Esto provocará que tengas que implementar los métodos ``comenzar`` y ``terminar`` en las clases ya existentes que implementan dicha interfaz. Los cuerpos de ambos métodos en las cuatro clases deberán llamar respectivamente a los métodos ``establecerConexion`` y ``cerrarConexion`` de la clase MySQL.
4. Crea en el paquete ``negocio`` una Interface ``IFuenteDatos`` que tendrá los métodos ``crearAlumnos``, ``crearCiclosFormativos``, ``crearAsignaturas``, ``crearMatriculas``.
5. Crea un paquete llamado ``memoria``.
6. Mueve las clases ``Alumnos``, ``Asignaturas``, ``CiclosFormativos`` y ``Matriculas`` al paquete ``memoria``.
7. Crea en el paquete ``memoria`` una nueva clase llamada ``FuenteDatosMemoria`` que implementa la interfaz ``IFuenteDatos``. 
Esta clase será la encargada de implementar el **patrón fábrica**, devolviendo en cada caso el resultado de crear 
la colección a la que hace referencia su nombre:
   1. Método ``crearAlumnos`` devolverá una nueva colección de tipo ``Alumnos`` del paquete ``memoria``.
   2. Método ``crearCiclosFormativos`` devolverá una nueva colección de tipo ``CiclosFormativos`` del paquete ``memoria``.
   3. Método ``crearAsignaturas`` devolverá una nueva colección de tipo ``Asignaturas`` del paquete ``memoria``.
   4. Método ``crearMatriculas`` devolverá una nueva colección de tipo ``Matriculas`` del paquete ``memoria``.
8. Realiza el **commit** correspondiente.

#### Paquete mysql
1. Crea el paquete ``utilidades`` que contendrá una clase ``MySQL`` que constará de las siguientes constantes y métodos:
   1. ``HOST`` cuyo valor será el **nombre de la máquina hospedada en la nube y que aloja la base de datos creada**.
   2. ``ESQUEMA`` que tendrá como valor el **nombre de la base de datos creada**.
   3. ``USUARIO`` cuyo valor será el **nombre del usuario** con el que te conectas a la base de datos.
   4. ``CONTRASENA`` cuyo valor será la **password del usuario** con el que te conectas a la base de datos.
   5. Crea el método ``establecerConexion`` que se encargará de **realizar la conexión de la aplicación a la base de datos** alojada en la nube.
   6. Crea el método ``cerrarConexion`` que se encargará de **cerrar la conexión de la aplicación con la base de datos** alojada en la nube.
2. Añade la clase ``Alumnos`` tal y como aparece en el diagrama de clases teniendo en cuenta que:
   1. Implementa el patrón **singlenton** a través del atributo ``instancia`` y del método ``getInstancia`` que si el atributo ``instancia`` es nulo devolverá una instancia de la clase ``Alumnos``, y si no es nulo devolverá el valor del atributo ``instancia``.
   2. El constructor de la clase se encargará de llamar al método ``comenzar``.
   3. El método ``comenzar`` deberá crear una conexión con la base de datos.
   4. El método ``terminar`` deberá cerrar la conexión con la base de datos.
   5. El método ``get`` deberá devolver una lista formada por todos los alumnos existentes en la base de datos ordenados por ``dni``.
   6. El método ``getTamano`` deberá devolver el número de alumnos existentes en la base de datos.
   7. El método ``insertar`` deberá insertar un nuevo alumno en la base de datos.
   8. El método ``buscar`` deberá devolver el resultado de encontrar en la base de datos al alumno pasado como parámetro.
   9. El método ``borrar`` deberá eliminar de la base de datos al alumno pasado como parámetro.
3. Añade la clase ``CiclosFormativos`` tal y como aparece en el diagrama de clases teniendo en cuenta que:
   1. Implementa el patrón **singlenton** a través del atributo ``instancia`` y del método ``getInstancia`` que si el atributo ``instancia`` es nulo devolverá una instancia de la clase ``CiclosFormativos``, y si no es nulo devolverá el valor del atributo ``instancia``.
   2. El constructor de la clase se encargará de llamar al método ``comenzar``.
   3. El método ``comenzar`` deberá crear una conexión con la base de datos.
   4. El método ``terminar`` deberá cerrar la conexión con la base de datos.
   5. El método ``getGrado`` que, dependiendo del parámetro de tipo ``String tipoGrado``, deberá devolver un objeto de tipo ``GradoD`` o ``GradoE``, que serán creados a partir del resto de parámetros pasados al método.
   6. El método ``get`` deberá devolver una lista formada por todos los ciclos formativos existentes en la base de datos ordenados por ``nombre``.
   7. El método ``getTamano`` deberá devolver el número de ciclos formativos existentes en la base de datos.
   8. El método ``insertar`` deberá insertar un nuevo ciclo formativo en la base de datos.
   9. El método ``buscar`` deberá devolver el resultado de encontrar en la base de datos el ciclo formativo pasado como parámetro.
   10. El método ``borrar`` deberá eliminar de la base de datos al ciclo formativo pasado como parámetro.
4. Añade la clase ``Asignaturas`` tal y como aparece en el diagrama de clases teniendo en cuenta que:
   1. Implementa el patrón **singlenton** a través del atributo ``instancia`` y del método ``getInstancia`` que si el atributo ``instancia`` es nulo devolverá una instancia de la clase ``Asignaturas``, y si no es nulo devolverá el valor del atributo ``instancia``.
   2. El constructor de la clase se encargará de llamar al método ``comenzar``.
   3. El método ``comenzar`` deberá crear una conexión con la base de datos.
   4. El método ``terminar`` deberá cerrar la conexión con la base de datos.
   5. El método ``getCurso`` que, dependiendo del parámetro de tipo ``String curso``, deberá devolver un objeto de tipo ``Curso``.
   6. El método ``getEspecialidadProfesorado`` que, dependiendo del parámetro de tipo ``String especialidad``, deberá devolver un objeto de tipo ``EspecialidadProfesorado``.
   7. El método ``get`` deberá devolver una lista formada por todos las asignaturas existentes en la base de datos ordenadas por ``nombre``.
   8. El método ``getTamano`` deberá devolver el número de asignaturas existentes en la base de datos.
   9. El método ``insertar`` deberá insertar una nueva asignatura en la base de datos.
   10. El método ``buscar`` deberá devolver el resultado de encontrar en la base de datos la asignatura pasada como parámetro.
   11. El método ``borrar`` deberá eliminar de la base de datos la asignatura pasada como parámetro.
5. Añade la clase ``Matriculas`` tal y como aparece en el diagrama de clases teniendo en cuenta que:
   1. Implementa el patrón **singlenton** a través del atributo ``instancia`` y del método ``getInstancia`` que si el atributo ``instancia`` es nulo devolverá una instancia de la clase ``Matriculas``, y si no es nulo devolverá el valor del atributo ``instancia``.
   2. El constructor de la clase se encargará de llamar al método ``comenzar``.
   3. El método ``comenzar`` deberá crear una conexión con la base de datos.
   4. El método ``terminar`` deberá cerrar la conexión con la base de datos.
   5. El método ``getAsignaturasMatricula`` que a partir de un identificador de una matrícula, deberá devolver una lista de todas las asignaturas pertenecientes a dicha matrícula.
   6. El método ``get`` deberá devolver una lista formada por todas las matrículas existentes en la base de datos ordenadas por fecha de matriculación en orden descendente (es decir, las matrículas más recientes primero) y en caso de que existan varias matrículas con la misma fecha de matriculación, deberá considerarse como segundo criterio de ordenación el nombre del alumno correspondiente a la matrícula.
   7. El método ``getTamano`` deberá devolver el número de matrículas existentes en la base de datos.
   8. El método ``insertarAsignaturasMatricula`` que a partir de un identificador de matrícula y una lista de asignaturas pertenecientes a la matrícula, deberá realizar las inserciones correspondientes en la tabla ``asignaturasMatricula`` de la base de datos.
   9. El método ``insertar`` deberá insertar una nueva matrícula en la base de datos.
   10. El método ``buscar`` deberá devolver el resultado de encontrar en la base de datos la matrícula pasada como parámetro.
   11. El método ``borrar`` deberá eliminar de la base de datos la matrícula pasada como parámetro.
   12. El método ``get`` de un alumno deberá devolver una lista de todas las matrículas del alumno pasado como parámetro.
   13. El método ``get`` de un ciclo formativo deberá devolver una lista de todas las matrículas del ciclo formativo pasado como parámetro.
   14. El método ``get`` de un curso académico deberá devolver una lista de todas las matrículas del curso académico pasado como parámetro.
6. Crea la clase ``FuenteDatosMySQL`` que deberá implementar la interfaz ``IFuenteDatos``, tal y como se indica en el diagrama. Esta clase será la encargada de implementar el **patrón fábrica**, devolviendo en cada caso el resultado de crear la colección a la que hace referencia su nombre:
   1. Método ``crearAlumnos`` devolverá una nueva colección de tipo Alumnos del paquete ``mysql``.
   2. Método ``crearCiclosFormativos`` devolverá una nueva colección de tipo CiclosFormativos del paquete ``mysql``.
   3. Método ``crearAsignaturas`` devolverá una nueva colección de tipo Asignaturas del paquete ``mysql``.
   4. Método ``crearMatriculas`` devolverá una nueva colección de tipo Matriculas del paquete ``mysql``.
7. Realiza el **commit** correspondiente.

#### Paquete modelo
1. Crea el enumerado ``FactoriaFuenteDatos`` tal y como se muestra en el diagrama de clases. Este enumerado implementa el **patrón método de fabricación** para las dos fuentes de datos que se van a tener: **MEMORIA y MYSQL**.
2. Añade a la clase ``Modelo``, el atributo ``fuenteDatos`` tal y como aparece en el diagrama de clases.
3. Implementa el método ``setter`` correspondiente a la fuente de datos.
4. Modifica el constructor de la clase para que reciba y **establezca la fuente de datos de la aplicación**.
5. Modifica el método ``comenzar`` para que cree las colecciones en función de la fuente de datos a utilizar.
6. Modifica el método ``terminar`` para que llame al método ``terminar`` de cada una de las colecciones.
7. Realiza el **commit** correspondiente.

#### MainApp
1. Implementa el método ``procesarArgumentosFuenteDatos`` que creará un modelo cuya fuente de datos será la que se indique a través de los parámetros de la aplicación. Si el parámetro es ``-fdmemoria``, se creará un modelo cuya fuente de datos será de tipo **MEMORIA**. En cambio, si el parámetro es ``-fdmysql``, se creará un modelo cuya fuente de datos será de tipo **MYSQL**.
2. Modifica el método ``main`` para que se cree el modelo indicado a través de los parámetros de la aplicación.
3. Realiza el **commit** correspondiente.
4. Finalmente, realiza el **push** hacia tu repositorio remoto en GitHub.

#### Se valorará:

- La indentación debe ser correcta en cada uno de los apartados.
- El nombre de las variables debe ser adecuado.
- Se debe utilizar la clase **Entrada** para realizar la entrada por teclado.
- El programa debe pasar todas las pruebas que van en el esqueleto del proyecto y toda entrada del programa será validada, para evitar que el programa termine abruptamente debido a una excepción.
- La corrección ortográfica tanto en los comentarios como en los mensajes que se muestren al usuario.
