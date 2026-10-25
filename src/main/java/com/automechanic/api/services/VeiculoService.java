package com.automechanic.api.services;

import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import com.automechanic.api.entities.Cliente;
import com.automechanic.api.entities.Veiculo;
import com.automechanic.api.repositories.ClienteRepository;
import com.automechanic.api.repositories.VeiculoRepository;
import com.automechanic.api.services.exceptions.BusinessException;
import com.automechanic.api.services.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;

@Service
@Validated
@Transactional(readOnly = true)
public class VeiculoService {
    private final VeiculoRepository repository;
    private final ClienteRepository clienteRepository;

    public VeiculoService(VeiculoRepository repository, ClienteRepository clienteRepository) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
    }

    public List<Veiculo> findAll() {
        return repository.findAll();
    }

    public Veiculo findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Transactional
    public Veiculo insert(@Valid Veiculo obj) {
        if (obj.getId() != null) {
            throw new BusinessException("Nao informe ID ao criar um registro.");
        }
        obj.setCliente(resolveCliente(obj.getCliente()));
        return repository.save(obj);
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(findById(id));
    }

    @Transactional
    public Veiculo update(Long id, @Valid Veiculo obj) {
        Veiculo entity = findById(id);
        Cliente cliente = resolveCliente(obj.getCliente());
        // O PUT comum nao transfere propriedade nem altera o historico das ordens.
        if (!Objects.equals(entity.getCliente().getId(), cliente.getId())) {
            throw new BusinessException("A troca de proprietario exige uma operacao especifica.");
        }
        entity.setPlaca(obj.getPlaca());
        entity.setModelo(obj.getModelo());
        entity.setMarca(obj.getMarca());
        entity.setAno(obj.getAno());
        entity.setCor(obj.getCor());
        entity.setQuilometragem(obj.getQuilometragem());
        return repository.save(entity);
    }

    private Cliente resolveCliente(Cliente cliente) {
        if (cliente == null || cliente.getId() == null) {
            throw new BusinessException("Informe o ID do cliente.");
        }
        return clienteRepository.findById(cliente.getId())
                .orElseThrow(() -> new ResourceNotFoundException(cliente.getId()));
    }
}
