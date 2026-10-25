package com.automechanic.api.resources;

import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.automechanic.api.entities.Veiculo;
import com.automechanic.api.services.VeiculoService;

@RestController
@RequestMapping(value = "/veiculos")
public class VeiculoResource {

	private final VeiculoService service;
	
	public VeiculoResource(VeiculoService service) {
		this.service = service;	
	}
	
	@GetMapping
	public ResponseEntity<List<Veiculo>> findAll() {
		List<Veiculo> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}


	@GetMapping(value = "/{id}")
	public ResponseEntity<Veiculo> findById(@PathVariable Long id) {
		Veiculo obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}


	@PostMapping
	public ResponseEntity<Veiculo> insert(@Valid @RequestBody Veiculo obj) {
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(obj);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build(); // Status 204 (No Content)
	}


	@PutMapping(value = "/{id}")
	public ResponseEntity<Veiculo> update(@PathVariable Long id, @Valid @RequestBody Veiculo obj) {
		obj = service.update(id, obj);
		return ResponseEntity.ok().body(obj);
	}
}
