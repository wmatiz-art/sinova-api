package co.sena.sinova.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la API de SINOVA (modulo de empleados).
 * Al ejecutarla se levanta el servidor en http://localhost:8080
 *
 * @author Wilmar Andres Matiz Rodas
 */
@SpringBootApplication
public class SinovaApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SinovaApiApplication.class, args);
    }
}
