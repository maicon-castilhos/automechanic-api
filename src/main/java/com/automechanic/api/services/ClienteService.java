package com.automechanic.api.services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import com.automechanic.api.entities.Cliente;
import com.automechanic.api.repositories.ClienteRepository;
import com.automechanic.api.services.exceptions.BusinessException;
import com.automechanic.api.services.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;

@Service
@Validated
@Transactional(readOnly = true)
public class ClienteService {
    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public List<Cliente> findAll() {
        return repository.findAll();
    }

    public Cliente findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Transactional
    public Cliente insert(@Valid Cliente obj) {
        if (obj.getId() != null) {
            throw new BusinessException("Nao informe ID ao criar um registro.");
        }
        return repository.save(obj);
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(findById(id));
    }

    @Transactional
    public Cliente update(Long id, @Valid Cliente obj) {
        Cliente entity = findById(id);
        entity.setNome(obj.getNome());
        entity.setEmail(obj.getEmail());
        entity.setTelefone(obj.getTelefone());
        entity.setCpfCnpj(obj.getCpfCnpj());
        return repository.save(entity);
    }
}
