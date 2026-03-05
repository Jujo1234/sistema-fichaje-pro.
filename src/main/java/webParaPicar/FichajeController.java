package webParaPicar;

/*
 ESTA CLASE ES EL SEMÁFORO Y CEREBRO DE TU APLICACIÓN:
 - Valida quién entra y qué permisos tiene (Rol).
 - Guarda los fichajes nuevos en la base de datos.
 - Entrega el historial personal a los empleados y el global al jefe.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/fichajes") // Dirección base para las llamadas desde la web
public class FichajeController {

    @Autowired
    private TrabajadorRepository trabajadorRepo;
    
    @Autowired
    private FichajeRepository fichajeRepo;

    // 1. BUZÓN DE VALIDACIÓN: Comprueba DNI, contraseña y devuelve el ROL
    @PostMapping("/validar")
    public ResponseEntity<?> validar(@RequestBody Map<String, String> datos) {
        // Buscamos al trabajador en la tabla por su DNI
        Optional<Trabajador> t = trabajadorRepo.findById(datos.get("empleadoId"));
        
        // Comparamos si existe y si la contraseña coincide
        if (t.isPresent() && t.get().getPassword().equals(datos.get("password"))) {
            
            // Buscamos su último movimiento para que la web sepa qué botón bloquear
            Optional<Fichaje> ultimo = fichajeRepo.findFirstByEmpleadoIdOrderByIdDesc(t.get().getDni());
            String estado = ultimo.isPresent() ? ultimo.get().getTipo() : "SALIDA"; 

            // Devolvemos un paquete con: Nombre, Rol (jefe/empleado) y su último estado
            return ResponseEntity.ok(Map.of(
                "nombre", t.get().getNombre(),
                "rol", t.get().getRol(),
                "ultimoTipo", estado
            ));
        }
        // Si fallan los datos, mandamos error 401 (No autorizado)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrectos");
    }

    // 2. BUZÓN DE CONFIRMACIÓN: Guarda el fichaje físico en MySQL
    @PostMapping("/confirmar")
    public ResponseEntity<String> confirmar(@RequestBody Map<String, String> datos) {
        try {
            Fichaje f = new Fichaje();
            f.setEmpleadoId(datos.get("empleadoId"));
            f.setTipo(datos.get("tipo"));
            
            // Traducimos la fecha que viene de la web al formato de Java
            LocalDateTime fechaManual = LocalDateTime.parse(datos.get("fechaHoraManual"));
            f.setFechaHora(fechaManual);
            
            // Ordenamos al repositorio que guarde la ficha en MySQL
            fichajeRepo.save(f);
            
            return ResponseEntity.ok("¡Registro de " + datos.get("tipo") + " guardado!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al guardar: " + e.getMessage());
        }
    }

    // 3. BUZÓN DE HISTORIAL PERSONAL: Para que el empleado vea sus últimos 5 movimientos
    @GetMapping("/historial/{empleadoId}")
    public ResponseEntity<List<Fichaje>> obtenerHistorial(@PathVariable String empleadoId) {
        List<Fichaje> lista = fichajeRepo.findByEmpleadoId(empleadoId);
        return ResponseEntity.ok(lista);
    }

    // 4. NUEVO BUZÓN PARA EL JEFE: Trae los fichajes con el nombre del empleado
    @GetMapping("/todos")
    public ResponseEntity<List<Map<String, Object>>> obtenerHistorialGlobal() {
        // Le pedimos al buscador que traiga TODOS los registros de la empresa
        List<Fichaje> listaGlobal = fichajeRepo.findAll();
        List<Map<String, Object>> respuesta = new ArrayList<>();

        for (Fichaje f : listaGlobal) {
            // Buscamos el nombre del trabajador usando su ID de fichaje
            Optional<Trabajador> t = trabajadorRepo.findById(f.getEmpleadoId());
            String nombre = t.isPresent() ? t.get().getNombre() : "Desconocido";

            // Creamos un paquete de datos que incluya el nombre para la tabla del jefe
            Map<String, Object> dato = new HashMap<>();
            dato.put("empleadoId", f.getEmpleadoId());
            dato.put("nombreEmpleado", nombre);
            dato.put("tipo", f.getTipo());
            dato.put("fechaHora", f.getFechaHora());
            respuesta.add(dato);
        }
        return ResponseEntity.ok(respuesta);
    }
}