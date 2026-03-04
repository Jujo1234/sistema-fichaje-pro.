package webParaPicar;

/*
 ESTA CLASE ES EL BUSCADOR ESPECIALIZADO EN FICHAJES.
 - Permite buscar todos los movimientos de un solo empleado.
 - Permite encontrar el último movimiento para saber si bloquear botones.
 - Permite al jefe ver la actividad de toda la empresa.
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FichajeRepository extends JpaRepository<Fichaje, Integer> {

    // Busca todos los fichajes de un empleado usando su DNI
    // SQL equivalente: SELECT * FROM fichajes WHERE dni_empleado = ?
    List<Fichaje> findByEmpleadoId(String empleadoId);

    // Busca solo el último registro de un empleado para saber su estado actual
    // OrderByIdDesc hace que el más nuevo salga primero, y findFirst se queda con ese
    Optional<Fichaje> findFirstByEmpleadoIdOrderByIdDesc(String empleadoId);
    
    // NUEVO PARA EL JEFE: Traer absolutamente todos los fichajes de la empresa
    // El nombre de la función le dice a Spring que ordene por fecha_hora de forma descendente (el más nuevo arriba)
    List<Fichaje> findAllByOrderByFechaHoraDesc();
}