package de.ginisolutions.trader.trading.config;

import de.ginisolutions.trader.trading.management.HistoryManager;
import de.ginisolutions.trader.trading.management.StrategistManager;
import de.ginisolutions.trader.trading.management.TraderManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class ApplicationStartup {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);

    private final HistoryManager historyManager;

    private final StrategistManager strategistManager;

    private final TraderManager traderManager;

    public ApplicationStartup(HistoryManager historyManager, StrategistManager strategistManager, TraderManager traderManager) {
        this.historyManager = historyManager;
        this.strategistManager = strategistManager;
        this.traderManager = traderManager;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
       logger.info("Application started, beginning initialisation");
       this.historyManager.init();
       this.strategistManager.init();
       this.traderManager.init();
    }
}
