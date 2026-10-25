package com.automechanic.api.entities;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.DecimalMin;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import com.automechanic.api.entities.enums.TipoItem;
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
@Table(name = "tb_item_ordem_servico")
public class ItemOrdemServico implements Serializable {
	private static final long serialVersionUID = 1L;
 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
 
	@Enumerated(EnumType.STRING)
	@NotNull
	private TipoItem tipo;
 
	@NotBlank
	private String descricao;
	@NotNull
	@Positive
	private BigDecimal quantidade;
	@NotNull
	@DecimalMin("0.00")
	private BigDecimal valorUnitario;
 
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "ordem_servico_id")
	private OrdemServico ordemServico;
 
	public ItemOrdemServico() {
	}
 
	public ItemOrdemServico(Long id, TipoItem tipo, String descricao, BigDecimal quantidade,
			BigDecimal valorUnitario, OrdemServico ordemServico) {
		this.id = id;
		this.tipo = tipo;
		this.descricao = descricao;
		this.quantidade = quantidade;
		this.valorUnitario = valorUnitario;
		this.ordemServico = ordemServico;
	}
 
	public Long getId() {
		return id;
	}
 
	public void setId(Long id) {
		this.id = id;
	}
 
	public TipoItem getTipo() {
		return tipo;
	}
 
	public void setTipo(TipoItem tipo) {
		this.tipo = tipo;
	}
 
	public String getDescricao() {
		return descricao;
	}
 
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
 
	public BigDecimal getQuantidade() {
		return quantidade;
	}
 
	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}
 
	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}
 
	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}
 
	public OrdemServico getOrdemServico() {
		return ordemServico;
	}
 
	public void setOrdemServico(OrdemServico ordemServico) {
		this.ordemServico = ordemServico;
	}
 
	public BigDecimal getSubtotal() {
		return valorUnitario.multiply(quantidade);
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
		ItemOrdemServico other = (ItemOrdemServico) obj;
		return Objects.equals(id, other.id);
	}
}
