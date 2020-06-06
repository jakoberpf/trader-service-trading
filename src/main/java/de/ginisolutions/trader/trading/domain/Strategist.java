package de.ginisolutions.trader.trading.domain;

import de.ginisolutions.trader.common.strategy.parameter.StrategyParameter;
import de.ginisolutions.trader.history.domain.enumeration.INTERVAL;
import de.ginisolutions.trader.history.domain.enumeration.MARKET;
import de.ginisolutions.trader.trading.domain.enumeration.STRATEGY;
import de.ginisolutions.trader.history.domain.enumeration.SYMBOL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * The Strategist entity.\n@author A true hipster
 */
@Document(collection = "strategist")
public class Strategist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("strategy")
    private STRATEGY strategy;

    @Field("market")
    private MARKET market;

    @Field("symbol")
    private SYMBOL symbol;

    @Field("interval")
    private INTERVAL interval;

    @Field("parameters")
    private StrategyParameter parameters;

    public Strategist() {
    }

    public Strategist(STRATEGY strategy, MARKET market, SYMBOL symbol, INTERVAL interval, StrategyParameter parameters) {
        this.strategy = strategy;
        this.market = market;
        this.symbol = symbol;
        this.interval = interval;
        this.parameters = parameters;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public STRATEGY getStrategy() {
        return strategy;
    }

    public Strategist strategy(STRATEGY strategy) {
        this.strategy = strategy;
        return this;
    }

    public void setStrategy(STRATEGY strategy) {
        this.strategy = strategy;
    }

    public MARKET getMarket() {
        return market;
    }

    public Strategist market(MARKET market) {
        this.market = market;
        return this;
    }

    public void setMarket(MARKET market) {
        this.market = market;
    }

    public SYMBOL getSymbol() {
        return symbol;
    }

    public Strategist symbol(SYMBOL symbol) {
        this.symbol = symbol;
        return this;
    }

    public void setSymbol(SYMBOL symbol) {
        this.symbol = symbol;
    }

    public INTERVAL getInterval() {
        return interval;
    }

    public Strategist interval(INTERVAL interval) {
        this.interval = interval;
        return this;
    }

    public void setInterval(INTERVAL interval) {
        this.interval = interval;
    }

    public StrategyParameter getParameters() {
        return parameters;
    }

    public void setParameters(StrategyParameter parameters) {
        this.parameters = parameters;
    }

    public Strategist parameters(StrategyParameter parameters) {
        this.parameters = parameters;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Strategist)) {
            return false;
        }
        return id != null && id.equals(((Strategist) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Strategist{" +
                "id='" + id + '\'' +
                ", strategy=" + strategy +
                ", market=" + market +
                ", symbol=" + symbol +
                ", interval=" + interval +
                ", parameters=" + parameters +
                '}';
    }
}
