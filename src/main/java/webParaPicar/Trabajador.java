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

    // Constructor vacío (obligatorio para Java)
    public Trabajador() {}

    // Constructor para crear trabajadores
    public Trabajador(String dni, String nombre, String password) {
        this.dni = dni;
        this.nombre = nombre;
        this.password = password;
    }

    // Getters y Setters (los "permisos" para leer los datos)
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}