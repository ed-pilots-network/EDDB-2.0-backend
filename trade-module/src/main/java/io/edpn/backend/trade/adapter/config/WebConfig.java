package io.edpn.backend.trade.adapter.config;

import io.edpn.backend.trade.adapter.web.validator.FindSystemsByNameContainingInputValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("tradeWebConfig")
public class WebConfig {
    
    @Bean(name = "tradeFindSystemsByNameContainingInputValidator")
    public FindSystemsByNameContainingInputValidator findSystemsByNameContainingInputValidator() {
        return new FindSystemsByNameContainingInputValidator();
    }
}
