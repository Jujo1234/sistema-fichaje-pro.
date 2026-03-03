package webParaPicar;

/*
 LA FUNCION DE ESTA CLASE ES SER EL PUENTE DIRECTO ENTRE TU CODIGO JAVA Y LA BASE DE DATOS MySQL
 - El Repository es el empleador que va a la base de datos y busca datos(Registro de los fichajes) y se los trae al jefe
 - Sin esta clase, el controller no sabria como sacar o meter datos en MySQL
 */

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 extends JpaRepository<Fichaje, Long>: al poner el extends y lo que sigue tu clase ya sabe hacer lo basico sin que escribas nada guardar, borrar, editar y buscar por ID.
 - Fichaje: Le dice que la tabla es la de fichajes
 - Long: Le dice que el id de esa tabla es un numero largo
 */


// En resumen: Es la herramienta que gestiona las consultas a la base de datos de forma automática y ordenada.
public interface FichajeRepository extends JpaRepository<Fichaje, Long> {
    
    // Busca el último fichaje de un empleado para saber si bloquear ENTRADA o SALIDA
	// findFirst: Traeme solo uno(El primero que encuentres)
	// ByEmpleadoId: Filtra por el dni del empleado
	// OrderByIdDesc: Ordena del mas nuevo al mas viejo(descendente)
    Optional<Fichaje> findFirstByEmpleadoIdOrderByIdDesc(String empleadoId);

    // NUEVO: Recupera todos los fichajes de un empleado para rellenar la "Actividad reciente"
    // List<Fichaje>: indica que aqui no viene un solo dato, sino una lista de muchos fichajes
    // findByEmpleadoId: Le dice a MySQL que le de todas las filas de la tabla donde el dni sea X
    List<Fichaje> findByEmpleadoId(String empleadoId);
}