package de.ginisolutions.trader.trading.domain;

import de.ginisolutions.trader.common.strategy.parameter.StrategyParameter;
import de.ginisolutions.trader.history.domain.enumeration.INTERVAL;
import de.ginisolutions.trader.history.domain.enumeration.MARKET;
import de.ginisolutions.trader.history.domain.enumeration.SYMBOL;
import de.ginisolutions.trader.trading.domain.enumeration.STRATEGY;
import de.ginisolutions.trader.trading.messaging.Signal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * The Trader entity.
 * @author <a href="mailto:contact@jakoberpf.de">Jakob Erpf</a>
 */
@Document(collection = "trader")
public class Trader implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("name")
    private String name;

    @NotNull
    @Field("owner")
    private String owner;

    @NotNull
    @Field("market")
    private MARKET market;

    @NotNull
    @Field("symbol")
    private SYMBOL symbol;

    @NotNull
    @Field("interval")
    private INTERVAL interval;

    // TODO include parameters in strategy, so that strategy is the single identifier for an stragegy
    @NotNull
    @Field("strategy")
    private STRATEGY strategy; // TODO implement list of strategies for listening to multiple strategies

    @NotNull
    @Field("api_key")
    private String apiKey;

    @NotNull
    @Field("api_secret")
    private String apiSecret;

    @NotNull
    @Field("is_live")
    private Boolean isLive;

    @NotNull
    @Field("is_in")
    private Boolean isIn;

    @NotNull
    @Field("budget")
    private Double budget;

    @Field("signalHistory")
    private List<Signal> signalHistory;

    @Field("tradeHistory")
    private List<Trade> tradeHistory;

    // TODO implement trading record if serializable

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Trader name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public Trader owner(String owner) {
        this.owner = owner;
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public MARKET getMarket() {
        return market;
    }

    public Trader market(MARKET market) {
        this.market = market;
        return this;
    }

    public void setMarket(MARKET market) {
        this.market = market;
    }

    public SYMBOL getSymbol() {
        return symbol;
    }

    public Trader symbol(SYMBOL symbol) {
        this.symbol = symbol;
        return this;
    }

    public void setSymbol(SYMBOL symbol) {
        this.symbol = symbol;
    }

    public INTERVAL getInterval() {
        return interval;
    }

    public Trader interval(INTERVAL interval) {
        this.interval = interval;
        return this;
    }

    public void setInterval(INTERVAL interval) {
        this.interval = interval;
    }

    public STRATEGY getStrategy() {
        return strategy;
    }

    public Trader strategy(STRATEGY strategy) {
        this.strategy = strategy;
        return this;
    }

    public void setStrategy(STRATEGY strategy) {
        this.strategy = strategy;
    }

    public String getApiKey() {
        return apiKey;
    }

    public Trader apiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public Trader apiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
        return this;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    // TODO rework to isLive() and isLive(boolean)
    public Boolean isIsLive() {
        return isLive;
    }

    public Trader isLive(Boolean isLive) {
        this.isLive = isLive;
        return this;
    }

    public void setIsLive(Boolean isLive) {
        this.isLive = isLive;
    }

    // TODO rework to isIn() and isIn(boolean)
    public Boolean isIsIn() {
        return isIn;
    }

    public Trader isIn(Boolean isIn) {
        this.isIn = isIn;
        return this;
    }

    public void setIsIn(Boolean isIn) {
        this.isIn = isIn;
    }

    public Double getBudget() {
        return budget;
    }

    public Trader budget(Double budget) {
        this.budget = budget;
        return this;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public List<Signal> getSignalHistory() {
        return signalHistory;
    }

    // TODO add signalHistory()

    public void setSignalHistory(List<Signal> signalHistory) {
        this.signalHistory = signalHistory;
    }

    public List<Trade> getTradeHistory() {
        return tradeHistory;
    }

    // TODO add tradeHistory()

    public void setTradeHistory(List<Trade> tradeHistory) {
        this.tradeHistory = tradeHistory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trader)) {
            return false;
        }
        return id != null && id.equals(((Trader) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Trader{" +
                "id=" + getId() +
                ", name='" + getName() + "'" +
                ", owner='" + getOwner() + "'" +
                ", market='" + getMarket() + "'" +
                ", symbol='" + getSymbol() + "'" +
                ", apiKey='" + getApiKey() + "'" +
                ", apiSecret='" + getApiSecret() + "'" +
                ", isLive='" + isIsLive() + "'" +
                ", isIn='" + isIsIn() + "'" +
                ", budget=" + getBudget() +
                "}";
    }
}
