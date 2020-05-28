package de.ginisolutions.trader.trading.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class StrategistMapperTest {

    private StrategistMapper strategistMapper;

    @BeforeEach
    public void setUp() {
        strategistMapper = new StrategistMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        String id = "id1";
        assertThat(strategistMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(strategistMapper.fromId(null)).isNull();
    }
}
