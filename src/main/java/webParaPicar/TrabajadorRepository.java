package webParaPicar;

/*
 ESTA CLASE ES EL BUSCADOR PERSONAL DE TU APLICACION. SU FUNCION ES CONECTAR TU CODIGO CON LA TABLA DE LA BASE DE DATOS DONDE ESTAN GUARDADOS LOS NOMBRES Y CONTRASEÑAS DE LOS EMPLEADOS
 - Esto controla quien puede entrar y quien no
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 @Repository: Esta etiqueta le dice a Spring que esta clase es un componente de acceso a datos que sirve para que Spring sepa que debe gestionar las conexiones a MySQL a traves de ella
 extends JpaRepository<Trabajador, String>:
 - Trabajador: Le indica que este buscador trabaja con la clase Trabajador.java
 - String: Le indica que el id de esa tabla es de tipo texto
 */

@Repository
public interface TrabajadorRepository extends JpaRepository<Trabajador, String> {
    // Este repositorio se encarga de buscar si el DNI existe en la tabla de trabajadores
}