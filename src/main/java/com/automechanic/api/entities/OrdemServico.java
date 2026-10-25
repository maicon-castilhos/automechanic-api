package com.automechanic.api.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.DecimalMin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.automechanic.api.entities.enums.StatusOrdemServico;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_ordem_servico")
public class OrdemServico implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	@NotBlank
	private String numero;
	@NotNull
	private LocalDateTime dataAbertura;
	private LocalDateTime dataConclusao;
	@PositiveOrZero
	private Integer quilometragem;
	@DecimalMin("0.00")
	private BigDecimal desconto;
	private String relatoCliente;
	private String diagnostico;

	@ManyToOne
	@JoinColumn(name = "cliente_id")
	@NotNull
	private Cliente cliente;

	@ManyToOne
	@JoinColumn(name = "veiculo_id")
	@NotNull
	private Veiculo veiculo;

	@Enumerated(EnumType.STRING)
	@NotNull
	private StatusOrdemServico status;

	@OneToOne
	@JoinColumn(name = "agendamento_id")
	private Agendamento agendamento;
	
	@JsonIgnore
	@OneToMany(mappedBy = "ordemServico")
	private List<ItemOrdemServico> itens = new ArrayList<>();

	public OrdemServico() {

	}

	public OrdemServico(Long id, String numero, LocalDateTime dataAbertura, LocalDateTime dataConclusao,
			Integer quilometragem, BigDecimal desconto, String relatoCliente, String diagnostico, Cliente cliente,
			Veiculo veiculo, StatusOrdemServico status, Agendamento agendamento) {
		this.id = id;
		this.numero = numero;
		this.dataAbertura = dataAbertura;
		this.dataConclusao = dataConclusao;
		this.quilometragem = quilometragem;
		this.desconto = desconto;
		this.relatoCliente = relatoCliente;
		this.diagnostico = diagnostico;
		this.cliente = cliente;
		this.veiculo = veiculo;
		this.status = status;
		this.agendamento = agendamento;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRelatoCliente() {
		return relatoCliente;
	}

	public void setRelatoCliente(String relatoCliente) {
		this.relatoCliente = relatoCliente;
	}

	public String getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}

	public StatusOrdemServico getStatus() {
		return status;
	}

	public void setStatus(StatusOrdemServico status) {
		this.status = status;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public LocalDateTime getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(LocalDateTime dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public LocalDateTime getDataConclusao() {
		return dataConclusao;
	}

	public void setDataConclusao(LocalDateTime dataConclusao) {
		this.dataConclusao = dataConclusao;
	}

	public Integer getQuilometragem() {
		return quilometragem;
	}

	public void setQuilometragem(Integer quilometragem) {
		this.quilometragem = quilometragem;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public BigDecimal getValorTotal() {
		BigDecimal somaItens = itens.stream().map(ItemOrdemServico::getSubtotal).reduce(BigDecimal.ZERO,
				BigDecimal::add);
		if (desconto != null) {
			return somaItens.subtract(desconto);
		}
		return somaItens;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public Agendamento getAgendamento() {
		return agendamento;
	}

	public void setAgendamento(Agendamento agendamento) {
		this.agendamento = agendamento;
	}

	public List<ItemOrdemServico> getItens() {
		return itens;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrdemServico other = (OrdemServico) obj;
		return Objects.equals(id, other.id);
	}

}
