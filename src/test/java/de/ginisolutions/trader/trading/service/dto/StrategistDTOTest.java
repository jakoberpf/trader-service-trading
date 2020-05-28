package de.ginisolutions.trader.trading.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.ginisolutions.trader.trading.web.rest.TestUtil;

public class StrategistDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StrategistDTO.class);
        StrategistDTO strategistDTO1 = new StrategistDTO();
        strategistDTO1.setId("id1");
        StrategistDTO strategistDTO2 = new StrategistDTO();
        assertThat(strategistDTO1).isNotEqualTo(strategistDTO2);
        strategistDTO2.setId(strategistDTO1.getId());
        assertThat(strategistDTO1).isEqualTo(strategistDTO2);
        strategistDTO2.setId("id2");
        assertThat(strategistDTO1).isNotEqualTo(strategistDTO2);
        strategistDTO1.setId(null);
        assertThat(strategistDTO1).isNotEqualTo(strategistDTO2);
    }
}
