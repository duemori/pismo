package br.com.pismo.accountapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "accounts", indexes = @Index(columnList = "document"))
public final class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String document;

	public Account() { }

	public Account(AccountBuilder builder) {
		this.id = builder.id;
		this.document = builder.document;
	}

	public static AccountBuilder builder() {
		return new AccountBuilder();
	}

	public Long getId() {
		return id;
	}

	public String getDocument() {
		return document;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", document=" + document + "]";
	}

	public static class AccountBuilder {
		private Long id;
		private String document;

		private AccountBuilder() { }

		public AccountBuilder withId(Long id) {
			this.id = id;
			return this;
		}

		public AccountBuilder withDocument(String document) {
			this.document = document;
			return this;
		}

		public Account build() {
			return new Account(this);
		}
	}
}
