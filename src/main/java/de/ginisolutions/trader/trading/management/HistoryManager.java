package de.ginisolutions.trader.trading.management;

import de.ginisolutions.trader.common.market.CrawlerImpl;
import de.ginisolutions.trader.common.market.CrawlerImplFactory;
import de.ginisolutions.trader.common.messaging.TickListener;
import de.ginisolutions.trader.common.enumeration.INTERVAL;
import de.ginisolutions.trader.common.enumeration.MARKET;
import de.ginisolutions.trader.common.enumeration.SYMBOL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Service
public class HistoryManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(HistoryManager.class);

    private final Map<String, CrawlerImpl> crawlerMap;

    /**
     * Constructor for HistoryManager
     */
    public HistoryManager() {
        LOGGER.info("Constructing HistoryManager");
        this.crawlerMap = new HashMap<>();
        // TODO add Feign Client for quicker data fetch from database
    }

    /**
     * When the application context is started the initialisation process in the method will be evoked
     * by the ApplicationStartup
     */
    public void init() {
        LOGGER.info("Initializing HistoryManager");
        LOGGER.info("Initialisation of HistoryManager complete");
    }

    /**
     * This method subscribes the provided tick listener to the crawler defined by the parameters market, symbol and interval
     *
     * @param market   defines the market of the crawler
     * @param symbol   defines the symbol of the crawler
     * @param interval defines the interval of the crawler
     * @param listener defines the tick listener to be subscribe
     */
    public void subscribe2crawler(MARKET market, SYMBOL symbol, INTERVAL interval, TickListener listener) {
        final String key = market.toString() + symbol.toString() + interval.toString();
        final CrawlerImpl crawler = this.crawlerMap.get(key);
        if (crawler != null) {
            crawler.subscribe(listener);
        } else {
            final CrawlerImpl newCrawler = CrawlerImplFactory.buildCrawler(market, symbol, interval, listener);
            newCrawler.run();
            this.crawlerMap.put(key, newCrawler);
        }
    }
}
