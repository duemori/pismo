package br.com.pismo.accountapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pismo.accountapi.exception.UnprocessableException;
import br.com.pismo.accountapi.model.dto.TransactionDTO;
import br.com.pismo.accountapi.service.TransactionService;
import br.com.pismo.accountapi.util.TestDummies;

@WebMvcTest(controllers = TransactionController.class)
class TransactionControllerTest {

	@MockBean
	private TransactionService transactionService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testCreateWithoutRequiredFieldsShouldReturnStatusBadRequest() throws Exception {
		this.mockMvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON).content("{}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("amount is required"));

		verifyNoInteractions(this.transactionService);
	}

	@Test
	void testCreateWithInvalidAccountOrOperationTypeShouldReturnStatusUnprocessableEntity() throws Exception {
		var errorMessage = "Invalid account or operation type";
		var body = new ObjectMapper().writeValueAsString(TestDummies.getTransactionDTO());

		when(this.transactionService.create(any(TransactionDTO.class))).thenThrow(new UnprocessableException(errorMessage));

		this.mockMvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(content().string(errorMessage));

		verify(this.transactionService, only()).create(any(TransactionDTO.class));
	}

	@Test
	void testCreateWithValidBodyShouldReturnStatusCreated() throws Exception {
		var transaction = TestDummies.getTransactionDTO();
		var body = new ObjectMapper().writeValueAsString(transaction);

		when(this.transactionService.create(any(TransactionDTO.class))).thenReturn(transaction.getId());

		this.mockMvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isCreated())
				.andExpect(content().string(""))
				.andExpect(header().string("location", "http://localhost/transactions/" + transaction.getId()));

		verify(this.transactionService, only()).create(any(TransactionDTO.class));
	}

	@Test
	void testGetByIdWithNonExistentIdShouldReturnStatusNotFound() throws Exception {
		when(this.transactionService.findById(anyLong())).thenReturn(Optional.empty());

		this.mockMvc.perform(get("/transactions/{id}", 9L))
				.andExpect(status().isNotFound())
				.andExpect(content().string(""));

		verify(this.transactionService, only()).findById(anyLong());
	}

	@Test
	void testGetByIdWithExistentIdShouldReturnStatusOk() throws Exception {
		var transaction = TestDummies.getTransactionDTO();

		when(this.transactionService.findById(anyLong())).thenReturn(Optional.of(transaction));

		this.mockMvc.perform(get("/transactions/{id}", transaction.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.transaction_id").value(transaction.getId()))
				.andExpect(jsonPath("$.account_id").value(transaction.getAccountId()))
				.andExpect(jsonPath("$.operation_type_id").value(transaction.getOperationTypeId()))
				.andExpect(jsonPath("$.amount").value(transaction.getAmount()))
				.andExpect(jsonPath("$.event_date").value(transaction.getEventDate()));

		verify(this.transactionService, only()).findById(anyLong());
	}

}
