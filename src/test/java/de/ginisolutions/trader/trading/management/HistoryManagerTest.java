package de.ginisolutions.trader.trading.management;

import de.ginisolutions.trader.common.messaging.TickListener;
import de.ginisolutions.trader.history.domain.TickDTO;
import de.ginisolutions.trader.history.domain.enumeration.INTERVAL;
import de.ginisolutions.trader.history.domain.enumeration.MARKET;
import de.ginisolutions.trader.history.domain.enumeration.SYMBOL;
import de.ginisolutions.trader.trading.TraderServiceTradingApp;
import de.ginisolutions.trader.trading.config.TestSecurityConfiguration;
import net.engio.mbassy.listener.Handler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static de.ginisolutions.trader.history.domain.enumeration.INTERVAL.ONE_MINUTE;
import static de.ginisolutions.trader.history.domain.enumeration.MARKET.BINANCE;
import static de.ginisolutions.trader.history.domain.enumeration.SYMBOL.BTCUSDT;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(classes = {TraderServiceTradingApp.class, TestSecurityConfiguration.class})
public class HistoryManagerTest implements TickListener {

    private static final MARKET market = BINANCE;

    private static final SYMBOL symbol = BTCUSDT;

    private static final INTERVAL interval = ONE_MINUTE;

    @Autowired
    private HistoryManager historyManager;

    private TickDTO tickReceived;

    @BeforeEach
    public void initTest() {
        this.historyManager.init();
    }

    @Test
    public void whenTickDispatched_thenHandleTick() {
        assertNull(tickReceived);

        this.historyManager.subscribe2crawler(market, symbol, interval, this);

        await().until(() -> tickReceived != null);

        System.out.println(tickReceived.toString());
    }

    @Handler
    public void handleTick(TickDTO tickDTO) {
        this.tickReceived = tickDTO;
    }

}
