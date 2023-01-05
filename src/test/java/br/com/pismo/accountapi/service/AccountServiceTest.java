package br.com.pismo.accountapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.pismo.accountapi.exception.ConflictException;
import br.com.pismo.accountapi.model.Account;
import br.com.pismo.accountapi.repository.AccountRepository;
import br.com.pismo.accountapi.util.TestDummies;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

	@Mock
	private AccountRepository accountRepository;

	private AccountService accountService;

	@BeforeEach
	void setup() {
		this.accountService = new AccountService(this.accountRepository);
	}

	@Test
	void testCreateWithExistentDocumentShouldThrowConflictException() {
		var account = TestDummies.getAccountDTO();

		when(this.accountRepository.existsByDocument(anyString())).thenReturn(true);

		var exception = assertThrows(ConflictException.class, () -> this.accountService.create(account), "deve lançar ConflictException quando documento já estiver registrado");

		assertEquals("Document already registered", exception.getMessage(), "mensagem deve ser de documento já registrado");

		verify(this.accountRepository, only()).existsByDocument(anyString());
	}

	@Test
	void testCreateWithInexistentDocumentShouldReturnAccountIdFromRepositoryResponse() {
		var account = TestDummies.getAccount();

		when(this.accountRepository.existsByDocument(anyString())).thenReturn(false);
		when(this.accountRepository.save(any(Account.class))).thenReturn(account);

		var response = this.accountService.create(TestDummies.getAccountDTO());

		assertEquals(account.getId(), response, "response deve ser igual ao id retornado pelo repository");

		verify(this.accountRepository, times(1)).existsByDocument(anyString());
		verify(this.accountRepository, times(1)).save(any(Account.class));
		verifyNoMoreInteractions(this.accountRepository);
	}

	@Test
	void testFindByIdShouldReturnRepositoryResponse() {
		var account = TestDummies.getAccount();

		when(this.accountRepository.findById(anyLong())).thenReturn(Optional.of(account));

		var response = this.accountService.findById(account.getId()).get();

		assertEquals(account.getId(), response.getId(), "id igual ao do account retornado pelo repository");
		assertEquals(account.getDocument(), response.getDocument(),
				"document igual ao do account retornado pelo repository");

		verify(this.accountRepository, only()).findById(anyLong());
	}

}
