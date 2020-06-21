package de.ginisolutions.trader.trading.domain;

import de.ginisolutions.trader.common.market.AccountImpl;
import de.ginisolutions.trader.common.market.AccountImplFactory;
import de.ginisolutions.trader.trading.messaging.SignalListener;
import de.ginisolutions.trader.trading.messaging.SignalMessage;
import net.engio.mbassy.listener.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static de.ginisolutions.trader.trading.domain.enumeration.ORDER.BUY;
import static de.ginisolutions.trader.trading.domain.enumeration.ORDER.SELL;
import static de.ginisolutions.trader.trading.domain.enumeration.SIGNAL.ENTER;
import static de.ginisolutions.trader.trading.domain.enumeration.SIGNAL.EXIT;

/**
 * The TraderPackage entity contains the Trader entity and some simple business logic.
 * Is is instantiated for every Trader in the database at application startup and handles
 * the business logic of entering/exciting assets.
 */
public class TraderPackage implements SignalListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TraderPackage.class);

    @NotNull
    private final Trader trader;

    @NotNull
    private final AccountImpl accountImpl;

    public TraderPackage(@NotNull Trader trader) {
        this.trader = trader;
        this.accountImpl = AccountImplFactory.buildAccount(trader.getMarket(), trader.getApiKey(), trader.getApiSecret());
    }

    /**
     * @param signalMessage
     */
    @Handler
    @SuppressWarnings("unused")
    private void handleSignal(SignalMessage signalMessage) {
        LOGGER.warn("Got SIGNAL {}", signalMessage.toString());
        if (trader.isLive()) {
            if (signalMessage.getSignal().equals(ENTER) && !trader.isIn()) {
                LOGGER.info("I should ENTER");
                if (!trader.isIn()) {
                    try {
                        this.accountImpl.makeOrder(trader.getSymbol(), getTrader().getBudget(), BUY);
                        this.trader.getTradeHistory().add(new Trade(LocalDateTime.now(), ENTER, BUY, 0D, trader.getBudget())); // TODO get price and quantity from OrderResponse
                    } catch (Exception e) {
                        LOGGER.warn(e.getMessage());
                        throw e;
                    }
                    trader.setIn(true);
                } else {
                    LOGGER.info("Trader already entered, no order will be placed");
                }
            }
            if (signalMessage.getSignal().equals(EXIT) && trader.isIn()) {
                LOGGER.info("I should EXIT");
                if (trader.isIn()) {
                    try {
                        this.accountImpl.makeOrder(trader.getSymbol(), getTrader().getBudget(), SELL);
                        this.trader.getTradeHistory().add(new Trade(LocalDateTime.now(), EXIT, SELL, 0D, trader.getBudget())); // TODO get price and quantity from OrderResponse
                    } catch (Exception e) {
                        LOGGER.warn(e.getMessage());
                        throw e;
                    }
                    trader.setIn(false);
                } else {
                    LOGGER.info("Trader already exited, no order will be placed");
                }
            }
        }
    }

    public Trader getTrader() {
        return trader;
    }

    public String getTraderOwner() {
        return trader.getOwner();
    }
}
