package de.ginisolutions.trader.trading.messaging;

import de.ginisolutions.trader.trading.domain.enumeration.SIGNAL;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class Signal {

    @NotNull
    private final LocalDateTime timestamp;

    @NotNull
    private final SIGNAL signal;

    public Signal(@NotNull LocalDateTime timestamp, @NotNull SIGNAL signal) {
        this.timestamp = timestamp;
        this.signal = signal;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public SIGNAL getSignal() {
        return signal;
    }
}
