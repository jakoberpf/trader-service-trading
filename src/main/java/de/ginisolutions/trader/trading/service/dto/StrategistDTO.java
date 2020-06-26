package de.ginisolutions.trader.trading.service.dto;

import de.ginisolutions.trader.common.enumeration.INTERVAL;
import de.ginisolutions.trader.common.enumeration.MARKET;
import de.ginisolutions.trader.common.enumeration.STRATEGY;
import de.ginisolutions.trader.common.enumeration.SYMBOL;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;

/**
 * A DTO for the {@link de.ginisolutions.trader.trading.domain.Strategist} entity.
 */
@ApiModel(description = "The Strategist entity.\n@author A true hipster")
public class StrategistDTO implements Serializable {

    private String id;

    private STRATEGY strategy;

    private MARKET market;

    private SYMBOL symbol;

    private INTERVAL interval;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public STRATEGY getStrategy() {
        return strategy;
    }

    public void setStrategy(STRATEGY strategy) {
        this.strategy = strategy;
    }

    public MARKET getMarket() {
        return market;
    }

    public void setMarket(MARKET market) {
        this.market = market;
    }

    public SYMBOL getSymbol() {
        return symbol;
    }

    public void setSymbol(SYMBOL symbol) {
        this.symbol = symbol;
    }

    public INTERVAL getInterval() {
        return interval;
    }

    public void setInterval(INTERVAL interval) {
        this.interval = interval;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StrategistDTO)) {
            return false;
        }

        return id != null && id.equals(((StrategistDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StrategistDTO{" +
            "id=" + getId() +
            ", strategy='" + getStrategy() + "'" +
            ", market='" + getMarket() + "'" +
            ", symbol='" + getSymbol() + "'" +
            ", interval='" + getInterval() + "'" +
            "}";
    }
}
