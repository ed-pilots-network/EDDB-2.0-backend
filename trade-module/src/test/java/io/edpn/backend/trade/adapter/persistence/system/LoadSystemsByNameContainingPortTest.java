package io.edpn.backend.trade.adapter.persistence.system;

import io.edpn.backend.trade.adapter.persistence.SystemRepository;
import io.edpn.backend.trade.adapter.persistence.entity.MybatisSystemEntity;
import io.edpn.backend.trade.adapter.persistence.entity.mapper.MybatisSystemEntityMapper;
import io.edpn.backend.trade.adapter.persistence.filter.mapper.MybatisFindSystemFilterMapper;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisSystemRepository;
import io.edpn.backend.trade.application.domain.System;
import io.edpn.backend.trade.application.port.outgoing.system.LoadSystemsByNameContainingPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoadSystemsByNameContainingPortTest {
    
    @Mock
    private MybatisSystemEntityMapper systemEntityMapper;
    
    @Mock
    private MybatisFindSystemFilterMapper mybatisFindSystemFilterMapper;
    
    @Mock
    private MybatisSystemRepository mybatisSystemRepository;
    
    private LoadSystemsByNameContainingPort underTest;
    
    @BeforeEach
    void setUp() {
        underTest = new SystemRepository(systemEntityMapper, mybatisFindSystemFilterMapper, mybatisSystemRepository);
    }
    
    @Test
    void load_withNameAndAmount_shouldFindFromSearchbarAndMap() {
        
        String name = "system";
        int amount = 10;
        MybatisSystemEntity mybatisSystemEntity = mock(MybatisSystemEntity.class);
        List<MybatisSystemEntity> entities = List.of(mybatisSystemEntity);
        System mapped = mock(System.class);
        when(mybatisSystemRepository.findSystemsByNameContaining(name, amount)).thenReturn(entities);
        when(systemEntityMapper.map(mybatisSystemEntity)).thenReturn(mapped);
        
        
        List<System> result = underTest.loadSystemsByNameContaining(name, amount);
        
        
        assertThat(result, contains(mapped));
        verify(mybatisSystemRepository).findSystemsByNameContaining(name, amount);
    }
}
