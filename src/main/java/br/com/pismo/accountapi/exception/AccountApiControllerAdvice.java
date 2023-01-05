package br.com.pismo.accountapi.exception;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AccountApiControllerAdvice {

	private static final Logger log = LoggerFactory.getLogger(AccountApiControllerAdvice.class);

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
		var errors = exception.getFieldErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.joining(","));

		log.info("Invalid method arguments: {}", errors);

		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<String> handleConflictException(ConflictException exception) {
		var error = exception.getMessage();

		log.info("Conflict: {}", error);

		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}

	@ExceptionHandler(UnprocessableException.class)
	public ResponseEntity<String> handleUnprocessableException(UnprocessableException exception) {
		var error = exception.getMessage();

		log.info("Unprocessable Entity: {}", error);

		return ResponseEntity.unprocessableEntity().body(error);
	}

}
