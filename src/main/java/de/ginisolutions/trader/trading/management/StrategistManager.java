package de.ginisolutions.trader.trading.management;

import de.ginisolutions.trader.common.messaging.TickListener;
import de.ginisolutions.trader.common.strategy.parameter.StrategyParameter;
import de.ginisolutions.trader.history.domain.enumeration.INTERVAL;
import de.ginisolutions.trader.history.domain.enumeration.MARKET;
import de.ginisolutions.trader.history.domain.enumeration.SYMBOL;
import de.ginisolutions.trader.trading.domain.Strategist;
import de.ginisolutions.trader.trading.domain.StrategistPackage;
import de.ginisolutions.trader.trading.domain.enumeration.STRATEGY;
import de.ginisolutions.trader.trading.event.TradingInit;
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

    private static final Logger log = LoggerFactory.getLogger(StrategistManager.class);

    private final ApplicationEventPublisher eventPublisher;

    private final StrategistRepository strategistRepository;

    private final Map<String, List<StrategistPackage>> strategistMap;

    /**
     * Constructor for StrategistManager. Receives ApplicationEventPublisher via dependency injection.
     *
     * @param eventPublisher the eventPublisher, used for publishing an "Initialisation finished event"
     */
    public StrategistManager(ApplicationEventPublisher eventPublisher, StrategistRepository strategistRepository) {
        log.info("Constructing StrategistManager");
        this.eventPublisher = eventPublisher;
        this.strategistRepository = strategistRepository;
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
        log.info("Initializing StrategistManager");
        if (init.isHistoryManager()) {
            // TODO get all strategist from database and create a package for each
            init.setHistoryManager();
            this.eventPublisher.publishEvent(init);
        }
    }

    /**
     * Persist all strategists
     */
//    @Scheduled(fixedDelay = 1000) // TODO user env variable
    private void persist() {
        this.strategistMap.forEach((s, strategistPackages) -> {
            strategistPackages.forEach(strategistPackage -> {
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
    public void subscribe2strategist(MARKET market, SYMBOL symbol, INTERVAL interval, STRATEGY strategy, StrategyParameter parameters, TickListener listener) {
        // TODO simplify after testing
        final String key = market.toString() + symbol.toString() + interval.toString() + strategy.toString();
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
            }
        }
        // create a new strategist, subscribe to it and save it in new list
        else {
            final Strategist strategist = this.strategistRepository.save(new Strategist(strategy, market, symbol, interval, parameters));
            final StrategistPackage newStrategistPackage = new StrategistPackage(strategist, new HistoryProvider());
            this.strategistMap.put(key, List.of(newStrategistPackage));
        }
    }

}
