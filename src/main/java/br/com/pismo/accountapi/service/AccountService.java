package br.com.pismo.accountapi.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.pismo.accountapi.exception.ConflictException;
import br.com.pismo.accountapi.model.Account;
import br.com.pismo.accountapi.model.dto.AccountDTO;
import br.com.pismo.accountapi.repository.AccountRepository;

@Service
public class AccountService {

	private static final Logger log = LoggerFactory.getLogger(AccountService.class);

	private final AccountRepository accountRepository;

	public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public Long create(AccountDTO account) {
		if (this.accountRepository.existsByDocument(account.getDocument())) {
			throw new ConflictException("Document already registered");
		}

		var newAccount = this.accountRepository.save(Account.builder().withDocument(account.getDocument()).build());

		log.info("Account created: {}", newAccount);

		return newAccount.getId();
	}

	public Optional<AccountDTO> findById(Long id) {
		return this.accountRepository.findById(id).map(account -> AccountDTO.builder()
				.withId(account.getId())
				.withDocument(account.getDocument())
				.build()
		);
	}

}
