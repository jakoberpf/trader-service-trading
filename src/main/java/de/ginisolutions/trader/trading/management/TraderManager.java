package de.ginisolutions.trader.trading.management;

import de.ginisolutions.trader.common.market.AccountImplFactory;
import de.ginisolutions.trader.trading.domain.Trader;
import de.ginisolutions.trader.trading.domain.TraderPackage;
import de.ginisolutions.trader.trading.event.TradingInit;
import de.ginisolutions.trader.trading.repository.TraderRepository;
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

    private static final Logger log = LoggerFactory.getLogger(StrategistManager.class);

    private final ApplicationEventPublisher eventPublisher;

    private final TraderRepository traderRepository;

    private final StrategistManager strategistManager;

    private final List<TraderPackage> traderPackageList;

    public TraderManager(ApplicationEventPublisher eventPublisher, TraderRepository traderRepository, StrategistManager strategistManager) {
        log.info("Constructing TraderManager");
        this.eventPublisher = eventPublisher;
        this.traderRepository = traderRepository;
        this.strategistManager = strategistManager;
        this.traderPackageList = new ArrayList<>();
    }

    /**
     * TODO
     *
     * @param init
     */
    @EventListener()
    public void init(TradingInit init) {
        log.info("Initializing TraderManager");
        if (init.isHistoryManager() && init.isStrategistManager()) {
            traderRepository.findAll().forEach(trader -> {
                final TraderPackage newTraderPackage = new TraderPackage(trader,
                    AccountImplFactory.buildAccount(trader.getMarket(), trader.getApiKey(), trader.getApiSecret()));
                this.strategistManager.subscribe2strategist(trader.getMarket(), trader.getSymbol(), trader.getInterval(), trader.getStrategy(), newTraderPackage);
                this.traderPackageList.add(newTraderPackage);
            });
            log.info("Initialisation of TraderManager complete");
            init.setTraderManager();
            this.eventPublisher.publishEvent(init);
        }
    }

    /**
     * Persist all traders
     */
    @Scheduled(fixedDelay = 1000) // TODO user env variable
    private void persist() {
        this.traderPackageList.forEach(traderPackage -> {
            this.traderRepository.save(traderPackage.getTrader());
        });
    }

    /**
     * @param trader
     * @return
     */
    public Trader add(Trader trader) {
        // persist trader
        trader = this.traderRepository.save(trader);
        // add package
        this.traderPackageList.add(new TraderPackage(trader,
            AccountImplFactory.buildAccount(trader.getMarket(), trader.getApiKey(), trader.getApiSecret())));
        return trader;
    }

    /**
     * @param trader
     * @return
     */
    public Trader edit(Trader trader) {
        if (trader.getId() == null) {
            throw new IllegalArgumentException("Trader must have id to be edited in manager");
        }
        // remove old package
        Trader current = trader;
        this.traderPackageList.stream().filter(traderPackage ->
            traderPackage.getTrader().getId().equals(current.getId())).findFirst().ifPresent(this.traderPackageList::remove);
        // add updated package
        this.traderPackageList.add(new TraderPackage(trader,
            AccountImplFactory.buildAccount(trader.getMarket(), trader.getApiKey(), trader.getApiSecret())));
        // persist trader
        trader = this.traderRepository.save(trader);
        return trader;
    }

    /**
     * @param trader
     * @return
     */
    public Trader remove(Trader trader) {
        // remove package
        Trader current = trader;
        this.traderPackageList.stream().filter(traderPackage ->
            traderPackage.getTrader().getId().equals(current.getId())).findFirst().ifPresent(this.traderPackageList::remove);
        // truncate
        this.traderRepository.delete(trader);
        return trader;
    }
}
