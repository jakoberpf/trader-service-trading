package de.ginisolutions.trader.trading.event;

import org.springframework.context.ApplicationEvent;

public class TradingInit extends ApplicationEvent {

    private boolean historyManager;

    private boolean strategistManager;

    private boolean traderManager;
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public TradingInit(Object source) {
        super(source);
        this.historyManager = false;
        this.strategistManager = false;
        this.traderManager = false;
    }

    public boolean isHistoryManager() {
        return historyManager;
    }

    public void setHistoryManager() {
        this.historyManager = true;
    }

    public boolean isStrategistManager() {
        return strategistManager;
    }

    public void setStrategistManager() {
        this.strategistManager = true;
    }

    public boolean isTraderManager() {
        return traderManager;
    }

    public void setTraderManager() {
        this.traderManager = true;
    }
}
