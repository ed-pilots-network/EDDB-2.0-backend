package io.edpn.backend.trade.application.valdation;

import io.edpn.backend.trade.application.domain.exception.ValidationException;
import io.edpn.backend.trade.application.validation.LoadByNameContainingValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LoadByNameContainingValidatorTest {

    private LoadByNameContainingValidator validator;

    @BeforeEach
    public void setup() {
        // Consider these values are assigned here for testing, please replace with actual values used in your implementation
        int MIN_NAME_LENGTH = 3;
        int MIN_AMOUNT = 1;
        int MAX_AMOUNT = 5;
        validator = new LoadByNameContainingValidator(MIN_NAME_LENGTH, MIN_AMOUNT, MAX_AMOUNT);
    }

    @Test
    public void testValidate_ShouldReturnEmptyOptionalWhenValid() {
        String subString = "test";
        int amount = 3;

        Optional<ValidationException> result = validator.validate(subString, amount);

        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void testValidate_ShouldReturnValidationExceptionWhenSubStringLengthIsInvalid() {
        String subString = "te";
        int amount = 3;

        Optional<ValidationException> result = validator.validate(subString, amount);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get().getErrors().get(0), is("subString must be at least 3 characters long"));
    }

    @Test
    public void testValidate_ShouldReturnValidationExceptionWhenAmountIsGreaterThanMaxAmount() {
        String subString = "test";
        int amount = 6;

        Optional<ValidationException> result = validator.validate(subString, amount);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get().getErrors().get(0), is("Amount must not be bigger than 5"));
    }

    @Test
    public void testValidate_ShouldReturnValidationExceptionWhenAmountIsSmallerThanMinAmount() {
        String subString = "test";
        int amount = 0;

        Optional<ValidationException> result = validator.validate(subString, amount);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get().getErrors().get(0), is("Amount must not be smaller than 1"));
    }
}
