package de.ginisolutions.trader.trading.messaging;

import de.ginisolutions.trader.trading.domain.enumeration.SIGNAL;

public class SignalMessage {

    final SIGNAL signal;

    public SignalMessage(SIGNAL signal) {
        this.signal = signal;
    }

    public SIGNAL getSignal() {
        return signal;
    }
}
