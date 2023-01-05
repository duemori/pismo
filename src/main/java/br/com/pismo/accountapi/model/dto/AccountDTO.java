package br.com.pismo.accountapi.model.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class AccountDTO {

	@JsonProperty(value = "account_id", access = Access.READ_ONLY)
	private Long id;

	@JsonProperty("document_number")
	@NotBlank(message = "document_number is required")
	private String document;

	public AccountDTO() { }

	public AccountDTO(AccountDTOBuilder builder) {
		this.id = builder.id;
		this.document = builder.document;
	}

	public static AccountDTOBuilder builder() {
		return new AccountDTOBuilder();
	}

	public Long getId() {
		return id;
	}

	public String getDocument() {
		return document;
	}

	public static class AccountDTOBuilder {
		private Long id;
		private String document;

		private AccountDTOBuilder() { }

		public AccountDTOBuilder withId(Long id) {
			this.id = id;
			return this;
		}

		public AccountDTOBuilder withDocument(String document) {
			this.document = document;
			return this;
		}

		public AccountDTO build() {
			return new AccountDTO(this);
		}
	}
}
