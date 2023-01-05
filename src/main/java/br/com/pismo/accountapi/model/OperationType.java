package br.com.pismo.accountapi.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "operations")
public class OperationType {

	@Id
	private Long id;

	private String description;

	private BigDecimal multiplier;

	public OperationType() { }

	public OperationType(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(BigDecimal multiplier) {
		this.multiplier = multiplier;
	}

	@Override
	public String toString() {
		return "OperationType [id=" + id + ", description=" + description + ", multiplier=" + multiplier + "]";
	}

}
