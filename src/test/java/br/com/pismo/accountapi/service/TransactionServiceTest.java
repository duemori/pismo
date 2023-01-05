package br.com.pismo.accountapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.pismo.accountapi.exception.UnprocessableException;
import br.com.pismo.accountapi.model.Transaction;
import br.com.pismo.accountapi.repository.AccountRepository;
import br.com.pismo.accountapi.repository.OperationTypeRepository;
import br.com.pismo.accountapi.repository.TransactionRepository;
import br.com.pismo.accountapi.util.TestDummies;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private OperationTypeRepository operationTypeRepository;

	@Mock
	private TransactionRepository transactionRepository;

	private TransactionService transactionService;

	@BeforeEach
	void setup() {
		this.transactionService = new TransactionService(this.accountRepository, this.operationTypeRepository, this.transactionRepository);
	}

	@Test
	void testCreateWithInvalidAccountIdShouldThrowUnprocessableException() {
		var transaction = TestDummies.getTransactionDTO();

		when(this.accountRepository.existsById(anyLong())).thenReturn(false);

		var exception = assertThrows(UnprocessableException.class, () -> this.transactionService.create(transaction), "deve lançar UnprocessableException quando conta não existir");

		assertEquals("Invalid account", exception.getMessage(), "mensagem deve ser de conta inválida");

		verify(this.accountRepository, only()).existsById(anyLong());
	}

	@Test
	void testCreateWithValidAccountAndInvalidOperationTypeIdShouldThrowUnprocessableException() {
		var transaction = TestDummies.getTransactionDTO();

		when(this.accountRepository.existsById(anyLong())).thenReturn(true);
		when(this.operationTypeRepository.findById(anyLong())).thenReturn(Optional.empty());

		var exception = assertThrows(UnprocessableException.class, () -> this.transactionService.create(transaction), "deve lançar UnprocessableException quando conta não existir");

		assertEquals("Invalid operation type", exception.getMessage(), "mensagem deve ser de tipo de operação inválida");

		verify(this.accountRepository, only()).existsById(anyLong());
		verify(this.operationTypeRepository, only()).findById(anyLong());
	}

	@Test
	void testCreateWithValidTransactionShouldReturnTransactionIdFromRepositoryResponse() {
		var transaction = TestDummies.getTransaction();

		when(this.accountRepository.existsById(anyLong())).thenReturn(true);
		when(this.operationTypeRepository.findById(anyLong())).thenReturn(Optional.of(TestDummies.getOperationType()));
		when(this.transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

		var response = this.transactionService.create(TestDummies.getTransactionDTO());

		assertEquals(transaction.getId(), response, "response deve ser igual ao id retornado pelo repository");

		verify(this.accountRepository, only()).existsById(anyLong());
		verify(this.operationTypeRepository, only()).findById(anyLong());
		verify(this.transactionRepository, only()).save(any(Transaction.class));
	}

	@Test
	void testFindByIdShouldReturnRepositoryResponse() {
		var transaction = TestDummies.getTransaction();

		when(this.transactionRepository.findById(anyLong())).thenReturn(Optional.of(transaction));

		var response = this.transactionService.findById(transaction.getId()).get();

		assertEquals(transaction.getId(), response.getId(), "id igual ao do transaction retornado pelo repository");
		assertEquals(transaction.getAccount().getId(), response.getAccountId(), "accountId igual ao do transaction retornado pelo repository");
		assertEquals(transaction.getOperationType().getId(), response.getOperationTypeId(), "operationTypeId igual ao do transaction retornado pelo repository");
		assertEquals(transaction.getAmount(), response.getAmount(), "amount igual ao do transaction retornado pelo repository");
		assertEquals(transaction.getEventDate(), response.getEventDate(), "eventDate igual ao do transaction retornado pelo repository");

		verify(this.transactionRepository, only()).findById(anyLong());
	}

}
