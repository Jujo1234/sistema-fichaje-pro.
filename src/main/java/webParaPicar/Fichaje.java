package webParaPicar;


/*
 ESTA CLASE ES LA MAQUETA DE LOS DATOS
 - Su funcion es decirle a java y a tu base de datos que informacion vamos a guardar de cada fichaje y como debe ser la tabala 
 */


import jakarta.persistence.*;
import java.time.LocalDateTime;

/*
 @Entity: Le dice a Spring boot: "Esta clase no es solo para guardar datos en memoria, es una tabla de la base de datos"
 @Table(name = "fichajes"): Le da el nombre a la tabla en MySQL
 @Id: Indican que el campo id es la llave primaria.
  @GeneratedValue(...): Hace que el numero suba solo (1,2,3), cada vez que alguien ficha, para que nunca se repita
 */

@Entity
@Table(name = "fichajes")
public class Fichaje {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String empleadoId;
	private LocalDateTime fechaHora;
	private String tipo; // Asegúrate de que empieza con minúscula
	
	public Fichaje() {
	}

	// Solo necesitamos UN constructor de este tipo
	public Fichaje(String empleadoId, String tipo) {
		this.empleadoId = empleadoId;
		this.tipo = tipo;
		this.fechaHora = LocalDateTime.now(); // Esto hac e que se ponga la hora actual
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getEmpleadoId() { return empleadoId; }
	public void setEmpleadoId(String empleadoId) { this.empleadoId = empleadoId; }
	public LocalDateTime getFechaHora() { return fechaHora; }
	public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
	public String getTipo() { return tipo; }
	public void setTipo(String tipo) { this.tipo = tipo; }
}