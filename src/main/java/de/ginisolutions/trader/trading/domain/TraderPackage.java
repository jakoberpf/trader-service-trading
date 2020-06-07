package de.ginisolutions.trader.trading.domain;

import de.ginisolutions.trader.common.market.AccountImpl;
import de.ginisolutions.trader.common.market.AccountImplFactory;
import de.ginisolutions.trader.trading.messaging.SignalMessage;
import de.ginisolutions.trader.trading.messaging.SignalListener;
import net.engio.mbassy.listener.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.text.DecimalFormat;

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

    public TraderPackage(@NotNull Trader trader, @NotNull AccountImpl accountImpl) {
        this.trader = trader;
        this.accountImpl = AccountImplFactory.buildAccount(trader.getMarket(), trader.getApiKey(), trader.getApiSecret());
    }

    /**
     *
     * @param signalMessage
     */
    @Handler
    private void handleSignal(@NotNull SignalMessage signalMessage) {
        LOGGER.warn("Got SIGNAL");
        if (signalMessage.getSignal().equals(ENTER)) {
            LOGGER.warn("Got ENTER");
            this.accountImpl.makeOrder(trader.getSymbol(), getTrader().getBudget(), "buy"); // TODO implement enum for buy/sell
        }
        if (signalMessage.getSignal().equals(EXIT)) {
            LOGGER.warn("Got EXIT");
            this.accountImpl.makeOrder(trader.getSymbol(), getTrader().getBudget(), "sell"); // TODO implement enum for buy/sell
        }
        // TODO save action to history
    }

    /**
     *
     * @param value
     * @return
     */
    public static double filterAmount(double value) {
        // TODO check with filters from market, like min/max or step
        DecimalFormat df = new DecimalFormat("#.#######");
        return Double.parseDouble(df.format(value).replace(",", "."));
    }

    public Trader getTrader() {
        return trader;
    }

    public String getTraderOwner() {
        return trader.getOwner();
    }
}
