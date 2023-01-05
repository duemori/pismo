package br.com.pismo.accountapi.util;

import java.math.BigDecimal;

import br.com.pismo.accountapi.model.Account;
import br.com.pismo.accountapi.model.OperationType;
import br.com.pismo.accountapi.model.Transaction;
import br.com.pismo.accountapi.model.dto.AccountDTO;
import br.com.pismo.accountapi.model.dto.TransactionDTO;

public final class TestDummies {

	private static final long ID = 1L;
	private static final String DOCUMENT = "12345678900";
	private static final BigDecimal AMOUNT = new BigDecimal(10);

	private TestDummies() {
		throw new UnsupportedOperationException();
	}

	public static Account getAccount() {
		return Account.builder().withId(ID).withDocument(DOCUMENT).build();
	}

	public static AccountDTO getAccountDTO() {
		return AccountDTO.builder().withId(ID).withDocument(DOCUMENT).build();
	}

	public static TransactionDTO getTransactionDTO() {
		return TransactionDTO.builder().withId(ID).withAccountId(ID).withOperationTypeId(ID).withAmount(AMOUNT).build();
	}

	public static Transaction getTransaction() {
		return Transaction.builder().withId(ID).withAccountId(ID).withOperationTypeId(ID).withAmount(AMOUNT).build();
	}

	public static OperationType getOperationType() {
		var operationType = new OperationType();
		operationType.setId(ID);
		operationType.setDescription("COMPRA A VISTA");
		operationType.setMultiplier(new BigDecimal(-1));
		return operationType;
	}
}
