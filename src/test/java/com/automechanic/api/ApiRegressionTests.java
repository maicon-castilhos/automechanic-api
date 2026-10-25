package com.automechanic.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import com.automechanic.api.entities.*;
import com.automechanic.api.entities.enums.*;
import com.automechanic.api.repositories.*;
import com.automechanic.api.resources.*;
import com.automechanic.api.resources.exceptions.ResourceExceptionHandler;
import com.automechanic.api.services.*;
import com.automechanic.api.services.exceptions.BusinessException;
import com.automechanic.api.services.exceptions.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Transactional
class ApiRegressionTests {
    @Autowired private PecaResource pecaResource;
    @Autowired private ServicoResource servicoResource;
    @Autowired private OrdemServicoResource ordemResource;
    @Autowired private VeiculoResource veiculoResource;
    @Autowired private ClienteResource clienteResource;
    @Autowired private ResourceExceptionHandler handler;
    @Autowired private OrdemServicoService ordemService;
    @Autowired private PecaService pecaService;
    @Autowired private VeiculoService veiculoService;
    @Autowired private ClienteService clienteService;
    @Autowired private ClienteRepository clientes;
    @Autowired private VeiculoRepository veiculos;
    @Autowired private ItemOrdemServicoRepository itens;
    @Autowired private AgendamentoRepository agendamentos;
    private MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(pecaResource, servicoResource, ordemResource,
                veiculoResource, clienteResource).setControllerAdvice(handler).build();
    }

    private OrdemServico newOrder(String numero) {
        return new OrdemServico(null, numero, LocalDateTime.now(), null, 100,
                BigDecimal.ZERO, "Revisao", null, clientes.findById(1L).orElseThrow(),
                veiculos.findById(1L).orElseThrow(), StatusOrdemServico.ABERTA, null);
    }

    @Test
    void incompleteBodiesReturn400() throws Exception {
        for (String endpoint : new String[]{"/clientes", "/veiculos", "/pecas", "/servicos", "/ordens", "/ordens/1/itens"}) {
            mvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).content("{}"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    void serviceRejectsIncompleteItemWithoutPersisting() {
        long before = itens.count();
        assertThrows(ConstraintViolationException.class,
                () -> ordemService.insertItem(1L, new ItemOrdemServico()));
        assertEquals(before, itens.count());
    }

    @Test
    void inconsistentClientAndVehicleAreRejected() {
        OrdemServico obj = newOrder("INCONSISTENT");
        obj.setCliente(clientes.findById(2L).orElseThrow());
        assertThrows(BusinessException.class, () -> ordemService.insert(obj));
    }

    @Test
    void inconsistentAppointmentIsRejected() {
        Agendamento appointment = agendamentos.save(new Agendamento(null, LocalDateTime.now(),
                "Revisao", StatusAgendamento.AGENDADO, clientes.findById(2L).orElseThrow(),
                veiculos.findById(2L).orElseThrow()));
        OrdemServico obj = newOrder("APPOINTMENT");
        obj.setAgendamento(appointment);
        assertThrows(BusinessException.class, () -> ordemService.insert(obj));
    }

    @Test
    void discountCannotExceedItems() {
        OrdemServico obj = newOrder("NEGATIVE-TOTAL");
        obj.setDesconto(BigDecimal.TEN);
        assertThrows(BusinessException.class, () -> ordemService.insert(obj));
    }

    @Test
    void addingItemCountsItOnceAndDiscountIsValidatedOnUpdate() {
        OrdemServico saved = ordemService.insert(newOrder("TOTAL"));
        ItemOrdemServico item = new ItemOrdemServico(null, TipoItem.SERVICO, "Revisao",
                new BigDecimal("2"), new BigDecimal("20"), null);
        ordemService.insertItem(saved.getId(), item);
        assertEquals(0, new BigDecimal("40").compareTo(saved.getValorTotal()));
        OrdemServico input = newOrder("TOTAL");
        input.setDesconto(new BigDecimal("41"));
        assertThrows(BusinessException.class, () -> ordemService.update(saved.getId(), input));
    }

    @Test
    void validDiscountAndItemArePersisted() {
        OrdemServico saved = ordemService.insert(newOrder("VALID-TOTAL"));
        ordemService.insertItem(saved.getId(), new ItemOrdemServico(null, TipoItem.PECA,
                "Filtro", BigDecimal.ONE, new BigDecimal("35"), null));
        OrdemServico input = newOrder("VALID-TOTAL");
        input.setDesconto(new BigDecimal("5"));
        OrdemServico result = ordemService.update(saved.getId(), input);
        itens.flush();
        assertEquals(0, new BigDecimal("30").compareTo(result.getValorTotal()));
    }

    @Test
    void negativeQuantityIsRejected() {
        assertThrows(ConstraintViolationException.class, () -> ordemService.insertItem(1L,
                new ItemOrdemServico(null, TipoItem.PECA, "Filtro",
                        new BigDecimal("-1"), BigDecimal.TEN, null)));
    }

    @Test
    void existingIdCannotBeUsedToMoveAnItem() {
        ItemOrdemServico item = new ItemOrdemServico(1L, TipoItem.PECA, "Filtro",
                BigDecimal.ONE, BigDecimal.TEN, null);
        assertThrows(BusinessException.class, () -> ordemService.insertItem(1L, item));
    }

    @Test
    void postWithExistingIdIsRejected() {
        Peca obj = new Peca(1L, "OUTRO", "Filtro", "Marca", BigDecimal.TEN);
        assertThrows(BusinessException.class, () -> pecaService.insert(obj));
    }

    @Test
    void missingUpdateAndDeleteReturn404() throws Exception {
        mvc.perform(put("/pecas/999999").contentType(MediaType.APPLICATION_JSON)
                .content("{\"codigo\":\"NEW\",\"nome\":\"Filtro\",\"precoVenda\":10}"))
                .andExpect(status().isNotFound());
        mvc.perform(delete("/servicos/999999")).andExpect(status().isNotFound());
        assertThrows(ResourceNotFoundException.class, () -> clienteService.delete(999999L));
    }

    @Test
    void ownerChangeIsExplicitlyRejected() {
        Veiculo old = veiculos.findById(1L).orElseThrow();
        Veiculo input = new Veiculo(null, old.getPlaca(), old.getModelo(), old.getMarca(),
                old.getAno(), old.getCor(), old.getQuilometragem(), clientes.findById(2L).orElseThrow());
        assertThrows(BusinessException.class, () -> veiculoService.update(1L, input));
    }

    @Test
    void conclusionCannotPrecedeOpening() {
        OrdemServico input = newOrder("INVALID-DATES");
        input.setDataConclusao(input.getDataAbertura().minusDays(1));
        assertThrows(BusinessException.class, () -> ordemService.insert(input));
    }

    @Test
    void databaseConflictDoesNotExposeSql() {
        var request = new org.springframework.mock.web.MockHttpServletRequest();
        var response = handler.database(
                new org.springframework.dao.DataIntegrityViolationException("secret SQL"), request);
        assertEquals(409, response.getStatusCode().value());
        assertFalse(response.getBody().getMessage().contains("secret SQL"));
    }
}
