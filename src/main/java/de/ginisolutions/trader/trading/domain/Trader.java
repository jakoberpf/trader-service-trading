package de.ginisolutions.trader.trading.domain;

import com.bol.secure.Encrypted;
import de.ginisolutions.trader.common.enumeration.INTERVAL;
import de.ginisolutions.trader.common.enumeration.MARKET;
import de.ginisolutions.trader.common.enumeration.SYMBOL;
import de.ginisolutions.trader.common.enumeration.STRATEGY;
import de.ginisolutions.trader.trading.domain.model.Trade;
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
    @Encrypted
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

    @NotNull
    @Field("strategy")
    private STRATEGY strategy; // TODO implement list of strategies for listening to multiple strategies

    @NotNull
    @Encrypted
    @Field("api_key")
    private String apiKey;

    @NotNull
    @Encrypted
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

    @Field("tradeHistory")
    private List<Trade> tradeHistory;

    public String getId() {
        return id;
    }

    public Trader id(String id) {
        this.id = id;
        return this;
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

    public Boolean isLive() {
        return isLive;
    }

    public Trader isLive(Boolean isLive) {
        this.isLive = isLive;
        return this;
    }

    public void setLive(Boolean isLive) {
        this.isLive = isLive;
    }

    public Boolean isIn() {
        return isIn;
    }

    public Trader isIn(Boolean isIn) {
        this.isIn = isIn;
        return this;
    }

    public void setIn(Boolean isIn) {
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
                ", isLive='" + isLive() + "'" +
                ", isIn='" + isIn() + "'" +
                ", budget=" + getBudget() +
                "}";
    }
}
