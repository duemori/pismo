package br.com.pismo.accountapi.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Account account;

	@ManyToOne
	private OperationType operationType;

	private BigDecimal amount;

	private LocalDateTime eventDate;

	public Transaction() { }

	public Transaction(TransactionBuilder builder) {
		this.id = builder.id;
		this.account = builder.account;
		this.operationType = builder.operationType;
		this.amount = builder.amount;
		this.eventDate = LocalDateTime.now();
	}

	public static TransactionBuilder builder() {
		return new TransactionBuilder();
	}

	public Long getId() {
		return id;
	}

	public Account getAccount() {
		return account;
	}

	public OperationType getOperationType() {
		return operationType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public LocalDateTime getEventDate() {
		return eventDate;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", account=" + account + ", operationType=" + operationType + ", amount="
				+ amount + ", eventDate=" + eventDate + "]";
	}

	public static class TransactionBuilder {
		private Long id;
		private Account account;
		private OperationType operationType;
		private BigDecimal amount;

		private TransactionBuilder() { }

		public TransactionBuilder withId(Long id) {
			this.id = id;
			return this;
		}

		public TransactionBuilder withAccountId(Long accountId) {
			this.account = Account.builder().withId(accountId).build();
			return this;
		}

		public TransactionBuilder withOperationTypeId(Long operationTypeId) {
			this.operationType = new OperationType(operationTypeId);
			return this;
		}

		public TransactionBuilder withAmount(BigDecimal amount) {
			this.amount = amount;
			return this;
		}

		public Transaction build() {
			return new Transaction(this);
		}
	}
}
