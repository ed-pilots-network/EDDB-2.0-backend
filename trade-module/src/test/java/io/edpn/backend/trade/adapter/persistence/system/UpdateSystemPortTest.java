package io.edpn.backend.trade.adapter.persistence.system;

import io.edpn.backend.trade.adapter.persistence.SystemRepository;
import io.edpn.backend.trade.adapter.persistence.entity.MybatisSystemEntity;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisSystemRepository;
import io.edpn.backend.trade.application.domain.System;
import io.edpn.backend.trade.application.dto.persistence.entity.mapper.SystemEntityMapper;
import io.edpn.backend.trade.application.dto.persistence.filter.mapper.PersistenceFindSystemFilterMapper;
import io.edpn.backend.trade.application.port.outgoing.system.UpdateSystemPort;
import io.edpn.backend.util.exception.DatabaseEntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateSystemPortTest {

    @Mock
    private SystemEntityMapper<MybatisSystemEntity> mybatisSystemEntityMapper;
    @Mock
    private MybatisSystemRepository mybatisSystemRepository;
    @Mock
    private PersistenceFindSystemFilterMapper mybatisPersistenceFindSystemFilterMapper;
    private UpdateSystemPort underTest;

    @BeforeEach
    public void setUp() {
        underTest = new SystemRepository(mybatisSystemEntityMapper, mybatisPersistenceFindSystemFilterMapper, mybatisSystemRepository);
    }

    @Test
    void testUpdateWhenSystemIsFoundAfterUpdate() {
        System inputSystem = mock(System.class);
        MybatisSystemEntity inputSystemEntity = mock(MybatisSystemEntity.class);
        MybatisSystemEntity resultSystemEntity = mock(MybatisSystemEntity.class);
        System expectedSystem = mock(System.class);

        when(mybatisSystemEntityMapper.map(inputSystem)).thenReturn(inputSystemEntity);
        UUID uuid = UUID.randomUUID();
        when(inputSystemEntity.getId()).thenReturn(uuid);
        when(mybatisSystemRepository.findById(uuid)).thenReturn(Optional.of(resultSystemEntity));
        when(mybatisSystemEntityMapper.map(resultSystemEntity)).thenReturn(expectedSystem);

        System result = underTest.update(inputSystem);

        assertThat(result, is(expectedSystem));
        verify(mybatisSystemEntityMapper, times(1)).map(inputSystem);
        verify(mybatisSystemRepository, times(1)).update(inputSystemEntity);
    }

    @Test
    void testUpdateWhenSystemIsNotFoundAfterUpdate() {
        System inputSystem = mock(System.class);
        MybatisSystemEntity inputSystemEntity = mock(MybatisSystemEntity.class);

        when(mybatisSystemEntityMapper.map(inputSystem)).thenReturn(inputSystemEntity);
        when(inputSystemEntity.getId()).thenReturn(UUID.randomUUID());
        when(mybatisSystemRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        DatabaseEntityNotFoundException exception = assertThrows(DatabaseEntityNotFoundException.class, () -> underTest.update(inputSystem));

        assertThat(exception.getMessage(), equalTo("system with id: " + inputSystemEntity.getId() + " could not be found after update"));
        verify(mybatisSystemEntityMapper, times(1)).map(inputSystem);
        verify(mybatisSystemRepository, times(1)).update(inputSystemEntity);
    }

}
