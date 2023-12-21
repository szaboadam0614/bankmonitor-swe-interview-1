package bankmonitor.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidJsonDataException extends RuntimeException {
    public InvalidJsonDataException(final String message) {
        super(message);
    }
}
