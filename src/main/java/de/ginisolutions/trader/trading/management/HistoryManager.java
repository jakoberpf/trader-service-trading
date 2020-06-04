package de.ginisolutions.trader.trading.management;

import de.ginisolutions.trader.common.market.CrawlerImpl;
import de.ginisolutions.trader.common.market.CrawlerImplFactory;
import de.ginisolutions.trader.history.domain.enumeration.INTERVAL;
import de.ginisolutions.trader.history.domain.enumeration.MARKET;
import de.ginisolutions.trader.history.domain.enumeration.SYMBOL;

import de.ginisolutions.trader.trading.event.TradingInit;
import de.ginisolutions.trader.common.messaging.TickListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Service
public class HistoryManager {

    private static final Logger log = LoggerFactory.getLogger(HistoryManager.class);

    private final ApplicationEventPublisher eventPublisher;

    private final Map<String, CrawlerImpl> crawlerMap;

    /**
     * Constructor for HistoryManager. Receives ApplicationEventPublisher via dependency injection.
     * @param eventPublisher the eventPublisher, used for publishing an "Initialisation finished event"
     */
    public HistoryManager(ApplicationEventPublisher eventPublisher) {
        log.info("Constructing HistoryManager");
        this.eventPublisher = eventPublisher;
        this.crawlerMap = new HashMap<>();
        // TODO add Feign Client for quicker data fetch from database
    }

    /**
     * When the application context is started the initialisation process in the method will be triggered
     * by the ContextStartedEvent.
     * @param event the trigger event, emitted after the application is started
     */
    @EventListener
    @SuppressWarnings("unused")
    public void init(ContextStartedEvent event) { // TODO user TraderInit event instead -> catch ContextStartedEvent in external class
        final TradingInit init = new TradingInit(this);
        init.setHistoryManager();
        this.eventPublisher.publishEvent(init);
    }

    /**
     * This method subscribes the provided tick listener to the crawler defined by the parameters market, symbol and interval
     * @param market defines the market of the crawler
     * @param symbol defines the symbol of the crawler
     * @param interval defines the interval of the crawler
     * @param listener defines the tick listener to be subscribe
     */
    public void subscribe2crawler(MARKET market, SYMBOL symbol, INTERVAL interval, TickListener listener) {
        final String key = market.toString() + symbol.toString() + interval.toString();
        final CrawlerImpl crawler = this.crawlerMap.get(key);
        if (crawler != null) {
            crawler.subscribe(listener);
        }
        else {
            this.crawlerMap.put(key, CrawlerImplFactory.buildCrawler(market, symbol, interval, listener));
        }
    }
}
