package de.ginisolutions.trader.trading.domain;

import de.ginisolutions.trader.trading.domain.enumeration.ORDER;
import de.ginisolutions.trader.trading.domain.enumeration.SIGNAL;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class Trade {

    @NotNull
    private final LocalDateTime timestamp;

    @NotNull
    private final SIGNAL signal;

    @NotNull
    private final ORDER order;

    @NotNull
    private final Double price;

    @NotNull
    private final Double amount;

    public Trade(@NotNull LocalDateTime timestamp, @NotNull SIGNAL signal, @NotNull ORDER order, @NotNull Double price, @NotNull Double amount) {
        this.timestamp = timestamp;
        this.signal = signal;
        this.order = order;
        this.price = price;
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public SIGNAL getSignal() {
        return signal;
    }

    public ORDER getOrder() {
        return order;
    }

    public Double getPrice() {
        return price;
    }

    public Double getAmount() {
        return amount;
    }
}
