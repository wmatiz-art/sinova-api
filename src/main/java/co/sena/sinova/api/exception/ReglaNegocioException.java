package co.sena.sinova.api.exception;

/** Se lanza cuando no se cumple una regla del negocio, p. ej. documento repetido (responde 409). */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
