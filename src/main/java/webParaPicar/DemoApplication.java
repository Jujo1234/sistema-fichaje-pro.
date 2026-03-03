package webParaPicar;



/*
 ESTA ES LA CLASE ENCARGADA DE DE DARLE VIDA A LA WEB YA QUE SIN ELLA LAS DEMAS CLASES SOLO SERIAN CLASES SUELTAS
 
  - Esta clase es como un interruptor, enciende el servidor
 
  - Escanea todas las demas clases para que funcionen juntas
 
 
 Run As -> 1 Java Application
 */


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



/*
 @SpringBootApplication: Este es el comando mas importante, esto le dice a java que esta clase no es una normal, es una app web de spring boot, configura todo automaticamente por ti
 SpringApplication.run(DemoApplication.class, args): Este comando le dice a Spring que arranque en ese momento usando la configuracion de la clase
 */



@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}