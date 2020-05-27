package de.ginisolutions.trader.trading.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.ginisolutions.trader.trading.web.rest.TestUtil;

public class TraderDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TraderDTO.class);
        TraderDTO traderDTO1 = new TraderDTO();
        traderDTO1.setId("id1");
        TraderDTO traderDTO2 = new TraderDTO();
        assertThat(traderDTO1).isNotEqualTo(traderDTO2);
        traderDTO2.setId(traderDTO1.getId());
        assertThat(traderDTO1).isEqualTo(traderDTO2);
        traderDTO2.setId("id2");
        assertThat(traderDTO1).isNotEqualTo(traderDTO2);
        traderDTO1.setId(null);
        assertThat(traderDTO1).isNotEqualTo(traderDTO2);
    }
}
