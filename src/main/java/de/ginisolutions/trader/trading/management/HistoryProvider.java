package de.ginisolutions.trader.trading.management;

import de.ginisolutions.trader.common.market.MarketImpl;
import de.ginisolutions.trader.common.market.MarketImplFactory;
import de.ginisolutions.trader.history.domain.TickDTO;
import de.ginisolutions.trader.history.domain.enumeration.INTERVAL;
import de.ginisolutions.trader.history.domain.enumeration.MARKET;
import de.ginisolutions.trader.history.domain.enumeration.SYMBOL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.num.PrecisionNum;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class HistoryProvider {

    private static final Logger log = LoggerFactory.getLogger(HistoryManager.class);

    /**
     * This method will return an short init bar series for initializing a strategy
     * @param market defines the market
     * @param symbol defines the stock
     * @param interval defines the time interval
     * @return BarSeries for the defines enums
     */
    public BarSeries getInitialBarSeries(final MARKET market, final SYMBOL symbol, final INTERVAL interval) {
        // TODO Use FeignClient if available
        return this.getBarSeries(market, symbol, interval, 200); // TODO extract length parameter to application.properties
    }

    /**
     * This method will return an bar series
     * @param market defines the market
     * @param symbol defines the stock
     * @param interval defines the time interval
     * @param maxLength defines the maximum number of bar in the bar series
     * @return BarSeries for the defines enums and length
     */
    public BarSeries getBarSeries(final MARKET market, final SYMBOL symbol, final INTERVAL interval, final int maxLength) {
        // get tick from database or market
        final List<TickDTO> ticks = this.getTickList(market, symbol, interval);
        // convert ticks to bar series and return
        return this.convertTickListBarSeries(market, symbol, ticks);
    }

    /**
     * This method request the tick from either the database or the market api. Currently it just does the market api,
     * but in the future it  is the goal to be able to distinguish between requests which should and can be handles by
     * the database and which are for the market apis. It also should be able to function as a fail safe, so in case the
     * database or the market api is down, it will tr to fetch the date from the other source.
     * @param market defines the market
     * @param symbol defines the stock
     * @param interval defines the time interval
     * @return a list of ticks
     */
    public List<TickDTO> getTickList(MARKET market, SYMBOL symbol, INTERVAL interval) {
        switch (market) {
            case BINANCE:
                final MarketImpl api = MarketImplFactory.buildApi(market);
                return api.getCandlesticks(symbol, interval);
            // TODO implement more market crawlers
            default:
                throw new IllegalArgumentException("Exchange with name " + market.getName() + " is not implemented yet");
        }
    }

    /**
     * This method converts the list of tick, recieved from the database or markets into a bar series
     * @param market defines the market
     * @param symbol defines the stock
     * @param ticks a list of ticks
     * @return BarSeries from the ticks and with the name of the market and symbol
     */
    public BarSeries convertTickListBarSeries(final MARKET market, final SYMBOL symbol, final List<TickDTO> ticks) {
        log.debug("Building BarSeries for " + market.getName() + " " + symbol.getNameUpper() + " from TickList");
        BarSeries barSeries = new BaseBarSeriesBuilder()
            .withName(market.getName() + "-" + symbol.getNameUpper())
            .withNumTypeOf(PrecisionNum.class)
            .build();
        for (TickDTO tick : ticks) {
            barSeries.addBar(
                Duration.ofMillis(tick.getInterval().getInterval() - 1),
                ZonedDateTime.ofInstant(Instant.ofEpochMilli(tick.getCloseTime()), ZoneId.systemDefault()),
                tick.getOpen(),
                tick.getHigh(),
                tick.getLow(),
                tick.getClose(),
                tick.getVolume()
            );
        }
        log.debug("Finished build of " + barSeries.getName() + " with " + barSeries.getBarCount() + " bar/ticks");
        return barSeries;
    }
}
