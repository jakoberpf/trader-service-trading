package de.ginisolutions.trader.trading.domain;

import de.ginisolutions.trader.trading.domain.enumeration.SIGNAL;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class Trade {

    @NotNull
    private final LocalDateTime timestamp;

    @NotNull
    private final SIGNAL signal;

    @NotNull
    private final Double price;

    public Trade(@NotNull LocalDateTime timestamp, @NotNull SIGNAL signal, @NotNull Double price) {
        this.timestamp = timestamp;
        this.signal = signal;
        this.price = price;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public SIGNAL getSignal() {
        return signal;
    }

    public Double getPrice() {
        return price;
    }
}
