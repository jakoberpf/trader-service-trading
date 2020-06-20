package de.ginisolutions.trader.trading.domain;

import de.ginisolutions.trader.common.messaging.TickListener;
import de.ginisolutions.trader.common.strategy.StrategyFactory;
import de.ginisolutions.trader.history.domain.TickPackage;
import de.ginisolutions.trader.trading.domain.enumeration.SIGNAL;
import de.ginisolutions.trader.trading.messaging.SignalListener;
import de.ginisolutions.trader.trading.messaging.SignalMessage;
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
public class StrategistPackage implements TickListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(StrategistPackage.class);

    @NotNull
    private final Strategist strategist;

    @NotNull
    private final SignalPublisher publisher;

    @NotNull
    private final Strategy strategy;

    @NotNull
    private final BarSeries barSeries;

    @NotNull
    private TradingRecord tradingRecord;

    public StrategistPackage(@NotNull Strategist strategist, @NotNull HistoryProvider historyProvider) {
        this.strategist = strategist;
        this.publisher = new SignalPublisher();
        this.barSeries = historyProvider.getInitialBarSeries(strategist.getMarket(), strategist.getSymbol(), strategist.getInterval());
        this.tradingRecord = new BaseTradingRecord();
        this.strategy = StrategyFactory.buildStrategy(this.barSeries, strategist.getStrategy(), strategist.getParameters());
    }

    /**
     * @param tickPackage
     */
    @Handler
    public void handleTick(TickPackage tickPackage) {
        LOGGER.debug("Received tick message");
        if (tickPackage.getMarket().equals(this.strategist.getMarket()) &&
            tickPackage.getSymbol().equals(this.strategist.getSymbol()) &&
            tickPackage.getInterval().equals(this.strategist.getInterval())) {
            if (ZonedDateTime.ofInstant(Instant.ofEpochMilli(tickPackage.getOpenTime()), ZoneId.systemDefault()).isAfter(this.barSeries.getLastBar().getEndTime())) {
                this.barSeries.addBar(
                    Duration.ofMillis(tickPackage.getInterval().getInterval() - 1),
                    ZonedDateTime.ofInstant(Instant.ofEpochMilli(tickPackage.getCloseTime()), ZoneId.systemDefault()),
                    tickPackage.getOpen(),
                    tickPackage.getHigh(),
                    tickPackage.getLow(),
                    tickPackage.getClose(),
                    tickPackage.getVolume()
                );
                LOGGER.info("Added tick to bar series -> " + tickPackage.toString());
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
        final Bar newBar = this.barSeries.getLastBar();
        final int endIndex = this.barSeries.getEndIndex();
        if (strategy.shouldEnter(endIndex)) {
            LOGGER.debug("Strategy should ENTER");
            this.tradingRecord.enter(endIndex, newBar.getClosePrice(), PrecisionNum.valueOf(1000));
            this.publisher.publishSignal(new SignalMessage(SIGNAL.ENTER, strategist.getMarket(), strategist.getSymbol(), strategist.getInterval(), strategist.getStrategy()), false);
        }
        if (strategy.shouldExit(endIndex)) {
            LOGGER.debug("Strategy should EXIT");
            this.tradingRecord.exit(endIndex, newBar.getClosePrice(), PrecisionNum.valueOf(1000));
            this.publisher.publishSignal(new SignalMessage(SIGNAL.EXIT, strategist.getMarket(), strategist.getSymbol(), strategist.getInterval(), strategist.getStrategy()), false);
        }
    }

    /**
     * @param listener
     */
    public void subscribe(SignalListener listener) {
        LOGGER.debug("Subscribing new listener: " + listener);
        this.publisher.subscribe(listener);
    }

    /**
     * @param listener
     */
    public void unsubscribe(SignalListener listener) {
        LOGGER.debug("Unsubscribing new listener: " + listener);
        this.publisher.unsubscribe(listener);
    }

    /**
     * @return the strategist
     */
    public Strategist getStrategist() {
        return strategist;
    }
}
