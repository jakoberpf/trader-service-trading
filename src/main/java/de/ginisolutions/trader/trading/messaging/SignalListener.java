package de.ginisolutions.trader.trading.messaging;

import de.ginisolutions.trader.common.messaging.BaseListener;
import net.engio.mbassy.listener.Listener;
import net.engio.mbassy.listener.References;

@Listener(references = References.Strong)
public interface SignalListener extends BaseListener {

}
