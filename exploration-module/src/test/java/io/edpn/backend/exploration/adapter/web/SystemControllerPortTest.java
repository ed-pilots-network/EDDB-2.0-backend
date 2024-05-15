package io.edpn.backend.exploration.adapter.web;

import io.edpn.backend.exploration.adapter.web.dto.RestSystemDto;
import io.edpn.backend.exploration.adapter.web.dto.mapper.RestSystemDtoMapper;
import io.edpn.backend.exploration.application.domain.System;
import io.edpn.backend.exploration.application.port.incomming.FindStationNamesBySystemNameUseCase;
import io.edpn.backend.exploration.application.port.incomming.FindSystemsByNameContainingUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SystemControllerPortTest {

    @Mock
    private FindSystemsByNameContainingUseCase findSystemsByNameContainingUseCase;
    @Mock
    private FindStationNamesBySystemNameUseCase findStationNamesBySystemNameUseCase;
    @Mock
    private FindSystemsByNameContainingInputValidator findSystemsByNameContainingInputValidator;
    @Mock
    private RestSystemDtoMapper restSystemDtoMapper;
    
    private SystemController underTest;

    @BeforeEach
    void setUp() {
        underTest = new SystemController(findSystemsByNameContainingUseCase, findStationNamesBySystemNameUseCase, findSystemsByNameContainingInputValidator, restSystemDtoMapper);
    }

    @Test
    public void testFindSystemsFromSearchBar_shouldThrowOnInvalidSubString() {
        String subString = null;
        int amount = 10;
        doThrow(new IllegalArgumentException("subString must not be null or empty"))
                .when(findSystemsByNameContainingInputValidator)
                .validateSubString(subString);

        assertThrows(IllegalArgumentException.class, () -> underTest.findByNameContaining(subString, amount));

        verify(findSystemsByNameContainingInputValidator).validateSubString(subString);
    }

    @Test
    public void testFindSystemsFromSearchBar_shouldThrowOnInvalidAmount() {
        String subString = "test";
        int amount = 1;
        doThrow(new IllegalArgumentException("Amount must not be strict positive"))
                .when(findSystemsByNameContainingInputValidator)
                .validateAmount(amount);

        assertThrows(IllegalArgumentException.class, () -> underTest.findByNameContaining(subString, amount));

        verify(findSystemsByNameContainingInputValidator).validateAmount(amount);
    }


    @Test
    public void testFindSystemsFromSearchBar_shouldReturnSystems() {
        String subString = "test";
        int amount = 10;
        System system = mock(System.class);
        RestSystemDto restSystemDto = mock(RestSystemDto.class);
        List<System> expectedSystems = List.of(system);
        when(findSystemsByNameContainingUseCase.findSystemsByNameContaining(subString, amount)).thenReturn(expectedSystems);
        when(restSystemDtoMapper.map(system)).thenReturn(restSystemDto);

        List<RestSystemDto> actualSystems = underTest.findByNameContaining(subString, amount);

        assertThat(actualSystems, contains(restSystemDto));
        verify(findSystemsByNameContainingUseCase).findSystemsByNameContaining(subString, amount);
    }
    
    @Test
    public void testFindStationNamesFromSystemName(){
        String systemName = "Sol";
        
        List<String> stations = List.of(
                "Columbus",
                "Daedalus",
                "Dekker's Yard",
                "Dewsnap Prospecting Station",
                "Fraser Industrial Moulding",
                "Galileo",
                "Ji's Slumber",
                "Li Qing Jao",
                "M.Gorbachev",
                "Majoro Entertainment Complex",
                "Mars High");
        
        when(findStationNamesBySystemNameUseCase.findStationNamesBySystemName(systemName)).thenReturn(stations);
        
        List<String> result = underTest.stationNamesBySystemName(systemName);
        
        assertThat(result, hasSize(11));
        assertThat(result, containsInAnyOrder(
                "Columbus",
                "Daedalus",
                "Dekker's Yard",
                "Dewsnap Prospecting Station",
                "Fraser Industrial Moulding",
                "Galileo",
                "Ji's Slumber",
                "Li Qing Jao",
                "M.Gorbachev",
                "Majoro Entertainment Complex",
                "Mars High"));
    }
    
}