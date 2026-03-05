package webParaPicar;

/*
 ESTA CLASE ES EL SEMÁFORO Y CEREBRO DE TU APLICACIÓN:
 - Valida quién entra y qué permisos tiene (Rol).
 - Guarda los fichajes nuevos en la base de datos.
 - Entrega el historial personal a los empleados y el global al jefe.
 - Permite al jefe gestionar el personal (Alta/Baja) en una sección dedicada.
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
@RequestMapping("/api/fichajes")
public class FichajeController {

    @Autowired
    private TrabajadorRepository trabajadorRepo;
    
    @Autowired
    private FichajeRepository fichajeRepo;

    // 1. BUZÓN DE VALIDACIÓN: Comprueba DNI, contraseña y devuelve el ROL
    @PostMapping("/validar")
    public ResponseEntity<?> validar(@RequestBody Map<String, String> datos) {
        Optional<Trabajador> t = trabajadorRepo.findById(datos.get("empleadoId"));
        
        if (t.isPresent() && t.get().getPassword().equals(datos.get("password"))) {
            Optional<Fichaje> ultimo = fichajeRepo.findFirstByEmpleadoIdOrderByIdDesc(t.get().getDni());
            String estado = ultimo.isPresent() ? ultimo.get().getTipo() : "SALIDA"; 

            return ResponseEntity.ok(Map.of(
                "nombre", t.get().getNombre(),
                "rol", t.get().getRol(),
                "ultimoTipo", estado
            ));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrectos");
    }

    // 2. BUZÓN DE CONFIRMACIÓN: Guarda el fichaje físico en MySQL
    @PostMapping("/confirmar")
    public ResponseEntity<String> confirmar(@RequestBody Map<String, String> datos) {
        try {
            Fichaje f = new Fichaje();
            f.setEmpleadoId(datos.get("empleadoId"));
            f.setTipo(datos.get("tipo"));
            f.setFechaHora(LocalDateTime.parse(datos.get("fechaHoraManual")));
            fichajeRepo.save(f);
            return ResponseEntity.ok("Fichaje guardado");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    // 3. BUZÓN DE HISTORIAL PERSONAL
    @GetMapping("/historial/{empleadoId}")
    public ResponseEntity<List<Fichaje>> obtenerHistorial(@PathVariable String empleadoId) {
        return ResponseEntity.ok(fichajeRepo.findByEmpleadoId(empleadoId));
    }

    // 4. BUZÓN GLOBAL (JEFE): Trae los fichajes con el nombre del empleado
    @GetMapping("/todos")
    public ResponseEntity<List<Map<String, Object>>> obtenerHistorialGlobal() {
        List<Fichaje> listaGlobal = fichajeRepo.findAll();
        List<Map<String, Object>> respuesta = new ArrayList<>();
        for (Fichaje f : listaGlobal) {
            Optional<Trabajador> t = trabajadorRepo.findById(f.getEmpleadoId());
            Map<String, Object> dato = new HashMap<>();
            dato.put("empleadoId", f.getEmpleadoId());
            dato.put("nombreEmpleado", t.isPresent() ? t.get().getNombre() : "Empleado Eliminado");
            dato.put("tipo", f.getTipo());
            dato.put("fechaHora", f.getFechaHora());
            respuesta.add(dato);
        }
        return ResponseEntity.ok(respuesta);
    }

    // 5. REGISTRAR TRABAJADOR: Con validación de DNI duplicado
    @PostMapping("/admin/registrar-trabajador")
    public ResponseEntity<String> registrar(@RequestBody Trabajador nuevo) {
        // Verificamos si ya existe el DNI
        if (trabajadorRepo.existsById(nuevo.getDni())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ese DNI ya está registrado");
        }
        trabajadorRepo.save(nuevo);
        return ResponseEntity.ok("Empleado añadido");
    }

    // 6. ELIMINAR TRABAJADOR
    @DeleteMapping("/admin/eliminar-trabajador/{id}")
    public ResponseEntity<String> eliminar(@PathVariable String id) {
        try {
            trabajadorRepo.deleteById(id);
            return ResponseEntity.ok("Empleado eliminado");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    // 7. LISTADO DE PERSONAL
    @GetMapping("/admin/lista-trabajadores")
    public ResponseEntity<List<Trabajador>> listarTodos() {
        return ResponseEntity.ok(trabajadorRepo.findAll());
    }
}