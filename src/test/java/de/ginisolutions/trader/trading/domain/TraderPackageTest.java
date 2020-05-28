package de.ginisolutions.trader.trading.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.ginisolutions.trader.trading.web.rest.TestUtil;

public class TraderPackageTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TraderPackage.class);
        TraderPackage traderPackage1 = new TraderPackage();
        traderPackage1.setId("id1");
        TraderPackage traderPackage2 = new TraderPackage();
        traderPackage2.setId(traderPackage1.getId());
        assertThat(traderPackage1).isEqualTo(traderPackage2);
        traderPackage2.setId("id2");
        assertThat(traderPackage1).isNotEqualTo(traderPackage2);
        traderPackage1.setId(null);
        assertThat(traderPackage1).isNotEqualTo(traderPackage2);
    }
}
