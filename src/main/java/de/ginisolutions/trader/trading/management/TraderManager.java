package de.ginisolutions.trader.trading.management;

import de.ginisolutions.trader.common.market.AccountImplFactory;
import de.ginisolutions.trader.trading.domain.Trader;
import de.ginisolutions.trader.trading.domain.TraderPackage;
import de.ginisolutions.trader.trading.event.TradingInit;
import de.ginisolutions.trader.trading.repository.TraderRepository;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
@Service
public class TraderManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(TraderManager.class);

    private final ApplicationEventPublisher eventPublisher;

    private final TraderRepository traderRepository;

    private final StrategistManager strategistManager;

    private final List<TraderPackage> traderPackageList;

    public TraderManager(ApplicationEventPublisher eventPublisher, TraderRepository traderRepository, StrategistManager strategistManager) {
        LOGGER.info("Constructing TraderManager");
        this.eventPublisher = eventPublisher;
        this.traderRepository = traderRepository;
        this.strategistManager = strategistManager;
        this.traderPackageList = new ArrayList<>();
    }

    /**
     * TODO
     */
    public void init() {
        LOGGER.info("Initializing TraderManager");
        traderRepository.findAll().forEach(trader -> {
            LOGGER.debug("Initializing Trader: {}", trader);
            final TraderPackage newTraderPackage = new TraderPackage(trader,
                AccountImplFactory.buildAccount(trader.getMarket(), trader.getApiKey(), trader.getApiSecret()));
            this.strategistManager.subscribe(trader.getMarket(), trader.getSymbol(), trader.getInterval(), trader.getStrategy(), newTraderPackage);
            this.traderPackageList.add(newTraderPackage);
        });
        LOGGER.info("Initialisation of TraderManager complete");
    }

    /**
     * Persist all traders
     */
    @Scheduled(fixedDelay = 20000) // TODO user env variable
    private void persist() {
        this.traderPackageList.forEach(traderPackage -> {
            LOGGER.debug("Persisting trader: {}", traderPackage.getTrader());
            this.traderRepository.save(traderPackage.getTrader());
        });
    }

    /**
     * @param trader
     * @return
     */
    public Trader add(Trader trader) {
        LOGGER.debug("Adding Trader : {}", trader);
        if (trader.getId() != null) {
            throw new IllegalArgumentException("A new trader cannot already have an ID");
        }
        // persist trader
        trader = this.traderRepository.save(trader);
        // add package
//        try {
        final TraderPackage newTraderPackage = new TraderPackage(trader,
            AccountImplFactory.buildAccount(trader.getMarket(), trader.getApiKey(), trader.getApiSecret()));
        this.strategistManager.subscribe(trader.getMarket(), trader.getSymbol(), trader.getInterval(), trader.getStrategy(), newTraderPackage);
        this.traderPackageList.add(newTraderPackage);
//        } catch (Exception exception) {
//            this.traderRepository.delete(trader);
//            throw new RuntimeException("Unable to add trader, rolling back repository", exception);
//        }
        LOGGER.debug("Adding Trader successful: {}", trader);
        return trader;
    }

    /**
     * @param trader
     * @return
     */
    public Trader edit(Trader trader) {
        LOGGER.debug("Editing Trader : {}", trader);
        if (trader.getId() == null) {
            throw new IllegalArgumentException("A trader must have id to be edited in manager");
        }
        // remove old package
        Trader current = trader;
        this.traderPackageList.stream().filter(traderPackage ->
            traderPackage.getTrader().getId().equals(current.getId())).findFirst().ifPresentOrElse(traderPackage -> {
            LOGGER.debug("Found trader to edit, unsubscribing and removing package");
            this.strategistManager.unsubscribe(traderPackage.getTrader().getMarket(), traderPackage.getTrader().getSymbol(), traderPackage.getTrader().getInterval(), traderPackage.getTrader().getStrategy(), traderPackage);
            this.traderPackageList.remove(traderPackage);
        }, () -> {
            throw new RuntimeException("Trader to edit not found");
        });
        // add updated package
        final TraderPackage newTraderPackage = new TraderPackage(trader,
            AccountImplFactory.buildAccount(trader.getMarket(), trader.getApiKey(), trader.getApiSecret()));
        this.strategistManager.subscribe(trader.getMarket(), trader.getSymbol(), trader.getInterval(), trader.getStrategy(), newTraderPackage);
        this.traderPackageList.add(newTraderPackage);
        // persist trader
        trader = this.traderRepository.save(trader);
        LOGGER.debug("Adding Trader successful: {}", trader);
        return trader;
    }

    /**
     * @param trader
     * @return
     */
    public Trader remove(Trader trader) {
        LOGGER.debug("Removing Trader : {}", trader);
        // remove package
        Trader current = trader;
        this.traderPackageList.stream().filter(traderPackage ->
            traderPackage.getTrader().getId().equals(current.getId())).findFirst().ifPresent(this.traderPackageList::remove);
        // truncate
        this.traderRepository.delete(trader);
        return trader;
    }

    /**
     * @return the number of trader packages (running) in the list
     */
    public int count() {
        return traderPackageList.size();
    }
}
