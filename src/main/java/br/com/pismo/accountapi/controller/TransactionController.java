package br.com.pismo.accountapi.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.pismo.accountapi.model.dto.TransactionDTO;
import br.com.pismo.accountapi.service.TransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("transactions")
@Api(tags = "Transactions API")
public class TransactionController {

	private final TransactionService transactionService;

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	@ApiOperation(value = "Creates a transaction")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Transaction created"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 422, message = "Invalid account or operation type")
	})
	public ResponseEntity<Void> create(@Valid @RequestBody @ApiParam(value = "Transaction information") TransactionDTO transaction) {
		var id = this.transactionService.create(transaction);
		var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();

		return ResponseEntity.created(uri).build();
	}

	@GetMapping("{id}")
	@ApiOperation(value = "Get transaction information by id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return transaction information"),
			@ApiResponse(code = 404, message = "Transaction not found")
	})
	public ResponseEntity<TransactionDTO> getById(@PathVariable @ApiParam(value = "Transaction id", example = "1") Long id) {
		return this.transactionService.findById(id).map(transaction -> ResponseEntity.ok(transaction)).orElse(ResponseEntity.notFound().build());
	}

}
