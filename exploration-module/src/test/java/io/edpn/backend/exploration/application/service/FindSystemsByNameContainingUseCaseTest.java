package io.edpn.backend.exploration.application.service;

import io.edpn.backend.exploration.application.domain.System;
import io.edpn.backend.exploration.application.domain.exception.ValidationException;
import io.edpn.backend.exploration.application.port.incomming.FindSystemsByNameContainingUseCase;
import io.edpn.backend.exploration.application.port.outgoing.station.LoadStationNamesBySystemNamePort;
import io.edpn.backend.exploration.application.port.outgoing.system.LoadSystemsByNameContainingPort;
import io.edpn.backend.exploration.application.validation.LoadByNameContainingValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindSystemsByNameContainingUseCaseTest {

    @Mock
    private LoadSystemsByNameContainingPort loadSystemsByNameContainingPort;
    @Mock
    private LoadByNameContainingValidator loadByNameContainingValidator;
    @Mock
    private LoadStationNamesBySystemNamePort loadStationNamesBySystemNamePort;

    private FindSystemsByNameContainingUseCase underTest;

    @BeforeEach
    void setUp() {
        underTest = new SystemControllerService(loadSystemsByNameContainingPort, loadByNameContainingValidator, loadStationNamesBySystemNamePort);
    }

    @Test
    void testFindSystemsFromSearchBar_shouldThrowOnValidationError() {
        String subString = "sub";
        int amount = 10;
        ValidationException validationException = new ValidationException(List.of("Test Error"));
        when(loadByNameContainingValidator.validate(subString, amount)).thenReturn(Optional.of(validationException));

        assertThrows(ValidationException.class, () -> underTest.findSystemsByNameContaining(subString, amount));

        verifyNoInteractions(loadSystemsByNameContainingPort);
    }

    @Test
    void testFindSystemsFromSearchBar_shouldReturnListOfSystemDto() {

        String subString = "sub";
        int amount = 10;
        System system1 = mock(System.class);
        System system2 = mock(System.class);
        when(loadByNameContainingValidator.validate(subString, amount)).thenReturn(Optional.empty());
        when(loadSystemsByNameContainingPort.loadByNameContaining(subString, amount)).thenReturn(List.of(system1, system2));

        List<System> result = underTest.findSystemsByNameContaining(subString, amount);

        assertThat(result, hasSize(2));
        assertThat(result, containsInAnyOrder(system1, system2));
    }
}