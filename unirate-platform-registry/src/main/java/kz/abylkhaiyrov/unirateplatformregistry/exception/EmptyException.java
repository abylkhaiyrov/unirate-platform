package kz.abylkhaiyrov.unirateplatformregistry.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmptyException extends RuntimeException {

    public EmptyException(String message) {
        super(message);
    }

}
