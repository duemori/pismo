package br.com.pismo.accountapi.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.pismo.accountapi.util.TestDummies;

@DataJpaTest
class AccountRepositoryTest {

	@Autowired
	private AccountRepository accountRepository;

	@Test
	void testExistsByDocumentWithSuccess() {
		var account = TestDummies.getAccount();
		var responseFalse = this.accountRepository.existsByDocument(account.getDocument());

		assertFalse(responseFalse, "documento inexistente deve retornar false");

		this.accountRepository.save(account);

		var responseTrue = this.accountRepository.existsByDocument(account.getDocument());

		assertTrue(responseTrue, "documento existente deve retornar true");
	}

}
