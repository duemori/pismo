package br.com.pismo.accountapi.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.pismo.accountapi.exception.UnprocessableException;
import br.com.pismo.accountapi.model.Transaction;
import br.com.pismo.accountapi.model.dto.TransactionDTO;
import br.com.pismo.accountapi.repository.AccountRepository;
import br.com.pismo.accountapi.repository.OperationTypeRepository;
import br.com.pismo.accountapi.repository.TransactionRepository;

@Service
public class TransactionService {

	private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

	private final AccountRepository accountRepository;
	private final OperationTypeRepository operationTypeRepository;
	private final TransactionRepository transactionRepository;

	public TransactionService(AccountRepository accountRepository, OperationTypeRepository operationTypeRepository, TransactionRepository transactionRepository) {
		this.accountRepository = accountRepository;
		this.operationTypeRepository = operationTypeRepository;
		this.transactionRepository = transactionRepository;
	}

	public Long create(TransactionDTO transaction) {
		if (!this.accountRepository.existsById(transaction.getAccountId())) {
			throw new UnprocessableException("Invalid account");
		}

		var operationType = this.operationTypeRepository.findById(transaction.getOperationTypeId());

		if (operationType.isEmpty()) {
			throw new UnprocessableException("Invalid operation type");
		}

		var amount = transaction.getAmount().multiply(operationType.get().getMultiplier());
		var newTransaction = this.transactionRepository.save(Transaction.builder()
				.withAccountId(transaction.getAccountId())
				.withOperationTypeId(transaction.getOperationTypeId())
				.withAmount(amount)
				.build()
		);

		log.info("Transaction created: {}", newTransaction);

		return newTransaction.getId();
	}

	public Optional<TransactionDTO> findById(Long id) {
		return this.transactionRepository.findById(id).map(transaction -> TransactionDTO.builder()
				.withId(transaction.getId())
				.withAccountId(transaction.getAccount().getId())
				.withOperationTypeId(transaction.getOperationType().getId())
				.withAmount(transaction.getAmount())
				.withEventDate(transaction.getEventDate())
				.build()
		);
	}

}
