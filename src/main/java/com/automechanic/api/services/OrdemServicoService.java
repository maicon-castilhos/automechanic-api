package com.automechanic.api.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import com.automechanic.api.entities.Agendamento;
import com.automechanic.api.entities.Cliente;
import com.automechanic.api.entities.ItemOrdemServico;
import com.automechanic.api.entities.OrdemServico;
import com.automechanic.api.entities.Veiculo;
import com.automechanic.api.repositories.AgendamentoRepository;
import com.automechanic.api.repositories.ClienteRepository;
import com.automechanic.api.repositories.ItemOrdemServicoRepository;
import com.automechanic.api.repositories.OrdemServicoRepository;
import com.automechanic.api.repositories.VeiculoRepository;
import com.automechanic.api.services.exceptions.BusinessException;
import com.automechanic.api.services.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;

@Service
@Validated
@Transactional(readOnly = true)
public class OrdemServicoService {
    private final OrdemServicoRepository repository;
    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final ItemOrdemServicoRepository itemRepository;

    public OrdemServicoService(OrdemServicoRepository repository, ClienteRepository clienteRepository,
            VeiculoRepository veiculoRepository, AgendamentoRepository agendamentoRepository,
            ItemOrdemServicoRepository itemRepository) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
        this.veiculoRepository = veiculoRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.itemRepository = itemRepository;
    }

    public List<OrdemServico> findAll() {
        return repository.findAll();
    }

    public OrdemServico findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Transactional
    public OrdemServico insert(@Valid OrdemServico obj) {
        if (obj.getId() != null) {
            throw new BusinessException("Nao informe ID ao criar uma ordem.");
        }
        validateRelations(obj);
        validateDiscount(obj.getDesconto(), obj.getItens());
        validateDates(obj);
        return repository.save(obj);
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(findById(id));
    }

    @Transactional
    public OrdemServico update(Long id, @Valid OrdemServico obj) {
        OrdemServico entity = findById(id);
        validateRelations(obj);
        validateDiscount(obj.getDesconto(), entity.getItens());
        validateDates(obj);
        updateData(entity, obj);
        return repository.save(entity);
    }

    @Transactional
    public ItemOrdemServico insertItem(Long id, @Valid ItemOrdemServico item) {
        if (item.getId() != null) {
            throw new BusinessException("Nao informe ID ao criar um item.");
        }
        OrdemServico entity = findById(id);
        item.setOrdemServico(entity);
        entity.getItens().add(item);
        validateDiscount(entity.getDesconto(), entity.getItens());
        itemRepository.save(item);
        return item;
    }

    private void validateRelations(OrdemServico obj) {
        if (obj.getCliente() == null || obj.getCliente().getId() == null
                || obj.getVeiculo() == null || obj.getVeiculo().getId() == null) {
            throw new BusinessException("Informe os IDs do cliente e do veiculo.");
        }
        Cliente cliente = clienteRepository.findById(obj.getCliente().getId())
                .orElseThrow(() -> new ResourceNotFoundException(obj.getCliente().getId()));
        Veiculo veiculo = veiculoRepository.findById(obj.getVeiculo().getId())
                .orElseThrow(() -> new ResourceNotFoundException(obj.getVeiculo().getId()));
        if (veiculo.getCliente() == null || !Objects.equals(veiculo.getCliente().getId(), cliente.getId())) {
            throw new BusinessException("O veiculo nao pertence ao cliente informado.");
        }
        obj.setCliente(cliente);
        obj.setVeiculo(veiculo);
        if (obj.getAgendamento() != null) {
            Long agendamentoId = obj.getAgendamento().getId();
            if (agendamentoId == null) {
                throw new BusinessException("Informe o ID do agendamento.");
            }
            Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                    .orElseThrow(() -> new ResourceNotFoundException(agendamentoId));
            if (agendamento.getCliente() == null || agendamento.getVeiculo() == null
                    || !Objects.equals(agendamento.getCliente().getId(), cliente.getId())
                    || !Objects.equals(agendamento.getVeiculo().getId(), veiculo.getId())) {
                throw new BusinessException("O agendamento deve corresponder ao cliente e ao veiculo da ordem.");
            }
            obj.setAgendamento(agendamento);
        }
    }

    private void validateDiscount(BigDecimal desconto, List<ItemOrdemServico> itens) {
        BigDecimal total = itens.stream().map(ItemOrdemServico::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (desconto != null && (desconto.signum() < 0 || desconto.compareTo(total) > 0)) {
            throw new BusinessException("O desconto deve estar entre zero e o total dos itens.");
        }
    }

    private void validateDates(OrdemServico obj) {
        if (obj.getDataConclusao() != null && obj.getDataAbertura() != null
                && obj.getDataConclusao().isBefore(obj.getDataAbertura())) {
            throw new BusinessException("A conclusao nao pode ser anterior a abertura.");
        }
    }

    private void updateData(OrdemServico entity, OrdemServico obj) {
        entity.setNumero(obj.getNumero());
        entity.setDataAbertura(obj.getDataAbertura());
        entity.setDataConclusao(obj.getDataConclusao());
        entity.setRelatoCliente(obj.getRelatoCliente());
        entity.setDiagnostico(obj.getDiagnostico());
        entity.setDesconto(obj.getDesconto());
        entity.setStatus(obj.getStatus());
        entity.setCliente(obj.getCliente());
        entity.setVeiculo(obj.getVeiculo());
        entity.setAgendamento(obj.getAgendamento());
        entity.setQuilometragem(obj.getQuilometragem());
    }
}
