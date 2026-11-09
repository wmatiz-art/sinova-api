// Pantalla de Empleados de SINOVA: consume la API REST /api/empleados
const API = "/api/empleados";
const campos = ["tipoDocumento", "numeroDocumento", "nombres", "apellidos", "fechaNacimiento",
    "telefono", "correo", "numeroCuenta", "direccion"];
let empleadoActual = null;   // empleado que se ve en el detalle
let idPorEliminar = null;

const $ = (id) => document.getElementById(id);

// ---------- Navegacion entre vistas ----------
function mostrar(vista) {
    ["vistaListado", "vistaDetalle", "vistaFormulario"].forEach(v => $(v).classList.add("oculto"));
    $(vista).classList.remove("oculto");
    window.scrollTo(0, 0);
}

function mensaje(id, texto, tipo) {
    const caja = $(id);
    caja.textContent = texto;
    caja.className = "mensaje " + (tipo === "error" ? "error-msg" : "ok") + (id === "mensajeForm" ? " ancho" : "");
}

function ocultarMensaje(id) {
    $(id).classList.add("oculto");
}

// Evita que el texto del usuario se interprete como HTML
function texto(valor) {
    const div = document.createElement("div");
    div.textContent = valor ?? "";
    return div.innerHTML;
}

// ---------- HU-02: consultar ----------
async function cargarEmpleados() {
    const respuesta = await fetch(API);
    pintarTabla(await respuesta.json());
}

async function buscarPorDocumento(documento) {
    const respuesta = await fetch(`${API}/documento/${encodeURIComponent(documento)}`);
    const datos = await respuesta.json();
    if (respuesta.ok) {
        pintarTabla([datos]);
        ocultarMensaje("mensaje");
    } else {
        pintarTabla([]);
        mensaje("mensaje", datos.mensaje, "error");
    }
}

function pintarTabla(lista) {
    $("tablaEmpleados").innerHTML = lista.map(e => `
        <tr>
            <td>${e.idEmpleado}</td>
            <td>${texto(e.tipoDocumento)}</td>
            <td>${texto(e.numeroDocumento)}</td>
            <td>${texto(e.nombres)} ${texto(e.apellidos)}</td>
            <td>${texto(e.telefono)}</td>
            <td>${texto(e.correo)}</td>
            <td><span class="estado">${e.estado ? "Activo" : "Inactivo"}</span></td>
            <td class="acciones">
                <button class="btn-tabla" onclick="verDetalle(${e.idEmpleado})">Ver</button>
                <button class="btn-tabla" onclick="abrirEdicion(${e.idEmpleado})">Editar</button>
                <button class="btn-tabla eliminar" onclick="pedirEliminar(${e.idEmpleado}, '${texto(e.nombres)} ${texto(e.apellidos)}')">Eliminar</button>
            </td>
        </tr>`).join("");
    $("total").textContent = `Empleados registrados: ${lista.length}`;
}

async function verDetalle(id) {
    const respuesta = await fetch(`${API}/${id}`);
    const e = await respuesta.json();
    empleadoActual = e;
    const filas = [
        ["ID", e.idEmpleado], ["Tipo de documento", e.tipoDocumento], ["Número de documento", e.numeroDocumento],
        ["Nombres", e.nombres], ["Apellidos", e.apellidos], ["Fecha de nacimiento", e.fechaNacimiento],
        ["Teléfono", e.telefono], ["Correo", e.correo], ["Número de cuenta", e.numeroCuenta],
        ["Dirección", e.direccion], ["Estado", e.estado ? "Activo" : "Inactivo"]
    ];
    $("detalle").innerHTML = filas.map(([k, v]) => `<dt>${k}</dt><dd>${texto(v) || "—"}</dd>`).join("");
    mostrar("vistaDetalle");
}

// ---------- HU-01 y HU-03: registrar y actualizar ----------
function limpiarFormulario() {
    $("formEmpleado").reset();
    $("idEmpleado").value = "";
    $("idBanco").value = "";
    $("estado").checked = true;
    limpiarErrores();
    ocultarMensaje("mensajeForm");
}

