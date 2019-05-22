package machalica.marcin.gss.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlingController {
	@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Data integrity violation")
	@ExceptionHandler(DataIntegrityViolationException.class)
	public void dataIntegrityViolation() {

	}

	@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Method Arguments are not valid")
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public void notValidMethodArguments() {
		 
	}
	
	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource not found")
	@ExceptionHandler(ResourceNotFoundException.class)
	public void resourceNotFound() {
		 
	}
}
