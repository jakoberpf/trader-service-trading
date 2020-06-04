package de.ginisolutions.trader.trading.domain;

import de.ginisolutions.trader.common.market.AccountImpl;
import de.ginisolutions.trader.common.messaging.BaseListener;
import de.ginisolutions.trader.trading.messaging.SignalMessage;
import net.engio.mbassy.listener.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

import static de.ginisolutions.trader.trading.domain.enumeration.SIGNAL.ENTER;
import static de.ginisolutions.trader.trading.domain.enumeration.SIGNAL.EXIT;

/**
 * The TraderPackage entity contains the Trader entity and some simple business logic.
 * Is is instantiated for every Trader in the database at application startup and handles
 * the business logic of entering/exciting assets.
 */
public class TraderPackage implements BaseListener {

    private static final Logger log = LoggerFactory.getLogger(TraderPackage.class);

    @NotNull
    private final Trader trader;

    @NotNull
    private final AccountImpl accountImpl;

    public TraderPackage(@NotNull Trader trader, @NotNull AccountImpl accountImpl) {
        this.trader = trader;
        this.accountImpl = accountImpl;
    }

    @Handler
    private void handleSignal(@NotNull SignalMessage signalMessage) {
        log.warn("Got SIGNAL");
        if (signalMessage.getSignal().equals(ENTER)) {
            log.warn("Got ENTER");
        }
        if (signalMessage.getSignal().equals(EXIT)) {
            log.warn("Got EXIT");
        }
        // TODO enter market with budget
        // TODO save action to history
    }

    public Trader getTrader() {
        return trader;
    }

    public String getTraderOwner() {
        return trader.getOwner();
    }
}
