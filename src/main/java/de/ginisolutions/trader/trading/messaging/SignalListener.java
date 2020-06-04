package de.ginisolutions.trader.trading.messaging;

import de.ginisolutions.trader.history.domain.TickDTO;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import net.engio.mbassy.listener.References;

@Listener(references = References.Strong)
public class SignalListener {

    /**
     * TODO
     * @param tickDTO
     */
//    @Handler(delivery = Invoke.Asynchronously)
    @Handler()
    public void expensiveOperation(TickDTO tickDTO) {
        // TODO handle tack accordingly
    }

    /**
     * TODO
     * @param tick
     */
    private void publishTickEvent(TickDTO tick) {
        // TODO publish new "final" tick event to the trading service
    }
}
