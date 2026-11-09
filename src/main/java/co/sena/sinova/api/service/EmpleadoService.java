package co.sena.sinova.api.service;

import co.sena.sinova.api.entity.Empleado;
import co.sena.sinova.api.exception.RecursoNoEncontradoException;
import co.sena.sinova.api.exception.ReglaNegocioException;
import co.sena.sinova.api.repository.EmpleadoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Logica del modulo de empleados: aqui se aplican las reglas
 * antes de llegar a la base de datos.
 */
@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository repository;

    // HU-02: consultar todos los empleados
    public List<Empleado> listar() {
        return repository.findAll();
    }

    // HU-02: consultar un empleado por su id
    public Empleado buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un empleado con id " + id));
    }

    // HU-02: consultar por numero de documento
    public Empleado buscarPorDocumento(String documento) {
        return repository.findByNumeroDocumento(documento)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un empleado con documento " + documento));
    }

    // HU-01: registrar empleado (el documento no se puede repetir)
    @Transactional
    public Empleado guardar(Empleado empleado) {
        if (repository.existsByNumeroDocumento(empleado.getNumeroDocumento())) {
            throw new ReglaNegocioException("Ya existe un empleado con el documento " + empleado.getNumeroDocumento());
        }
        empleado.setIdEmpleado(null); // el id lo asigna MySQL
        if (empleado.getEstado() == null) {
            empleado.setEstado(true);
        }
        return repository.save(empleado);
    }

    // HU-03: actualizar los datos de un empleado existente
    @Transactional
    public Empleado actualizar(Integer id, Empleado datos) {
        Empleado actual = buscarPorId(id);
        if (repository.existsByNumeroDocumentoAndIdEmpleadoNot(datos.getNumeroDocumento(), id)) {
            throw new ReglaNegocioException("El documento " + datos.getNumeroDocumento() + " ya pertenece a otro empleado");
        }
        actual.setIdBanco(datos.getIdBanco());
        actual.setTipoDocumento(datos.getTipoDocumento());
        actual.setNumeroDocumento(datos.getNumeroDocumento());
        actual.setNombres(datos.getNombres());
        actual.setApellidos(datos.getApellidos());
        actual.setFechaNacimiento(datos.getFechaNacimiento());
        actual.setDireccion(datos.getDireccion());
        actual.setTelefono(datos.getTelefono());
        actual.setCorreo(datos.getCorreo());
        actual.setNumeroCuenta(datos.getNumeroCuenta());
        if (datos.getEstado() != null) {
            actual.setEstado(datos.getEstado());
        }
        return repository.save(actual);
    }

    // HU-04: eliminar empleado (solo si no tiene contratos ni nomina asociados)
    @Transactional
    public void eliminar(Integer id) {
        Empleado empleado = buscarPorId(id);
        repository.delete(empleado);
        repository.flush(); // fuerza el DELETE aqui para capturar el error de llave foranea
    }
}
