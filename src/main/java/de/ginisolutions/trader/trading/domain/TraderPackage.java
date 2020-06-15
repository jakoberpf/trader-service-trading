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
     *
     * @param signalMessage
     */
    @Handler
    private void handleSignal(SignalMessage signalMessage) {
        LOGGER.warn("Got SIGNAL {}", signalMessage);
        if (trader.isLive()) {
            if (signalMessage.getSignal().equals(ENTER) && !trader.isIn()) {
                LOGGER.warn("Got ENTER");
                try {
                    this.accountImpl.makeOrder(trader.getSymbol(), getTrader().getBudget(), BUY);
                    this.trader.getTradeHistory().add(new Trade(LocalDateTime.now(), ENTER, BUY, 0D, trader.getBudget())); // TODO get price and quantity from OrderResponse
                } catch (Exception e) {
                    LOGGER.warn(e.getMessage());
                    throw e;
                }
            }
            if (signalMessage.getSignal().equals(EXIT) && trader.isIn()) {
                LOGGER.warn("Got EXIT");
                try {
                    this.accountImpl.makeOrder(trader.getSymbol(), getTrader().getBudget(), SELL);
                    this.trader.getTradeHistory().add(new Trade(LocalDateTime.now(), EXIT, SELL, 0D, trader.getBudget())); // TODO get price and quantity from OrderResponse
                } catch (Exception e) {
                    LOGGER.warn(e.getMessage());
                    throw e;
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
