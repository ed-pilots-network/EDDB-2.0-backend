package io.edpn.backend.trade.adapter.web.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
@Slf4j
public class FindSystemsByNameContainingInputValidator {

    public void validateSubString(String subString) {
        if (ObjectUtils.isEmpty(subString)) {
            throw new IllegalArgumentException("subString must not be null or empty");
        }
    }

    public void validateAmount(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be strict positive");
        }
    }
}
