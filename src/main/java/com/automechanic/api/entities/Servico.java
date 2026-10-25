package com.automechanic.api.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_servico")
public class Servico implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String nome;
	@NotNull
	@DecimalMin("0.00")
	private BigDecimal valorPadrao;

	public Servico() {
	}

	public Servico(Long id, String nome, BigDecimal valorPadrao) {
		this.id = id;
		this.nome = nome;
		this.valorPadrao = valorPadrao;
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public BigDecimal getValorPadrao() { return valorPadrao; }
	public void setValorPadrao(BigDecimal valorPadrao) { this.valorPadrao = valorPadrao; }

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Servico other = (Servico) obj;
		return Objects.equals(id, other.id);
	}
}
