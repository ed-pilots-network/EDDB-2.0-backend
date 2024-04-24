package io.edpn.backend.trade.application.domain.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {
    private final List<String> errors;

    public ValidationException(List<String> errors) {
        super(errors.toString());
        this.errors = errors;
    }
}
