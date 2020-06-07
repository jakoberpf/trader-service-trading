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
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StrategistManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(StrategistManager.class);

    private final ApplicationEventPublisher signalPublisher;

    private final StrategistRepository strategistRepository;

    private final HistoryManager historyManager;

    private final Map<String, List<StrategistPackage>> strategistMap;

    /**
     * Constructor for StrategistManager. Receives ApplicationEventPublisher via dependency injection.
     *
     * @param signalPublisher the eventPublisher, used for publishing an "Initialisation finished event"
     */
    public StrategistManager(ApplicationEventPublisher signalPublisher, StrategistRepository strategistRepository, HistoryManager historyManager) {
        LOGGER.info("Constructing StrategistManager");
        this.signalPublisher = signalPublisher;
        this.strategistRepository = strategistRepository;
        this.historyManager = historyManager;
        this.strategistMap = new HashMap<>();
        // TODO add Feign Client for quicker data fetch from database
    }

    /**
     * TODO
     *
     * @param init
     */
    @EventListener
    public void init(TradingInit init) {
        LOGGER.info("Initializing StrategistManager");
        final HistoryProvider historyProvider = new HistoryProvider();
        if (init.isHistoryManager()) {
            this.strategistRepository.findAll().forEach(strategistFromRepo -> {
                final String key = strategistFromRepo.getMarket().toString() + strategistFromRepo.getSymbol().toString() + strategistFromRepo.getInterval().toString() + strategistFromRepo.getStrategy().toString();
                final List<StrategistPackage> strategistList = this.strategistMap.get(key);
                if (strategistList != null) {
                    if (strategistList.stream()
                        .anyMatch(strategist -> strategist.getStrategist().getParameters().equals(strategistFromRepo.getParameters()))) {
                        // parameter are the same -> duplicates in the repository
                        LOGGER.warn("Found duplicates in the repository, all the later ones are neglected and deleted");
                        this.strategistRepository.delete(strategistFromRepo);
                    }
                    // create a new strategist package and add it to the list
                    else {
                        final StrategistPackage newStrategistPackage = new StrategistPackage(strategistFromRepo, historyProvider);
                        this.historyManager.subscribe2crawler(strategistFromRepo.getMarket(), strategistFromRepo.getSymbol(), strategistFromRepo.getInterval(), newStrategistPackage);
                        strategistList.add(newStrategistPackage);
                    }
                }
                // create a new strategist package and add it to the list
                else {
                    final StrategistPackage newStrategistPackage = new StrategistPackage(strategistFromRepo, historyProvider);
                    this.historyManager.subscribe2crawler(strategistFromRepo.getMarket(), strategistFromRepo.getSymbol(), strategistFromRepo.getInterval(), newStrategistPackage);
                    this.strategistMap.put(key, List.of(newStrategistPackage));
                }
            });
            // Initialisation is complete
            LOGGER.info("Initialisation of StrategistManager complete");
            init.setHistoryManager();
            this.signalPublisher.publishEvent(init);
        }
    }

    /**
     * Persist all strategists
     */
    @Scheduled(fixedDelay = 10000) // TODO user env variable
    private void persist() {
        this.strategistMap.forEach((s, strategistPackages) -> {
            strategistPackages.forEach(strategistPackage -> {
                LOGGER.debug("Persisting trader: {}", strategistPackage.getStrategist());
                this.strategistRepository.save(strategistPackage.getStrategist());
            });
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
        LOGGER.debug("New subscriber for {} {} {} {}", market, symbol, interval, strategy);

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
        final List<StrategistPackage> strategistList = this.strategistMap.get(key);
        if (strategistList != null) {
            final Optional<StrategistPackage> strategistPackage = strategistList.stream()
                .filter(strategist -> strategist.getStrategist().getParameters().equals(parameters)).findAny();
            if (strategistPackage.isPresent()) {
                // parameter are the same -> subscribe to it
                strategistPackage.get().subscribe(listener);
            }
            // create a new strategist, subscribe to it and add it to the list
            else {
                final Strategist strategist = this.strategistRepository.save(new Strategist(strategy, market, symbol, interval, parameters));
                final StrategistPackage newStrategistPackage = new StrategistPackage(strategist, new HistoryProvider());
                strategistList.add(newStrategistPackage);
                LOGGER.info("Created new strategist: " + strategist);
            }
        }
        // create a new strategist, subscribe to it and save it in new list
        else {
            final Strategist strategist = this.strategistRepository.save(new Strategist(strategy, market, symbol, interval, parameters));
            final StrategistPackage newStrategistPackage = new StrategistPackage(strategist, new HistoryProvider());
            this.strategistMap.put(key, List.of(newStrategistPackage));
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
        final String key = market.toString() + "-" + symbol.toString() + "-" + interval.toString() + "-" + strategy.toString();
        final List<StrategistPackage> strategistList = this.strategistMap.get(key);
        LOGGER.warn("Unsubscription is not implemented");
    }
}
