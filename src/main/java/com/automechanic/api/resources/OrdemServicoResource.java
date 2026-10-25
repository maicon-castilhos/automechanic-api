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

import com.automechanic.api.entities.ItemOrdemServico;
import com.automechanic.api.entities.OrdemServico;
import com.automechanic.api.services.OrdemServicoService;

@RestController
@RequestMapping(value = "/ordens")
public class OrdemServicoResource {

	private final OrdemServicoService service;


	public OrdemServicoResource(OrdemServicoService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<OrdemServico>> findAll() {
		List<OrdemServico> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<OrdemServico> findById(@PathVariable Long id) {
		OrdemServico obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}

	@PostMapping
	public ResponseEntity<OrdemServico> insert(@Valid @RequestBody OrdemServico obj) {
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(obj);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<OrdemServico> update(@PathVariable Long id, @Valid @RequestBody OrdemServico obj) {
		obj = service.update(id, obj);
		return ResponseEntity.ok().body(obj);
	}

	@PostMapping(value = "/{id}/itens")
	public ResponseEntity<ItemOrdemServico> insertItem(@PathVariable Long id, @Valid @RequestBody ItemOrdemServico item) {
		item = service.insertItem(id, item);

		return ResponseEntity.status(201).body(item);
	}
	
}
