package br.com.pismo.accountapi.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class TransactionDTO {

	@JsonProperty(value = "transaction_id", access = Access.READ_ONLY)
	private Long id;

	@JsonProperty("account_id")
	private Long accountId;

	@JsonProperty("operation_type_id")
	private Long operationTypeId;

	@NotNull(message = "amount is required")
	@Positive(message = "amount must be positive")
	private BigDecimal amount;

	@JsonFormat(shape = Shape.STRING)
	@JsonProperty(value = "event_date", access = Access.READ_ONLY)
	private LocalDateTime eventDate;

	public TransactionDTO() { }

	public TransactionDTO(TransactionDTOBuilder builder) {
		this.id = builder.id;
		this.accountId = builder.accountId;
		this.operationTypeId = builder.operationTypeId;
		this.amount = builder.amount;
		this.eventDate = builder.eventDate;
	}

	public static TransactionDTOBuilder builder() {
		return new TransactionDTOBuilder();
	}

	public Long getId() {
		return id;
	}

	public Long getAccountId() {
		return accountId;
	}

	public Long getOperationTypeId() {
		return operationTypeId;
	}

	public LocalDateTime getEventDate() {
		return eventDate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public static class TransactionDTOBuilder {
		private Long id;
		private Long accountId;
		private Long operationTypeId;
		private BigDecimal amount;
		private LocalDateTime eventDate;

		private TransactionDTOBuilder() { }

		public TransactionDTOBuilder withId(Long id) {
			this.id = id;
			return this;
		}

		public TransactionDTOBuilder withAccountId(Long accountId) {
			this.accountId = accountId;
			return this;
		}

		public TransactionDTOBuilder withOperationTypeId(Long operationTypeId) {
			this.operationTypeId = operationTypeId;
			return this;
		}

		public TransactionDTOBuilder withAmount(BigDecimal amount) {
			this.amount = amount;
			return this;
		}

		public TransactionDTOBuilder withEventDate(LocalDateTime eventDate) {
			this.eventDate = eventDate;
			return this;
		}

		public TransactionDTO build() {
			return new TransactionDTO(this);
		}
	}
}
