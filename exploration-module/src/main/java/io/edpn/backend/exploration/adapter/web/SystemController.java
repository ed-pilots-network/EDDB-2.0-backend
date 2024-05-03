package io.edpn.backend.exploration.adapter.web;

import io.edpn.backend.exploration.adapter.web.dto.RestSystemDto;
import io.edpn.backend.exploration.adapter.web.dto.mapper.RestSystemDtoMapper;
import io.edpn.backend.exploration.application.port.incomming.FindStationNamesBySystemNameUseCase;
import io.edpn.backend.exploration.application.port.incomming.FindSystemsByNameContainingUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/exploration/system")
public class SystemController {

    final FindSystemsByNameContainingUseCase findSystemsByNameContainingUseCase;
    final FindStationNamesBySystemNameUseCase findStationNamesBySystemNameUseCase;
    final FindSystemsByNameContainingInputValidator findSystemsByNameContainingInputValidator;
    final RestSystemDtoMapper restSystemDtoMapper;

    @Operation(summary = "Find systems by name containing a specific substring, will give priority to return systems starting with the given substring")
    @GetMapping("/by-name-containing")
    public List<RestSystemDto> findByNameContaining(
            @Parameter(description = "Substring to search for in the system name, minimal 3 characters", required = true) @RequestParam(name = "subString") String subString,
            @Parameter(description = "Amount of results to retrieve. values 1...100", required = false) @RequestParam(name = "amount", required = false, defaultValue = "10") Integer amount) {
        findSystemsByNameContainingInputValidator.validateSubString(subString);
        findSystemsByNameContainingInputValidator.validateAmount(amount);
        return findSystemsByNameContainingUseCase.findSystemsByNameContaining(subString, amount)
                .stream()
                .map(restSystemDtoMapper::map)
                .toList();
    }
    
    @Operation(summary = "Find station names by full System Name, will respond with list of names only")
    @GetMapping("/list-station-names")
    public List<String> stationNamesBySystemName(
            @Parameter(description = "Full case-sensitive system name", required = true) @RequestParam(name = "systemName") String systemName){
        
        return findStationNamesBySystemNameUseCase
                .findStationNamesBySystemName(systemName)
                .stream()
                .toList();
    }
}
