package de.ginisolutions.trader.trading.domain;

import de.ginisolutions.trader.common.messaging.BaseListener;
import de.ginisolutions.trader.common.messaging.TickListener;
import de.ginisolutions.trader.common.strategy.StrategyFactory;
import de.ginisolutions.trader.history.domain.TickDTO;
import de.ginisolutions.trader.trading.domain.enumeration.SIGNAL;
import de.ginisolutions.trader.trading.messaging.SignalListener;
import de.ginisolutions.trader.trading.messaging.Signal;
import de.ginisolutions.trader.trading.messaging.SignalPublisher;
import de.ginisolutions.trader.trading.management.HistoryProvider;
import net.engio.mbassy.listener.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.*;
import org.ta4j.core.num.PrecisionNum;

import javax.validation.constraints.NotNull;
import java.time.*;

/**
 * The StrategistPackage contains the Strategist entity and some simple business logic.
 * It is instantiate for every strategist in the database at application startup and handles
 * the running of the strategy defined in the strategist. I consumes tick events, when a
 * new tick is published by the history service or the internal crawler (which ever comes first)
 */
public class StrategistPackage extends TickListener {

    private static final Logger log = LoggerFactory.getLogger(StrategistPackage.class);

    @NotNull
    private final Strategist strategist;

    @NotNull
    private final SignalPublisher publisher;

    @NotNull
    private final Strategy strategy;

    @NotNull
    private final BarSeries barSeries;

    @NotNull
    private final TradingRecord tradingRecord;

    public StrategistPackage(@NotNull Strategist strategist, @NotNull HistoryProvider historyProvider) {
        this.strategist = strategist;
        this.publisher = new SignalPublisher();
        this.barSeries = historyProvider.getInitialBarSeries(strategist.getMarket(), strategist.getSymbol(), strategist.getInterval());
        this.strategy = StrategyFactory.buildStrategy(this.barSeries, strategist.getStrategy(), strategist.getParameters());
        this.tradingRecord = new BaseTradingRecord();
    }

    /**
     * @param tickDTO
     */
    @Handler
    public void handleTick(TickDTO tickDTO) {
        log.debug("Received tick message");
        if (tickDTO.getMarket().equals(this.strategist.getMarket()) &&
            tickDTO.getSymbol().equals(this.strategist.getSymbol()) &&
            tickDTO.getInterval().equals(this.strategist.getInterval())) {
            if (ZonedDateTime.ofInstant(Instant.ofEpochMilli(tickDTO.getOpenTime()), ZoneId.systemDefault()).isAfter(this.barSeries.getLastBar().getEndTime())) {
                this.barSeries.addBar(
                    Duration.ofMillis(tickDTO.getInterval().getInterval() - 1),
                    ZonedDateTime.ofInstant(Instant.ofEpochMilli(tickDTO.getCloseTime()), ZoneId.systemDefault()),
                    tickDTO.getOpen(),
                    tickDTO.getHigh(),
                    tickDTO.getLow(),
                    tickDTO.getClose(),
                    tickDTO.getVolume()
                );
                log.info("Added tick to bar series -> " + tickDTO.toString());
                this.decide();
            } else {
                // TODO update last bar
                // log.info("Updated last bar with tick -> " + tickDTO.toString());
            }
        }
    }

    /**
     *
     */
    public void decide() {
        log.debug("Decision process...");
        final Bar newBar = this.barSeries.getLastBar();
        final int endIndex = this.barSeries.getEndIndex();
        if (strategy.shouldEnter(endIndex)) {
            tradingRecord.enter(endIndex, newBar.getClosePrice(), PrecisionNum.valueOf(1000)); // TODO externalise value 1000 into application.properties
            this.publisher.publishSignal(new Signal(LocalDateTime.now(), SIGNAL.ENTER));
            // TODO add entry to strategist log
        }
        if (strategy.shouldExit(endIndex)) {
            tradingRecord.exit(endIndex, newBar.getClosePrice(), PrecisionNum.valueOf(1000)); // TODO externalise value 1000 into application.properties
            this.publisher.publishSignal(new Signal(LocalDateTime.now(), SIGNAL.EXIT));
            // TODO add entry to strategist log
        }
    }

    /**
     * @param listener
     */
    public void subscribe(SignalListener listener) {
        log.debug("Subscribing new listener: " + listener);
        this.publisher.subscribe(listener);
    }

    /**
     * @param listener
     */
    public void unsubscribe(SignalListener listener) {
        log.debug("Unsubscribing new listener: " + listener);
        this.publisher.unsubscribe(listener);
    }

    /**
     * @return the strategist
     */
    public Strategist getStrategist() {
        return strategist;
    }
}
