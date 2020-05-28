package de.ginisolutions.trader.trading.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.ginisolutions.trader.trading.web.rest.TestUtil;

public class StrategistTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Strategist.class);
        Strategist strategist1 = new Strategist();
        strategist1.setId("id1");
        Strategist strategist2 = new Strategist();
        strategist2.setId(strategist1.getId());
        assertThat(strategist1).isEqualTo(strategist2);
        strategist2.setId("id2");
        assertThat(strategist1).isNotEqualTo(strategist2);
        strategist1.setId(null);
        assertThat(strategist1).isNotEqualTo(strategist2);
    }
}
