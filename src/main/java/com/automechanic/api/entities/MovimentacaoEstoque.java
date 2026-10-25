package com.automechanic.api.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import com.automechanic.api.entities.enums.TipoMovimentacaoEstoque;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_movimentacao_estoque")
public class MovimentacaoEstoque implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDateTime data;
	private BigDecimal quantidade;
	private String observacao;

	@Enumerated(EnumType.STRING)
	private TipoMovimentacaoEstoque tipo;

	@ManyToOne
	@JoinColumn(name = "peca_id")
	private Peca peca;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "ordem_servico_id")
	private OrdemServico ordemServico;

	public MovimentacaoEstoque() {
	}

	public MovimentacaoEstoque(Long id, LocalDateTime data, BigDecimal quantidade, String observacao,
			TipoMovimentacaoEstoque tipo, Peca peca, OrdemServico ordemServico) {
		this.id = id;
		this.data = data;
		this.quantidade = quantidade;
		this.observacao = observacao;
		this.tipo = tipo;
		this.peca = peca;
		this.ordemServico = ordemServico;
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public LocalDateTime getData() { return data; }
	public void setData(LocalDateTime data) { this.data = data; }

	public BigDecimal getQuantidade() { return quantidade; }
	public void setQuantidade(BigDecimal quantidade) { this.quantidade = quantidade; }

	public String getObservacao() { return observacao; }
	public void setObservacao(String observacao) { this.observacao = observacao; }

	public TipoMovimentacaoEstoque getTipo() { return tipo; }
	public void setTipo(TipoMovimentacaoEstoque tipo) { this.tipo = tipo; }

	public Peca getPeca() { return peca; }
	public void setPeca(Peca peca) { this.peca = peca; }

	public OrdemServico getOrdemServico() { return ordemServico; }
	public void setOrdemServico(OrdemServico ordemServico) { this.ordemServico = ordemServico; }

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		MovimentacaoEstoque other = (MovimentacaoEstoque) obj;
		return Objects.equals(id, other.id);
	}
}