package webParaPicar;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "trabajadores") // Esto conecta con tu tabla de MySQL
public class Trabajador {
    
    @Id
    private String dni;
    private String nombre;
    private String password;
    private String rol; // Nuevo campo para diferenciar jefe de empleado

    // Constructor vacío (obligatorio para Java)
    public Trabajador() {}

    // Constructor para crear trabajadores
    public Trabajador(String dni, String nombre, String password, String rol) {
        this.dni = dni;
        this.nombre = nombre;
        this.password = password;
        this.rol = rol;
    }

    // Getters y Setters (los "permisos" para leer los datos)
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; } // Permiso para leer el rol
    public void setRol(String rol) { this.rol = rol; } // Permiso para cambiar el rol
}