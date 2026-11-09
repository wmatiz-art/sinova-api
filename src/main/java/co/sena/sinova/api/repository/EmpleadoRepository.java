package co.sena.sinova.api.repository;

import co.sena.sinova.api.entity.Empleado;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acceso a datos de la tabla empleado. JpaRepository ya trae
 * findAll, findById, save y deleteById.
 */
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

    Optional<Empleado> findByNumeroDocumento(String numeroDocumento);

    boolean existsByNumeroDocumento(String numeroDocumento);

    // Para validar duplicados al actualizar sin contar el mismo empleado
    boolean existsByNumeroDocumentoAndIdEmpleadoNot(String numeroDocumento, Integer idEmpleado);
}
