package co.sena.sinova.api.controller;

import co.sena.sinova.api.entity.Empleado;
import co.sena.sinova.api.service.EmpleadoService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints del CRUD de empleados.
 * URL base: http://localhost:8080/api/empleados
 */
@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService service;

    // GET /api/empleados
    @GetMapping
    public List<Empleado> listar() {
        return service.listar();
    }

    // GET /api/empleados/{id}
    @GetMapping("/{id}")
    public Empleado buscar(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    // GET /api/empleados/documento/{documento}
    @GetMapping("/documento/{documento}")
    public Empleado buscarPorDocumento(@PathVariable String documento) {
        return service.buscarPorDocumento(documento);
    }

    // POST /api/empleados  -> 201 Created
    @PostMapping
    public ResponseEntity<Empleado> guardar(@Valid @RequestBody Empleado empleado) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(empleado));
    }

    // PUT /api/empleados/{id}
    @PutMapping("/{id}")
    public Empleado actualizar(@PathVariable Integer id, @Valid @RequestBody Empleado empleado) {
        return service.actualizar(id, empleado);
    }

    // DELETE /api/empleados/{id}
    @DeleteMapping("/{id}")
    public Map<String, String> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return Map.of("mensaje", "Empleado eliminado correctamente");
    }
}
