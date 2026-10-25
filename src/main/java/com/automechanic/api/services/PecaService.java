package com.automechanic.api.services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import com.automechanic.api.entities.Peca;
import com.automechanic.api.repositories.PecaRepository;
import com.automechanic.api.services.exceptions.BusinessException;
import com.automechanic.api.services.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;

@Service
@Validated
@Transactional(readOnly = true)
public class PecaService {
    private final PecaRepository repository;

    public PecaService(PecaRepository repository) {
        this.repository = repository;
    }

    public List<Peca> findAll() {
        return repository.findAll();
    }

    public Peca findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Transactional
    public Peca insert(@Valid Peca obj) {
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
    public Peca update(Long id, @Valid Peca obj) {
        Peca entity = findById(id);
        entity.setCodigo(obj.getCodigo());
        entity.setNome(obj.getNome());
        entity.setFabricante(obj.getFabricante());
        entity.setPrecoVenda(obj.getPrecoVenda());
        return repository.save(entity);
    }
}
