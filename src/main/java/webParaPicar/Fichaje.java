package webParaPicar;

/*
 ESTA CLASE ES EL MODELO DE UN FICHAJE.
 - Representa una fila de la tabla "fichajes" en tu MySQL.
 - Guarda quién picó, si entró o salió y en qué momento exacto.
 */

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fichajes") // Conecta con tu tabla de MySQL
public class Fichaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "dni_empleado") // <--- IMPORTANTE: Coincide con tu SQL
    private String empleadoId;

    private String tipo; // Guardará "ENTRADA" o "SALIDA"

    @Column(name = "fecha_hora") // <--- IMPORTANTE: Coincide con tu SQL
    private LocalDateTime fechaHora;

    // Constructor vacío (Obligatorio para Java)
    public Fichaje() {}

    // Getters y Setters (Los "permisos" para leer y escribir datos)
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getEmpleadoId() { return empleadoId; }
    public void setEmpleadoId(String empleadoId) { this.empleadoId = empleadoId; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
}