function limpiarErrores() {
    document.querySelectorAll("[data-error]").forEach(s => s.textContent = "");
    document.querySelectorAll(".invalido").forEach(c => c.classList.remove("invalido"));
}

function abrirRegistro() {
    limpiarFormulario();
    $("tituloForm").textContent = "Registrar empleado";
    mostrar("vistaFormulario");
    $("tipoDocumento").focus();
}

async function abrirEdicion(id) {
    const respuesta = await fetch(`${API}/${id}`);
    const e = await respuesta.json();
    limpiarFormulario();
    $("tituloForm").textContent = `Editar empleado #${e.idEmpleado}`;
    $("idEmpleado").value = e.idEmpleado;
    $("idBanco").value = e.idBanco ?? "";
    campos.forEach(c => $(c).value = e[c] ?? "");
    $("estado").checked = e.estado;
    mostrar("vistaFormulario");
}

function leerFormulario() {
    const datos = {};
    campos.forEach(c => {
        const valor = $(c).value.trim();
        // los campos opcionales vacios se envian como null
        datos[c] = valor === "" && !["tipoDocumento", "numeroDocumento", "nombres", "apellidos"].includes(c) ? null : valor;
    });
    datos.idBanco = $("idBanco").value ? Number($("idBanco").value) : null;
    datos.estado = $("estado").checked;
    return datos;
}

async function guardar(evento) {
    evento.preventDefault();
    limpiarErrores();
    const id = $("idEmpleado").value;
    const respuesta = await fetch(id ? `${API}/${id}` : API, {
        method: id ? "PUT" : "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(leerFormulario())
    });
    const datos = await respuesta.json();

    if (respuesta.ok) {
        mostrar("vistaListado");
        await cargarEmpleados();
        mensaje("mensaje", id ? `Empleado #${datos.idEmpleado} actualizado correctamente.`
                              : `Empleado registrado correctamente con el ID ${datos.idEmpleado}.`, "ok");
        return;
    }
    // Errores de validacion que devuelve la API: se muestran debajo de cada campo
    if (datos.errores) {
        Object.entries(datos.errores).forEach(([campo, texto]) => {
            const destino = document.querySelector(`[data-error="${campo}"]`);
            if (destino) destino.textContent = texto;
            if ($(campo)) $(campo).classList.add("invalido");
        });
    }
    mensaje("mensajeForm", datos.mensaje, "error");
}

// ---------- HU-04: eliminar ----------
function pedirEliminar(id, nombre) {
    idPorEliminar = id;
    $("textoDialogo").textContent = `¿Seguro que desea eliminar a ${nombre} (ID ${id})? Esta acción no se puede deshacer.`;
    $("dialogo").classList.remove("oculto");
}

async function confirmarEliminar() {
    $("dialogo").classList.add("oculto");
    const respuesta = await fetch(`${API}/${idPorEliminar}`, { method: "DELETE" });
    const datos = await respuesta.json();
    await cargarEmpleados();
    mensaje("mensaje", datos.mensaje, respuesta.ok ? "ok" : "error");
}

// ---------- Eventos ----------
$("btnNuevo").addEventListener("click", abrirRegistro);
$("formEmpleado").addEventListener("submit", guardar);
$("btnEditarDetalle").addEventListener("click", () => abrirEdicion(empleadoActual.idEmpleado));
$("btnCancelarEliminar").addEventListener("click", () => $("dialogo").classList.add("oculto"));
$("btnConfirmarEliminar").addEventListener("click", confirmarEliminar);
$("btnTodos").addEventListener("click", () => { $("txtBuscar").value = ""; ocultarMensaje("mensaje"); cargarEmpleados(); });
$("formBuscar").addEventListener("submit", (e) => {
    e.preventDefault();
    const doc = $("txtBuscar").value.trim();
    doc ? buscarPorDocumento(doc) : cargarEmpleados();
});
document.querySelectorAll("[data-volver]").forEach(b => b.addEventListener("click", () => {
    ocultarMensaje("mensaje");
    mostrar("vistaListado");
}));

cargarEmpleados();
