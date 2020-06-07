package de.ginisolutions.trader.trading.management;

import de.ginisolutions.trader.common.strategy.parameter.ParameterCommodityChannelIndex;
import de.ginisolutions.trader.common.strategy.parameter.ParameterMovingMomentum;
import de.ginisolutions.trader.common.strategy.parameter.ParameterRelativeStrengthIndex;
import de.ginisolutions.trader.common.strategy.parameter.StrategyParameter;
import de.ginisolutions.trader.history.domain.enumeration.INTERVAL;
import de.ginisolutions.trader.history.domain.enumeration.MARKET;
import de.ginisolutions.trader.history.domain.enumeration.SYMBOL;
import de.ginisolutions.trader.trading.domain.Strategist;
import de.ginisolutions.trader.trading.domain.StrategistPackage;
import de.ginisolutions.trader.trading.domain.enumeration.STRATEGY;
import de.ginisolutions.trader.trading.event.TradingInit;
import de.ginisolutions.trader.trading.messaging.SignalListener;
import de.ginisolutions.trader.trading.repository.StrategistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
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
            final String key = strategist.getMarket().toString() + strategist.getSymbol().toString() + strategist.getInterval().toString() + strategist.getStrategy().toString();
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
            LOGGER.debug("Persisting trader: {}", strategistPackage.getStrategist());
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

        // TODO simplify after testing
        // TODO get parameters from current learning status or repository
        final StrategyParameter parameters;
        switch (strategy) {
            case MM:
                parameters = new ParameterMovingMomentum(10, 30, 14, 9, 26, 18, 20);
                break;
            case RSI:
                parameters = new ParameterRelativeStrengthIndex(10, 200, 2, 5, 95);
                break;
            case CCI:
                parameters = new ParameterCommodityChannelIndex(200, -5, 100, -100, 5);
                break;
            case SAMPLE_ENUM:
                parameters = null;
                break;
            default:
                throw new IllegalArgumentException("this is not allowed" + strategy);
        }

        final String key = market.toString() + "-" + symbol.toString() + "-" + interval.toString() + "-" + strategy.toString();
        final StrategistPackage strategistPackage = this.strategistMap.get(key);
        if (strategistPackage != null) {
            // strategist is already present -> subscribe to it
            strategistPackage.subscribe(listener);
        } else {
            // create a new strategist, subscribe to it and save it in new list
            final Strategist strategist = this.strategistRepository.save(new Strategist(strategy, market, symbol, interval, parameters));
            final StrategistPackage newStrategistPackage = new StrategistPackage(strategist, this.historyProvider);
            this.historyManager.subscribe2crawler(strategist.getMarket(), strategist.getSymbol(), strategist.getInterval(), newStrategistPackage);
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
