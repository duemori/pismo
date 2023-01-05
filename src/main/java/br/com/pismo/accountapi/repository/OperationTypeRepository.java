package br.com.pismo.accountapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.pismo.accountapi.model.OperationType;

@Repository
public interface OperationTypeRepository extends JpaRepository<OperationType, Long> {
}
