package de.ginisolutions.trader.trading.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.ginisolutions.trader.trading.web.rest.TestUtil;

public class TraderTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Trader.class);
        Trader trader1 = new Trader();
        trader1.setId("id1");
        Trader trader2 = new Trader();
        trader2.setId(trader1.getId());
        assertThat(trader1).isEqualTo(trader2);
        trader2.setId("id2");
        assertThat(trader1).isNotEqualTo(trader2);
        trader1.setId(null);
        assertThat(trader1).isNotEqualTo(trader2);
    }
}
