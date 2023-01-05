package br.com.pismo.accountapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.pismo.accountapi.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	boolean existsByDocument(String document);
}
