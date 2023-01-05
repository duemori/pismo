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

import br.com.pismo.accountapi.exception.ConflictException;
import br.com.pismo.accountapi.model.dto.AccountDTO;
import br.com.pismo.accountapi.service.AccountService;
import br.com.pismo.accountapi.util.TestDummies;

@WebMvcTest(controllers = AccountController.class)
class AccountControllerTest {

	@MockBean
	private AccountService accountService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testCreateWithoutRequiredFieldsShouldReturnStatusBadRequest() throws Exception {
		this.mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content("{}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("document_number is required"));

		verifyNoInteractions(this.accountService);
	}

	@Test
	void testCreateWithDocumentAlreadyRegisteredShouldReturnStatusConflict() throws Exception {
		var errorMessage = "Document already registered";
		var body = new ObjectMapper().writeValueAsString(TestDummies.getAccountDTO());

		when(this.accountService.create(any(AccountDTO.class))).thenThrow(new ConflictException(errorMessage));

		this.mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isConflict())
				.andExpect(content().string(errorMessage));

		verify(this.accountService, only()).create(any(AccountDTO.class));
	}

	@Test
	void testCreateWithNewDocumentShouldReturnStatusCreated() throws Exception {
		var account = TestDummies.getAccountDTO();
		var body = new ObjectMapper().writeValueAsString(account);

		when(this.accountService.create(any(AccountDTO.class))).thenReturn(account.getId());

		this.mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isCreated())
				.andExpect(content().string(""))
				.andExpect(header().string("location", "http://localhost/accounts/" + account.getId()));

		verify(this.accountService, only()).create(any(AccountDTO.class));
	}

	@Test
	void testGetByIdWithNonExistentIdShouldReturnStatusNotFound() throws Exception {
		when(this.accountService.findById(anyLong())).thenReturn(Optional.empty());

		this.mockMvc.perform(get("/accounts/{id}", 9L))
				.andExpect(status().isNotFound())
				.andExpect(content().string(""));

		verify(this.accountService, only()).findById(anyLong());
	}

	@Test
	void testGetByIdWithExistentIdShouldReturnStatusOk() throws Exception {
		var account = TestDummies.getAccountDTO();

		when(this.accountService.findById(anyLong())).thenReturn(Optional.of(account));

		this.mockMvc.perform(get("/accounts/{id}", account.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.account_id").value(account.getId()))
				.andExpect(jsonPath("$.document_number").value(account.getDocument()));

		verify(this.accountService, only()).findById(anyLong());
	}

}
