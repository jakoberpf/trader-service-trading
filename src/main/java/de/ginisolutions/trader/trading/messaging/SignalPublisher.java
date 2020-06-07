package de.ginisolutions.trader.trading.messaging;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.config.IBusConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignalPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignalPublisher.class);

    private final MBassador<SignalMessage> bus;

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
        LOGGER.debug("Subscribing new listener: " + listener);
        this.bus.subscribe(listener);
    }

    /**
     * This method unsubscribes the provided listener from the event bus
     * @param listener defines the listener to unsubscribe from the event bus
     */
    public void unsubscribe(SignalListener listener) {
        LOGGER.debug("Unsubscribing listener: " + listener);
        this.bus.unsubscribe(listener);
    }

    /**
     * @param signalMessage
     * @param async
     * @return
     */
    public void publishSignal(SignalMessage signalMessage, boolean async) {
        LOGGER.debug("Publishing {} for {} {} {} {}", signalMessage.getSignal(), signalMessage.getMarket(), signalMessage.getSymbol(), signalMessage.getInterval(), signalMessage.getStrategy());
        if (async) {
            this.bus.post(signalMessage).asynchronously();
        } else {
            this.publishSignal(signalMessage);
        }
    }

    /**
     * @param signalMessage
     * @return
     */
    private void publishSignal(SignalMessage signalMessage) {
        this.bus.post(signalMessage).now();
    }
}
