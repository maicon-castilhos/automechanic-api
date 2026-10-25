package com.automechanic.api.services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import com.automechanic.api.entities.Servico;
import com.automechanic.api.repositories.ServicoRepository;
import com.automechanic.api.services.exceptions.BusinessException;
import com.automechanic.api.services.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;

@Service
@Validated
@Transactional(readOnly = true)
public class ServicoService {
    private final ServicoRepository repository;

    public ServicoService(ServicoRepository repository) {
        this.repository = repository;
    }

    public List<Servico> findAll() {
        return repository.findAll();
    }

    public Servico findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Transactional
    public Servico insert(@Valid Servico obj) {
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
    public Servico update(Long id, @Valid Servico obj) {
        Servico entity = findById(id);
        entity.setNome(obj.getNome());
        entity.setValorPadrao(obj.getValorPadrao());
        return repository.save(entity);
    }
}
