package util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<?> handleHttpClientException(HttpClientErrorException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ExceptionModel(ex.getMessage(), "Requested currencies not found", HttpStatus.NOT_FOUND));
	}
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<?> handleMissingRequestParam(MissingServletRequestParameterException ex) {
		return  ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ExceptionModel(ex.getMessage(), "Make sure to enter all requested parameters", HttpStatus.BAD_REQUEST));
	}
	
	@ExceptionHandler(NoDataFoundException.class)
	public ResponseEntity<?> handleInvalidExchangeRate(NoDataFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ExceptionModel(ex.getMessage(),
						String.format("Please make sure to enter currencies from the list %s", ex.getCurrencies()),
						HttpStatus.NOT_FOUND));
	}
	
	@ExceptionHandler(CurrencyDoesntExistException.class)
	public ResponseEntity<?> handleInvalidCurrency(CurrencyDoesntExistException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ExceptionModel(ex.getMessage(),
						String.format("Please make sure to enter currencies from the list %s", ex.getCurrencies()),
						HttpStatus.NOT_FOUND));
	}
	
	@ExceptionHandler(InvalidQuantityException.class) 
	public ResponseEntity<?> handleInvalidQuantity(InvalidQuantityException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ExceptionModel(ex.getMessage(),
						"You can exchange up to 300 currencies", HttpStatus.BAD_REQUEST));
	}
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
	    return ResponseEntity.status(HttpStatus.FORBIDDEN)
	            .body(new ExceptionModel(
	                    ex.getMessage(),
	                    "Operation is not allowed",
	                    HttpStatus.FORBIDDEN));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGenericException(Exception ex) {
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body(new ExceptionModel(
	                    "Unexpected error occurred",
	                    "Please check request data",
	                    HttpStatus.INTERNAL_SERVER_ERROR));
	}
	
}

