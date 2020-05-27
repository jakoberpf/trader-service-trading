package de.ginisolutions.trader.trading.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TraderMapperTest {

    private TraderMapper traderMapper;

    @BeforeEach
    public void setUp() {
        traderMapper = new TraderMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        String id = "id1";
        assertThat(traderMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(traderMapper.fromId(null)).isNull();
    }
}
