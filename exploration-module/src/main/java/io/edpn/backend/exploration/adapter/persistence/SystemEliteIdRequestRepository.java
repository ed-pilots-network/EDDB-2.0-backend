package io.edpn.backend.exploration.adapter.persistence;

import io.edpn.backend.exploration.adapter.persistence.entity.MybatisSystemEliteIdRequestEntity;
import io.edpn.backend.exploration.adapter.persistence.entity.mapper.MybatisSystemEliteIdRequestEntityMapper;
import io.edpn.backend.exploration.application.domain.SystemEliteIdRequest;
import io.edpn.backend.exploration.application.port.outgoing.systemeliteidrequest.CreateSystemEliteIdRequestPort;
import io.edpn.backend.exploration.application.port.outgoing.systemeliteidrequest.DeleteSystemEliteIdRequestPort;
import io.edpn.backend.exploration.application.port.outgoing.systemeliteidrequest.LoadAllSystemEliteIdRequestPort;
import io.edpn.backend.exploration.application.port.outgoing.systemeliteidrequest.LoadSystemEliteIdRequestBySystemNamePort;
import io.edpn.backend.exploration.application.port.outgoing.systemeliteidrequest.LoadSystemEliteIdRequestPort;
import io.edpn.backend.util.Module;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class SystemEliteIdRequestRepository implements CreateSystemEliteIdRequestPort, LoadSystemEliteIdRequestPort, LoadSystemEliteIdRequestBySystemNamePort, LoadAllSystemEliteIdRequestPort, DeleteSystemEliteIdRequestPort {

    private final MybatisSystemEliteIdRequestRepository mybatisSystemEliteIdRequestRepository;
    private final MybatisSystemEliteIdRequestEntityMapper mybatisSystemEliteIdRequestEntityMapper;

    @Override
    public void create(SystemEliteIdRequest systemEliteIdRequest) {
        mybatisSystemEliteIdRequestRepository.insert(mybatisSystemEliteIdRequestEntityMapper.map(systemEliteIdRequest));
    }

    @Override
    public void delete(String systemName, Module requestingModule) {
        mybatisSystemEliteIdRequestRepository.delete(new MybatisSystemEliteIdRequestEntity(systemName, requestingModule));
    }

    @Override
    public List<SystemEliteIdRequest> loadByName(String systemName) {
        return mybatisSystemEliteIdRequestRepository.findBySystemName(systemName).stream()
                .map(mybatisSystemEliteIdRequestEntityMapper::map)
                .toList();
    }

    @Override
    public List<SystemEliteIdRequest> loadAll() {
        return mybatisSystemEliteIdRequestRepository.findAll().stream()
                .map(mybatisSystemEliteIdRequestEntityMapper::map)
                .toList();
    }

    @Override
    public Optional<SystemEliteIdRequest> load(SystemEliteIdRequest systemEliteIdRequest) {
        return mybatisSystemEliteIdRequestRepository.find(systemEliteIdRequest.requestingModule(), systemEliteIdRequest.systemName())
                .map(mybatisSystemEliteIdRequestEntityMapper::map);
    }
}
