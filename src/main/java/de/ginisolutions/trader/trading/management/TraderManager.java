package de.ginisolutions.trader.trading.management;

import de.ginisolutions.trader.trading.domain.Trader;
import de.ginisolutions.trader.trading.domain.TraderPackage;
import de.ginisolutions.trader.trading.repository.TraderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            final TraderPackage newTraderPackage = new TraderPackage(trader);
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
        final TraderPackage newTraderPackage = new TraderPackage(trader);
        this.strategistManager.subscribe(trader.getMarket(), trader.getSymbol(), trader.getInterval(), trader.getStrategy(), newTraderPackage);
        this.traderPackageList.add(newTraderPackage);
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
        final TraderPackage newTraderPackage = new TraderPackage(trader);
        this.strategistManager.subscribe(trader.getMarket(), trader.getSymbol(), trader.getInterval(), trader.getStrategy(), newTraderPackage);
        this.traderPackageList.add(newTraderPackage);
        // persist trader
        trader = this.traderRepository.save(trader);
        LOGGER.debug("Adding Trader successful: {}", trader);
        return trader;
    }

    /**
     * @param id
     * @return
     */
    public Trader get(String id) {
        final Optional<TraderPackage> optional = this.traderPackageList.stream().filter(traderPackage ->
            traderPackage.getTrader().getId().equals(id)).findAny();
        if (optional.isEmpty()) {
            throw new RuntimeException("Trader not found");
        }
        return optional.get().getTrader();
    }

    /**
     * @param id
     * @return
     */
    public String remove(String id) {
        LOGGER.debug("Removing Trader : {}", id);
        // remove package
        this.traderPackageList.stream().filter(traderPackage ->
            traderPackage.getTrader().getId().equals(id)).findFirst().ifPresent(this.traderPackageList::remove);
        // truncate
        this.traderRepository.deleteById(id);
        return id;
    }

    /**
     * @return the number of trader packages (running) in the list
     */
    public int count() {
        return traderPackageList.size();
    }
}
