package co.sena.sinova.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa la tabla empleado de la base de datos sinova.
 * Las validaciones se revisan antes de guardar o actualizar (HU-01 y HU-03).
 */
@Entity
@Table(name = "empleado")
@Getter
@Setter
@NoArgsConstructor
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado")
    private Integer idEmpleado;

    // Banco donde se consigna la nomina (opcional)
    @Column(name = "id_banco")
    private Short idBanco;

    @NotBlank(message = "El tipo de documento es obligatorio")
    @Pattern(regexp = "CC|CE|TI|PA", message = "El tipo de documento debe ser CC, CE, TI o PA")
    @Column(name = "tipo_documento", length = 2, nullable = false)
    private String tipoDocumento;

    @NotBlank(message = "El numero de documento es obligatorio")
    @Pattern(regexp = "\\d{6,12}", message = "El documento solo admite numeros, entre 6 y 12 digitos")
    @Column(name = "numero_documento", length = 20, nullable = false, unique = true)
    private String numeroDocumento;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(min = 2, max = 80, message = "Los nombres deben tener entre 2 y 80 caracteres")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "Los nombres solo admiten letras y espacios")
    @Column(length = 80, nullable = false)
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(min = 2, max = 80, message = "Los apellidos deben tener entre 2 y 80 caracteres")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "Los apellidos solo admiten letras y espacios")
    @Column(length = 80, nullable = false)
    private String apellidos;

    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Size(max = 150, message = "La direccion no puede superar 150 caracteres")
    @Column(length = 150)
    private String direccion;

    @Pattern(regexp = "\\d{7,10}", message = "El telefono debe tener entre 7 y 10 digitos")
    @Column(length = 15)
    private String telefono;

    @Email(message = "El correo no tiene un formato valido")
    @Size(max = 120, message = "El correo no puede superar 120 caracteres")
    @Column(length = 120)
    private String correo;

    @Pattern(regexp = "\\d{6,20}", message = "El numero de cuenta solo admite numeros (6 a 20 digitos)")
    @Column(name = "numero_cuenta", length = 25)
    private String numeroCuenta;

    // true = activo, false = inactivo
    @Column(nullable = false)
    private Boolean estado = true;
}
