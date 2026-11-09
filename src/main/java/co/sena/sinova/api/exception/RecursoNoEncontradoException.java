package co.sena.sinova.api.exception;

/** Se lanza cuando se busca un empleado que no existe (responde 404). */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
