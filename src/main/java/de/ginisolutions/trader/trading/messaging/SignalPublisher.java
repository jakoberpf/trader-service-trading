package de.ginisolutions.trader.trading.messaging;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.config.IBusConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignalPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignalPublisher.class);

    private final MBassador<Signal> bus;

    /**
     * TODO
     */
    public SignalPublisher() {
        LOGGER.info("Constructing SignalPublisher");
        IBusConfiguration configuration;
        this.bus = new MBassador<>();
    }

    /**
     * This method subscribes the provided listener to the event bus
     * @param listener defines the listener to subscribe to the event bus
     */
    public void subscribe(SignalListener listener) {
        LOGGER.info("Subscribing new listener: " + listener);
        this.bus.subscribe(listener);
    }

    /**
     * This method unsubscribes the provided listener from the event bus
     * @param listener defines the listener to unsubscribe from the event bus
     */
    public void unsubscribe(SignalListener listener) {
        LOGGER.info("Unsubscribing listener: " + listener);
        this.bus.unsubscribe(listener);
    }

    /**
     * @param signal
     * @param aync
     * @return
     */
    public boolean publishSignal(Signal signal, boolean aync) {
        if (aync) {
            this.bus.post(signal).asynchronously();
        } else {
            this.publishSignal(signal);
        }
        return true;
    }

    /**
     * @param signal
     * @return
     */
    public boolean publishSignal(Signal signal) {
        this.bus.post(signal).now();
        return true;
    }
}
