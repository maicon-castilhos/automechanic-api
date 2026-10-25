package com.automechanic.api.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_estoque_peca")
public class EstoquePeca implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private BigDecimal quantidadeAtual;
	private BigDecimal quantidadeMinima;
	private String localizacao;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "peca_id")
	private Peca peca;

	public EstoquePeca() {
	}

	public EstoquePeca(Long id, BigDecimal quantidadeAtual, BigDecimal quantidadeMinima, String localizacao, Peca peca) {
		this.id = id;
		this.quantidadeAtual = quantidadeAtual;
		this.quantidadeMinima = quantidadeMinima;
		this.localizacao = localizacao;
		this.peca = peca;
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public BigDecimal getQuantidadeAtual() { return quantidadeAtual; }
	public void setQuantidadeAtual(BigDecimal quantidadeAtual) { this.quantidadeAtual = quantidadeAtual; }

	public BigDecimal getQuantidadeMinima() { return quantidadeMinima; }
	public void setQuantidadeMinima(BigDecimal quantidadeMinima) { this.quantidadeMinima = quantidadeMinima; }

	public String getLocalizacao() { return localizacao; }
	public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

	public Peca getPeca() { return peca; }
	public void setPeca(Peca peca) { this.peca = peca; }

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		EstoquePeca other = (EstoquePeca) obj;
		return Objects.equals(id, other.id);
	}
}