package de.ginisolutions.trader.trading.management;

import de.ginisolutions.trader.common.strategy.parameter.ParameterCCI;
import de.ginisolutions.trader.common.strategy.parameter.ParameterMM;
import de.ginisolutions.trader.common.strategy.parameter.ParameterRSI;
import de.ginisolutions.trader.common.strategy.parameter.StrategyParameter;
import de.ginisolutions.trader.common.enumeration.INTERVAL;
import de.ginisolutions.trader.common.enumeration.MARKET;
import de.ginisolutions.trader.common.enumeration.SYMBOL;
import de.ginisolutions.trader.trading.domain.Strategist;
import de.ginisolutions.trader.common.enumeration.STRATEGY;
import de.ginisolutions.trader.common.messaging.SignalListener;
import de.ginisolutions.trader.trading.repository.StrategistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StrategistManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(StrategistManager.class);

    private final ApplicationEventPublisher eventPublisher;

    private final StrategistRepository strategistRepository;

    private final HistoryManager historyManager;

    private final HistoryProvider historyProvider;

    private final Map<String, StrategistPackage> strategistMap;

    /**
     * Constructor for StrategistManager. Receives ApplicationEventPublisher via dependency injection.
     *
     * @param eventPublisher the eventPublisher, used for publishing an "Initialisation finished event"
     */
    public StrategistManager(ApplicationEventPublisher eventPublisher, StrategistRepository strategistRepository, HistoryManager historyManager, HistoryProvider historyProvider) {
        LOGGER.info("Constructing StrategistManager");
        this.eventPublisher = eventPublisher;
        this.strategistRepository = strategistRepository;
        this.historyManager = historyManager;
        this.historyProvider = historyProvider;
        this.strategistMap = new HashMap<>();
        // TODO add Feign Client for quicker data fetch from database
    }

    /**
     * TODO
     */
    public void init() {
        LOGGER.info("Initializing StrategistManager");
        this.strategistRepository.findAll().forEach(strategist -> {
            final String key = strategist.getMarket().toString() + "-" + strategist.getSymbol().toString() + "-" + strategist.getInterval().toString() + "-" + strategist.getStrategy().toString();
            final StrategistPackage strategistPackage = this.strategistMap.get(key);
            if (strategistPackage != null) {
                // duplicates in the repository, only the first one is relevant
                LOGGER.warn("Found duplicates in the repository, all the later ones are neglected and deleted");
                this.strategistRepository.delete(strategist);
            }
            // create a new strategist package and put it in the map
            else {
                final StrategistPackage newStrategistPackage = new StrategistPackage(strategist, historyProvider);
                this.historyManager.subscribe2crawler(strategist.getMarket(), strategist.getSymbol(), strategist.getInterval(), newStrategistPackage);
                this.strategistMap.put(key, newStrategistPackage);
            }
        });
        // Initialisation is complete
        LOGGER.info("Initialisation of StrategistManager complete");

    }

    /**
     * Persist all strategists
     */
    @Scheduled(fixedDelay = 20000) // TODO user env variable
    private void persist() {
        this.strategistMap.forEach((s, strategistPackage) -> {
            LOGGER.debug("Persisting strategist: {}", strategistPackage.getStrategist());
            this.strategistRepository.save(strategistPackage.getStrategist());
        });
    }

    /**
     * This method subscribes the provided signal listener to the strategist defined by the parameters strategy, market, symbol and interval
     *
     * @param market   defines the market of the crawler
     * @param symbol   defines the symbol of the crawler
     * @param interval defines the interval of the crawler
     * @param listener defines the tick listener to be subscribe
     */
    public void subscribe(MARKET market, SYMBOL symbol, INTERVAL interval, STRATEGY strategy, SignalListener listener) {
        LOGGER.debug("Subscribing {} to {} {} {} {}", listener, market, symbol, interval, strategy);
        final String key = market.toString() + "-" + symbol.toString() + "-" + interval.toString() + "-" + strategy.toString();
        final StrategistPackage strategistPackage = this.strategistMap.get(key);
        if (strategistPackage != null) {
            // strategist is already present -> subscribe to it
            strategistPackage.subscribe(listener);
        } else {
            // create a new strategist with package
            final Strategist strategist = this.strategistRepository.save(new Strategist(strategy, market, symbol, interval, null)); // TODO get parameters from analysis service
            final StrategistPackage newStrategistPackage = new StrategistPackage(strategist, this.historyProvider);
            // subscribe strategist to crawler
            this.historyManager.subscribe2crawler(strategist.getMarket(), strategist.getSymbol(), strategist.getInterval(), newStrategistPackage);
            // subscribe listener to strategist
            newStrategistPackage.subscribe(listener);
            // save strategist in map;
            this.strategistMap.put(key, newStrategistPackage);
            LOGGER.info("Created new strategist: " + strategist);
        }
    }

    /**
     * This method subscribes the provided signal listener to the strategist defined by the parameters strategy, market, symbol and interval
     *
     * @param market   defines the market of the crawler
     * @param symbol   defines the symbol of the crawler
     * @param interval defines the interval of the crawler
     * @param listener defines the tick listener to be subscribe
     */
    public void unsubscribe(MARKET market, SYMBOL symbol, INTERVAL interval, STRATEGY strategy, SignalListener listener) {
        LOGGER.debug("Unsubscribing {} from {} {} {} {}", listener, market, symbol, interval, strategy);
        final String key = market.toString() + "-" + symbol.toString() + "-" + interval.toString() + "-" + strategy.toString();
        final StrategistPackage strategistPackage = this.strategistMap.get(key);
        strategistPackage.unsubscribe(listener);
    }
}
