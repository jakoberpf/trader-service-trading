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
        this.bus.subscribe(listener);
    }

    /**
     * @param signalMessage
     * @param aync
     * @return
     */
    public boolean publishSignal(SignalMessage signalMessage, boolean aync) {
        if (aync) {
            this.bus.post(signalMessage).asynchronously();
        } else {
            this.publishSignal(signalMessage);
        }
        return true;
    }

    /**
     * @param signalMessage
     * @return
     */
    public boolean publishSignal(SignalMessage signalMessage) {
        this.bus.post(signalMessage).now();
        return true;
    }
}
