package io.edpn.backend.trade.application.service;

import io.edpn.backend.messageprocessorlib.application.dto.eddn.data.SystemDataRequest;
import io.edpn.backend.trade.application.domain.System;
import io.edpn.backend.trade.application.domain.filter.FindSystemFilter;
import io.edpn.backend.trade.application.port.outgoing.system.LoadSystemsByFilterPort;
import io.edpn.backend.trade.application.port.outgoing.systemeliteidrequest.CleanUpObsoleteSystemEliteIdRequestsUseCase;
import io.edpn.backend.trade.application.port.outgoing.systemeliteidrequest.DeleteSystemEliteIdRequestPort;
import io.edpn.backend.trade.application.port.outgoing.systemeliteidrequest.LoadAllSystemEliteIdRequestsPort;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CleanUpObsoleteSystemEliteIdRequestsUseCaseTest {

    @Mock
    private LoadSystemsByFilterPort loadSystemsByFilterPort;

    @Mock
    private LoadAllSystemEliteIdRequestsPort allSystemEliteIdRequestsPort;

    @Mock
    private DeleteSystemEliteIdRequestPort deleteSystemEliteIdRequestPort;

    private CleanUpObsoleteSystemEliteIdRequestsUseCase underTest;

    @BeforeEach
    public void setUp() {
        underTest = new CleanUpObsoleteSystemEliteIdRequestsService(loadSystemsByFilterPort, allSystemEliteIdRequestsPort, deleteSystemEliteIdRequestPort);
    }

    @Test
    public void testFindSystemsFilter() {
        FindSystemFilter findSystemFilter = FindSystemFilter.builder()
                .hasEliteId(false)
                .build();

        assertThat(CleanUpObsoleteSystemEliteIdRequestsService.FIND_SYSTEM_FILTER, equalTo(findSystemFilter));
    }

    @Test
    public void testCleanUpObsolete_NoOpenRequests() {
        when(allSystemEliteIdRequestsPort.loadAll()).thenReturn(Collections.emptyList());
        when(loadSystemsByFilterPort.loadByFilter(any())).thenReturn(Collections.emptyList());

        underTest.cleanUpObsolete();

        verify(deleteSystemEliteIdRequestPort, never()).delete(any());
    }

    @Test
    public void testCleanUpObsolete_MoreMissingItems() {
        SystemDataRequest request1 = mock(SystemDataRequest.class);
        when(request1.systemName()).thenReturn("Alpha");
        SystemDataRequest request2 = mock(SystemDataRequest.class);
        when(request2.systemName()).thenReturn("Beta");

        System system3 = mock(System.class);
        when(system3.getName()).thenReturn("Alpha");

        System system2 = mock(System.class);
        when(system2.getName()).thenReturn("Beta");

        System system1 = mock(System.class);
        when(system1.getName()).thenReturn("Gamma");

        when(allSystemEliteIdRequestsPort.loadAll()).thenReturn(List.of(request1, request2));
        when(loadSystemsByFilterPort.loadByFilter(any())).thenReturn(List.of(system1, system2, system3));

        underTest.cleanUpObsolete();

        verify(deleteSystemEliteIdRequestPort, never()).delete(any());
    }

    @Test
    public void testCleanUpObsolete_LessMissingItems() {
        SystemDataRequest request1 = mock(SystemDataRequest.class);
        when(request1.systemName()).thenReturn("Alpha");
        SystemDataRequest request2 = mock(SystemDataRequest.class);
        when(request2.systemName()).thenReturn("Beta");

        System system1 = mock(System.class);
        when(system1.getName()).thenReturn("Alpha");

        when(allSystemEliteIdRequestsPort.loadAll()).thenReturn(List.of(request1, request2));
        when(loadSystemsByFilterPort.loadByFilter(any())).thenReturn(List.of(system1));

        underTest.cleanUpObsolete();

        verify(deleteSystemEliteIdRequestPort).delete("Beta");
    }

    @Test
    public void testCleanUpObsolete_TheSameMissingItems() {

        SystemDataRequest request1 = mock(SystemDataRequest.class);
        when(request1.systemName()).thenReturn("Alpha");
        SystemDataRequest request2 = mock(SystemDataRequest.class);
        when(request2.systemName()).thenReturn("Beta");

        System system1 = mock(System.class);
        when(system1.getName()).thenReturn("Alpha");

        System system2 = mock(System.class);
        when(system2.getName()).thenReturn("Beta");

        when(allSystemEliteIdRequestsPort.loadAll()).thenReturn(List.of(request1, request2));
        when(loadSystemsByFilterPort.loadByFilter(any())).thenReturn(List.of(system1, system2));

        underTest.cleanUpObsolete();

        verify(deleteSystemEliteIdRequestPort, never()).delete(any());
    }
}
