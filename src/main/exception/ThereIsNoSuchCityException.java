package main.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "There is no such city")
public class ThereIsNoSuchCityException extends RuntimeException {

	private static final long serialVersionUID = 4387942087146980945L;
	
}

