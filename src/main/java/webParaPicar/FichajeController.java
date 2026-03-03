package webParaPicar;

/*
 ESTA CLASE ES LA QUE RECIBE LAS PETICIONES DESDE EL NAVEGADOR Y DECIDE QUE HACER CON ELLAS:
 - si guardar un dato
 - validar una contraseña
 - devolver una lista de fichajes.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/*
 @RestController: Le dice a Spring que esta clase enviara respuestas directamente al navegador en formato de datos(JSON)
 @RequestMapping("/api/fichajes"): Es la direccion base. Todas las llamadas desde la web deben empezar por aqui para que esta clase las atienda
 @Autowired(Conexion automatica): Conecta el controlador con los repositories para que pueda leer y escribir en la base de datos sin tener que abrirlos manualmente cada vez
 ResponseEntity<?>: Permite enviar el mensaje de exito con el codigo de estado adecuado
 
 LOS 3 BUZONES:
 
 *@PostMapping("/validar"):
  - Recibe el dni y la clave
  - busca en la tabla de trabajadores si existen
  - Mira cual fue el ultimo fichaje 
 
 
 *@PostMapping("/confirmar"):
 - Crea un objeto nuevo de la clase Fichaje
 - Convierte la hora que viene de la web al formato que entiene java
 - Le dice al repositorio: save(f), lo que inserta fisicamente la fila en tu MySQL

 *@GetMapping("/historial/{empleadoId}"):
 - Recibe un dni a traves de la URL
 - Busca todos los registros de ese empleado en la base de datos
 - Los envia de vuelta a la web para que aparezcan en la lista de "Actividad Reciente"
 *
 *
 */

@RestController
@RequestMapping("/api/fichajes")
public class FichajeController {

    @Autowired
    private TrabajadorRepository trabajadorRepo;
    
    @Autowired
    private FichajeRepository fichajeRepo;

    // 1. Validar usuario y obtener su último estado para bloquear botones
    @PostMapping("/validar")
    public ResponseEntity<?> validar(@RequestBody Map<String, String> datos) {
    	// El optional es un contenedor que puede estar lleno (si el DNI existe) o vacío (si el DNI no existe). Evita que el programa falle si buscas un usuario inventado
        // trabajadorRepo.findById(...): L e pide a la base de datos que busque en la tabla de trabajadores al que tenga este id
    	// datos.get("empleadoId"): Extrae el valor del dni que recibimos de la web
    	Optional<Trabajador> t = trabajadorRepo.findById(datos.get("empleadoId"));
        
    	// t.isPresent(): Comprueba si la caja del trabajador esta llena (es decir, si el DNI existe en MySQL).
        // t.get().getPassword(): Abre la caja, saca el objeto trabajador y obtiene su contrasela guardada
    	// .equals(datos.get("password")): Compara la contraseña de la base de datos con la que el usuario acaba de escribir. Si son iguales, entramos al bloque con exito
    	if (t.isPresent() && t.get().getPassword().equals(datos.get("password"))) {
            // Buscamos su último fichaje en la base de datos
    		// fichajeRepo.findFirstByEmpleadoIdOrderByIdDesc(...): esto es una consulta que le dice a MySQL: "Traeme solo el ultimo registro que hizo este empleado, el mas reciente"
            Optional<Fichaje> ultimo = fichajeRepo.findFirstByEmpleadoIdOrderByIdDesc(t.get().getDni());
            // String estado = ultimo.isPresent() ? ... : "SALIDA": Si hay un fichaje previo, el estado sera lo que diga ese fichaje(Entrada o salida),
            // Si el empleado es nuevo y no tiene fichajes, por defecto decimos que su estado es salida, para que pueda pulsar "Entrada"
            String estado = ultimo.isPresent() ? ultimo.get().getTipo() : "SALIDA"; 

            // ResponseEntity.ok(...): Envia un cidigo 200(Exito) al navegador
            // Map.of("nombre", ..., "ultimoTipo", ...): Crea un pequeño paquete de datos con el nombre del empleado y su ultimo estado para que la web lo use inmediatamente
            return ResponseEntity.ok(Map.of(
                "nombre", t.get().getNombre(),
                "ultimoTipo", estado
            ));
        }
    	// ResponseEntity.status(401).body(...): Si el if fallo, envia un codigo 401(No autorizado) y el mensaje de error que configuraste
        return ResponseEntity.status(401).body("Usuario o contraseña incorrectos");
    }

    // 2. Guardar el nuevo fichaje
    @PostMapping("/confirmar")
    public ResponseEntity<String> confirmar(@RequestBody Map<String, String> datos) {
        try {
            Fichaje f = new Fichaje();
            // f.setEmpleadoId(...): Usa el setter para escribir el dni del trabajador en la ficha
            f.setEmpleadoId(datos.get("empleadoId"));
            // f.setTipo(...): Escibe en la ficha si es una entrada o una salida
            // datos.get("..."): Es el comando para extraer la informacion especifica que el usuario envio desde el formulario de la web
            f.setTipo(datos.get("tipo"));
            
            // Convertimos el texto de la web a formato fecha de Java
            // LocalDateTime.parse(...): Este comando hace la traduccion entre lo que envia la web para que lo entienda java
            LocalDateTime fechaManual = LocalDateTime.parse(datos.get("fechaHoraManual"));
            // f.setFechaHora(fechaManual);: Guarda esa fecha ya traducida en nuestra ficha
            f.setFechaHora(fechaManual);
            
            // fichajeRepo.save(f);: Esto toma nuestra ficha f y la inserta en una nueva fila en la tabla fichajes de tu base de datos MySQL
            fichajeRepo.save(f);
            // ResponseEntity.ok(...): Si el guardado fue exitoso, envia un mensaje de confirmacion a la web para que aparezca el texto verde de "¡Registro guardado!"
            return ResponseEntity.ok("¡Registro de " + datos.get("tipo") + " guardado!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al guardar: " + e.getMessage());
        }
    }

    // 3. NUEVO: Obtener la lista de fichajes para la "Actividad reciente"
    @GetMapping("/historial/{empleadoId}")
    public ResponseEntity<List<Fichaje>> obtenerHistorial(@PathVariable String empleadoId) {
        // Recupera todos los registros asociados a ese DNI
    	// List<Fichaje> lista: Array de lista
    	// fichajeRepo.findByEmpleadoId(empleadoId): Esto es una orden de busqueda, busca en la tabla MySQL todas las filas donde la columna empleado_id coincida con el dni que te estoy pasando
        // empleadoId: Es la variable que contiene el dni del trabajador que esta logueado en ese momento
    	List<Fichaje> lista = fichajeRepo.findByEmpleadoId(empleadoId);
    	
    	// return ResponseEntity.ok(lista): Una vez que java tiene la lista con todos los fichajes, los mete en el sobre de respuesta, con el codigo 200 OK y los envia de vuelta al navegador
        return ResponseEntity.ok(lista);
    }
}