package co.sena.sinova.api.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Convierte los errores en respuestas JSON claras para quien consume la API,
 * en lugar de la pagina de error por defecto.
 */
@RestControllerAdvice
public class ManejadorErrores {

    // Errores de las anotaciones de validacion de la entidad (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validacion(MethodArgumentNotValidException ex) {
        Map<String, String> campos = new LinkedHashMap<>();
        // Un mensaje por campo; si el campo viene vacio se muestra que es obligatorio
        ex.getBindingResult().getFieldErrors().forEach(e -> {
            if ("NotBlank".equals(e.getCode())) {
                campos.put(e.getField(), e.getDefaultMessage());
            } else {
                campos.putIfAbsent(e.getField(), e.getDefaultMessage());
            }
        });
        Map<String, Object> cuerpo = cuerpo(HttpStatus.BAD_REQUEST, "Datos no validos");
        cuerpo.put("errores", campos);
        return ResponseEntity.badRequest().body(cuerpo);
    }

    // JSON mal formado o fecha con formato incorrecto (400)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> jsonInvalido(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(cuerpo(HttpStatus.BAD_REQUEST,
                "El cuerpo de la peticion no es valido. Revise el JSON y que las fechas usen el formato yyyy-MM-dd"));
    }

    // Id que no es numero, p. ej. /api/empleados/abc (400)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> tipoInvalido(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest().body(cuerpo(HttpStatus.BAD_REQUEST,
                "El parametro '" + ex.getName() + "' debe ser numerico"));
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> noEncontrado(RecursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cuerpo(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<Map<String, Object>> reglaNegocio(ReglaNegocioException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(cuerpo(HttpStatus.CONFLICT, ex.getMessage()));
    }

    // Llave foranea: el empleado tiene contratos, liquidaciones o usuario asociados (409)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> integridad(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(cuerpo(HttpStatus.CONFLICT,
                "No se puede completar la operacion: el registro tiene informacion relacionada o un dato repetido"));
    }

    private Map<String, Object> cuerpo(HttpStatus estado, String mensaje) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("fecha", LocalDateTime.now().withNano(0).toString());
        cuerpo.put("estado", estado.value());
        cuerpo.put("mensaje", mensaje);
        return cuerpo;
    }
}
