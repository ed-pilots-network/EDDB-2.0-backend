package io.edpn.backend.trade.adapter.persistence.stationarrivaldistancerequest;

import io.edpn.backend.trade.adapter.persistence.StationArrivalDistanceRequestRepository;
import io.edpn.backend.trade.adapter.persistence.entity.mapper.MybatisStationDataRequestEntityMapper;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisStationArrivalDistanceRequestRepository;
import io.edpn.backend.trade.application.port.outgoing.stationarrivaldistancerequest.CreateIfNotExistsStationArrivalDistanceRequestPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CreateStationArrivalDistanceRequestPortTest {

    @Mock
    private MybatisStationArrivalDistanceRequestRepository mybatisStationArrivalDistanceRequestRepository;
    @Mock
    private MybatisStationDataRequestEntityMapper mybatisStationDataRequestEntityMapper;

    private CreateIfNotExistsStationArrivalDistanceRequestPort underTest;

    @BeforeEach
    public void setup() {
        underTest = new StationArrivalDistanceRequestRepository(mybatisStationArrivalDistanceRequestRepository, mybatisStationDataRequestEntityMapper);
    }

    @Test
    public void testCreate() {
        String systemName = "someName";
        String stationName = "someName2";

        underTest.createIfNotExists(systemName, stationName);

        verify(mybatisStationArrivalDistanceRequestRepository).insertIfNotExists(systemName, stationName);
    }


}
