package io.edpn.backend.trade.application.validation;


import io.edpn.backend.trade.application.domain.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class LoadByNameContainingValidator {

    private final int MIN_NAME_LENGTH;
    private final int MIN_AMOUNT;
    private final int MAX_AMOUNT;

    public Optional<ValidationException> validate(String subString, int amount) {
        List<String> errors = new ArrayList<>();

        if (subString.length() < MIN_NAME_LENGTH) {
            errors.add("subString must be at least " + MIN_NAME_LENGTH + " characters long");
        }
        if (amount > MAX_AMOUNT) {
            errors.add("Amount must not be bigger than " + MAX_AMOUNT);
        }

        if (amount < MIN_AMOUNT) {
            errors.add("Amount must not be smaller than " + MIN_AMOUNT);
        }

        if (errors.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(new ValidationException(errors));
        }
    }
}
