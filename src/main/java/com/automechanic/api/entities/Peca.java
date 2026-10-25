package com.automechanic.api.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_peca")
public class Peca implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	@NotBlank
	private String codigo;
	@NotBlank
	private String nome;
	private String fabricante;
	@NotNull
	@DecimalMin("0.00")
	private BigDecimal precoVenda;

	public Peca() {
	}

	public Peca(Long id, String codigo, String nome, String fabricante, BigDecimal precoVenda) {
		this.id = id;
		this.codigo = codigo;
		this.nome = nome;
		this.fabricante = fabricante;
		this.precoVenda = precoVenda;
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getCodigo() { return codigo; }
	public void setCodigo(String codigo) { this.codigo = codigo; }

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public String getFabricante() { return fabricante; }
	public void setFabricante(String fabricante) { this.fabricante = fabricante; }

	public BigDecimal getPrecoVenda() { return precoVenda; }
	public void setPrecoVenda(BigDecimal precoVenda) { this.precoVenda = precoVenda; }

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Peca other = (Peca) obj;
		return Objects.equals(id, other.id);
	}
}
