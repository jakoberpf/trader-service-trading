package de.ginisolutions.trader.trading.management;

import com.binance.api.client.domain.account.NewOrderResponse;
import de.ginisolutions.trader.common.enumeration.ORDER;
import de.ginisolutions.trader.common.enumeration.SIGNAL;
import de.ginisolutions.trader.common.market.AccountImpl;
import de.ginisolutions.trader.common.market.AccountImplFactory;
import de.ginisolutions.trader.common.messaging.SignalListener;
import de.ginisolutions.trader.common.messaging.SignalMessage;
import de.ginisolutions.trader.trading.domain.Trader;
import de.ginisolutions.trader.trading.domain.model.Trade;
import net.engio.mbassy.listener.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static de.ginisolutions.trader.common.enumeration.ORDER.BUY;
import static de.ginisolutions.trader.common.enumeration.ORDER.SELL;
import static de.ginisolutions.trader.common.enumeration.SIGNAL.ENTER;
import static de.ginisolutions.trader.common.enumeration.SIGNAL.EXIT;

/**
 * The TraderPackage entity contains the Trader entity and some simple business logic.
 * Is is instantiated for every Trader in the database at application startup and handles
 * the business logic of entering/exciting assets.
 *
 * @author <a href="mailto:contact@jakoberpf.de">Jakob Erpf</a>
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
    private SignalMessage handleSignal(SignalMessage signalMessage) {
        LOGGER.warn("Trader " + trader.getId() + " got SIGNAL {}", signalMessage.toString());
        if (trader.isLive()) {
            if (signalMessage.getSignal().equals(ENTER)) {
                LOGGER.info("Trader " + trader.getId() + " should ENTER");
                if (!trader.isIn()) {
                    return makeOrder(signalMessage, BUY, ENTER);
                } else {
                    LOGGER.info("Trader already entered, no order will be placed");
                    return null;
                }
            }
            if (signalMessage.getSignal().equals(EXIT)) {
                LOGGER.info("I should EXIT");
                if (trader.isIn()) {
                    return makeOrder(signalMessage, SELL, EXIT);
                } else {
                    LOGGER.info("Trader already exited, no order will be placed");
                    return null;
                }
            } else {
                throw new IllegalArgumentException("Signal not supported");
            }
        } else {
            LOGGER.debug("Trader is not live, no order will be placed");
            return null;
        }
    }

    /**
     * @param signalMessage
     * @param order
     * @param signal
     * @return
     */
    private SignalMessage makeOrder(SignalMessage signalMessage, ORDER order, SIGNAL signal) {
        try {
            // TODO get balance
            final NewOrderResponse orderResponse = this.accountImpl.makeOrder(trader.getSymbol(), getTrader().getBudget(), order);
            this.trader.getTradeHistory().add(new Trade(LocalDateTime.now(), signal, order, Double.valueOf(orderResponse.getPrice()), trader.getBudget()));
            switch (signal) {
                case ENTER:
                    trader.setIn(true);
                    break;
                case EXIT:
                    trader.setIn(false);
                    break;
            }
            return signalMessage;
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            throw e;
        }
    }

    public Trader getTrader() {
        return trader;
    }

    public String getTraderOwner() {
        return trader.getOwner();
    }
}
