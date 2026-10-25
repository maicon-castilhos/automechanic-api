package com.automechanic.api.config;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.automechanic.api.entities.Cliente;
import com.automechanic.api.entities.ItemOrdemServico;
import com.automechanic.api.entities.OrdemServico;
import com.automechanic.api.entities.Peca;
import com.automechanic.api.entities.Servico;
import com.automechanic.api.entities.Veiculo;
import com.automechanic.api.entities.enums.StatusOrdemServico;
import com.automechanic.api.entities.enums.TipoItem;
import com.automechanic.api.repositories.ClienteRepository;
import com.automechanic.api.repositories.ItemOrdemServicoRepository;
import com.automechanic.api.repositories.OrdemServicoRepository;
import com.automechanic.api.repositories.PecaRepository;
import com.automechanic.api.repositories.ServicoRepository;
import com.automechanic.api.repositories.VeiculoRepository;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private VeiculoRepository veiculoRepository;
    
    @Autowired
    private ServicoRepository servicoRepository;
    
    @Autowired
    private PecaRepository pecaRepository;
    
    @Autowired
    private OrdemServicoRepository ordemServicoRepository;
    
    @Autowired
    private ItemOrdemServicoRepository itemOrdemServicoRepository;

    @Override
    public void run(String... args) throws Exception {

        // 1. Criando Clientes
        Cliente c1 = new Cliente(null, "Maria Brown", "123.456.789-00", "988888888", "maria@gmail.com");
        Cliente c2 = new Cliente(null, "Alex Green", "987.654.321-00", "977777777", "alex@gmail.com");
        clienteRepository.saveAll(Arrays.asList(c1, c2));

        // 2. Criando Veículos
        Veiculo v1 = new Veiculo(null, "ABC-1234", "Civic", "Honda", 2020, "Preto", 45000, c1);
        Veiculo v2 = new Veiculo(null, "XYZ-9876", "Gol", "Volkswagen", 2018, "Branco", 80000, c2);
        veiculoRepository.saveAll(Arrays.asList(v1, v2));

        // 3. Criando Catálogo (Peças e Serviços)
        Peca p1 = new Peca(null, "P001", "Óleo Sintético 5W30", "Mobil", new BigDecimal("50.00"));
        Peca p2 = new Peca(null, "P002", "Filtro de Óleo", "Tecfil", new BigDecimal("35.00"));
        pecaRepository.saveAll(Arrays.asList(p1, p2));

        Servico s1 = new Servico(null, "Mão de obra - Troca de Óleo", new BigDecimal("100.00"));
        Servico s2 = new Servico(null, "Alinhamento e Balanceamento", new BigDecimal("150.00"));
        servicoRepository.saveAll(Arrays.asList(s1, s2));

        // 4. Criando uma Ordem de Serviço
        OrdemServico os1 = new OrdemServico(null, "OS-001/2026", LocalDateTime.now(), null, 45000, 
                new BigDecimal("10.00"), "Carro fazendo barulho", null, c1, v1, StatusOrdemServico.ABERTA, null);
        ordemServicoRepository.save(os1);

        // 5. Criando os Itens da OS (Vinculando Peças e Serviços)
        ItemOrdemServico i1 = new ItemOrdemServico(null, TipoItem.PECA, "Óleo trocado", new BigDecimal("4.00"), p1.getPrecoVenda(), os1);
        ItemOrdemServico i2 = new ItemOrdemServico(null, TipoItem.PECA, "Filtro trocado", new BigDecimal("1.00"), p2.getPrecoVenda(), os1);
        ItemOrdemServico i3 = new ItemOrdemServico(null, TipoItem.SERVICO, "Mão de obra troca de óleo", new BigDecimal("1.00"), s1.getValorPadrao(), os1);
        
        itemOrdemServicoRepository.saveAll(Arrays.asList(i1, i2, i3));
    }
}
