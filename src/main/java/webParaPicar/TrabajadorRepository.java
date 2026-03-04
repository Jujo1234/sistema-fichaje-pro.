package webParaPicar;

/*
 ESTA CLASE ES EL BUSCADOR PERSONAL DE TU APLICACIÓN. SU FUNCIÓN ES CONECTAR TU CÓDIGO CON LA TABLA DE LA BASE DE DATOS DONDE ESTÁN GUARDADOS LOS NOMBRES, CONTRASEÑAS Y EL RANGO (ROL) DE LOS EMPLEADOS
 - Esto controla quién puede entrar, quién no, y si entra como jefe o como empleado normal.
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 @Repository: Esta etiqueta le dice a Spring que esta clase es un componente de acceso a datos. Sirve para que Spring sepa que debe gestionar las conexiones a MySQL a través de ella automáticamente.
 extends JpaRepository<Trabajador, String>:
 - Trabajador: Le indica que este buscador trabaja con la clase Trabajador.java (tu tabla de empleados).
 - String: Le indica que el ID (la llave primaria) de esa tabla es de tipo texto (el DNI).
 */

@Repository
public interface TrabajadorRepository extends JpaRepository<Trabajador, String> {
    
    /*
     findByDniAndPassword: 
     - Esta función es "mágica". Spring lee el nombre y automáticamente crea una consulta SQL 
       que busca en la tabla un trabajador que coincida exactamente con el DNI y la Contraseña que le pasemos.
     - Si lo encuentra, nos devuelve al trabajador con todos sus datos (incluyendo el ROL).
     - Si no coincide nada, nos devuelve un valor vacío (null).
     */
    Trabajador findByDniAndPassword(String dni, String password);
}