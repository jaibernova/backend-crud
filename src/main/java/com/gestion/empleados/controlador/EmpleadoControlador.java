package com.gestion.empleados.controlador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.empleados.excepciones.ResourceNotFoundException;
import com.gestion.empleados.modelo.Empleado;
import com.gestion.empleados.repositorio.EmpleadoRepositorio;

@RestController
@RequestMapping("/api/v1/")
@CrossOrigin(origins = "http://localhost:4200")
public class EmpleadoControlador {
	// Se utiliza el decorador autowired para poder realizar cambios usando el
	// modelo de la base de datos
	@Autowired
	private EmpleadoRepositorio repositorio;

	// Con este metodo se realiza el listado de los empleados presentes en la base
	// de datos
	@GetMapping("/empleados")
	public List<Empleado> listarTodosLosEmpleados() {
		return repositorio.findAll();
	}

	// Con este metodo se realiza el guardado en la base de datos de un nuevo em
	// mpleado
	@PostMapping("/empleados")
	public Empleado guardarEmpleado(@RequestBody Empleado empleado) {
		return repositorio.save(empleado);
	}

	// Con este metodo se realiza la consulta en la base de datos de un solo
	// empleado usando su id
	@GetMapping("/empleados/{id}")
	public ResponseEntity<Empleado> obtenerEmpleadoPorId(@PathVariable Long id) {
		Empleado empleado = repositorio.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No existe el empleado con el ID : " + id));
		return ResponseEntity.ok(empleado);
	}

	// Con este metodo se realiza la actualizacion de un emplead usando su id
	@PutMapping("/empleados/{id}")
	public ResponseEntity<Empleado> actualizarEmpleado(@PathVariable Long id, @RequestBody Empleado detallesEmpleado) {
		Empleado empleado = repositorio.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No existe el empleado con el ID : " + id));

		empleado.setNombre(detallesEmpleado.getNombre());
		empleado.setApellido(detallesEmpleado.getApellido());
		empleado.setEmail(detallesEmpleado.getEmail());

		Empleado empleadoActualizado = repositorio.save(empleado);
		return ResponseEntity.ok(empleadoActualizado);
	}

	// Con este metodo se realiza la eliminacion de un emplead usando su id
	@DeleteMapping("/empleados/{id}")
	public ResponseEntity<Map<String, Boolean>> eliminarEmpleado(@PathVariable Long id) {
		Empleado empleado = repositorio.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No existe el empleado con el ID : " + id));
		repositorio.delete(empleado);
		Map<String, Boolean> respuesta = new HashMap<>();
		respuesta.put("eliminar", Boolean.TRUE);
		return ResponseEntity.ok(respuesta);
	}
}
