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

import br.com.pismo.accountapi.model.dto.AccountDTO;
import br.com.pismo.accountapi.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("accounts")
@Api(tags = "Accounts API")
public class AccountController {

	private final AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	@ApiOperation(value = "Creates an account")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Account created"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 409, message = "Document already registered")
	})
	public ResponseEntity<Void> create(@Valid @RequestBody @ApiParam(value = "Account information") AccountDTO account) {
		var id = this.accountService.create(account);
		var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();

		return ResponseEntity.created(uri).build();
	}

	@GetMapping("{id}")
	@ApiOperation(value = "Get account information by id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return account information"),
			@ApiResponse(code = 404, message = "Account not found")
	})
	public ResponseEntity<AccountDTO> getById(@PathVariable @ApiParam(value = "Account id", example = "1") Long id) {
		return this.accountService.findById(id).map(account -> ResponseEntity.ok(account)).orElse(ResponseEntity.notFound().build());
	}

}
