package io.edpn.backend.application.controller;

import io.edpn.backend.exploration.adapter.web.FindSystemsByNameContainingInputValidator;
import io.edpn.backend.exploration.adapter.web.SystemController;
import io.edpn.backend.exploration.adapter.web.dto.mapper.RestSystemDtoMapper;
import io.edpn.backend.exploration.application.port.incomming.FindStationNamesBySystemNameUseCase;
import io.edpn.backend.exploration.application.port.incomming.FindSystemsByNameContainingUseCase;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BootExplorationModuleController extends SystemController {

    public BootExplorationModuleController(
            FindSystemsByNameContainingUseCase findSystemsByNameContainingUseCase,
            FindSystemsByNameContainingInputValidator findSystemsByNameContainingInputValidator,
            RestSystemDtoMapper restSystemDtoMapper,
            FindStationNamesBySystemNameUseCase findStationNamesBySystemNameUseCase
    ) {
        super(findSystemsByNameContainingUseCase, findStationNamesBySystemNameUseCase, findSystemsByNameContainingInputValidator, restSystemDtoMapper);
    }
}
