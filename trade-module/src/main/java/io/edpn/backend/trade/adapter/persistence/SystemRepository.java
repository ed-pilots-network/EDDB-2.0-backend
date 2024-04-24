package io.edpn.backend.trade.adapter.persistence;

import io.edpn.backend.trade.adapter.persistence.entity.mapper.MybatisSystemEntityMapper;
import io.edpn.backend.trade.adapter.persistence.filter.mapper.MybatisFindSystemFilterMapper;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisSystemRepository;
import io.edpn.backend.trade.application.domain.System;
import io.edpn.backend.trade.application.domain.filter.FindSystemFilter;
import io.edpn.backend.trade.application.port.outgoing.system.CreateOrLoadSystemPort;
import io.edpn.backend.trade.application.port.outgoing.system.LoadSystemByIdPort;
import io.edpn.backend.trade.application.port.outgoing.system.LoadSystemsByFilterPort;
import io.edpn.backend.trade.application.port.outgoing.system.LoadSystemsByNameContainingPort;
import io.edpn.backend.trade.application.port.outgoing.system.UpdateSystemPort;
import io.edpn.backend.util.exception.DatabaseEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class SystemRepository implements CreateOrLoadSystemPort, LoadSystemByIdPort, UpdateSystemPort, LoadSystemsByFilterPort, LoadSystemsByNameContainingPort {

    private final MybatisSystemEntityMapper mybatisSystemEntityMapper;
    private final MybatisFindSystemFilterMapper persistenceFindSystemFilter;
    private final MybatisSystemRepository mybatisSystemRepository;

    @Override
    public System createOrLoad(System system) {
        return mybatisSystemEntityMapper.map(mybatisSystemRepository.createOrUpdateOnConflict(mybatisSystemEntityMapper.map(system)));
    }

    @Override
    public Optional<System> loadById(UUID uuid) {
        return mybatisSystemRepository.findById(uuid)
                .map(mybatisSystemEntityMapper::map);
    }

    @Override
    public System update(System system) {
        var entity = mybatisSystemEntityMapper.map(system);

        mybatisSystemRepository.update(entity);
        return loadById(entity.getId())
                .orElseThrow(() -> new DatabaseEntityNotFoundException("system with id: " + entity.getId() + " could not be found after update"));
    }

    @Override
    public List<System> loadByFilter(FindSystemFilter findSystemFilter) {
        return mybatisSystemRepository.findByFilter(persistenceFindSystemFilter.map(findSystemFilter))
                .stream()
                .map(mybatisSystemEntityMapper::map)
                .toList();
    }
    
    @Override
    public List<System> loadSystemsByNameContaining(String name, Integer amount) {
        return mybatisSystemRepository.findSystemsByNameContaining(name, amount)
                .stream()
                .map(mybatisSystemEntityMapper::map)
                .toList();
    }
}
