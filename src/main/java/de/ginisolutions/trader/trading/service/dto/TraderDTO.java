package de.ginisolutions.trader.trading.service.dto;

import de.ginisolutions.trader.history.domain.enumeration.INTERVAL;
import de.ginisolutions.trader.history.domain.enumeration.MARKET;
import de.ginisolutions.trader.history.domain.enumeration.SYMBOL;
import de.ginisolutions.trader.trading.domain.Trade;
import de.ginisolutions.trader.trading.domain.enumeration.STRATEGY;
import io.swagger.annotations.ApiModel;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;


/**
 * A DTO for the {@link de.ginisolutions.trader.trading.domain.Trader} entity.
 */
@ApiModel(description = "The Trader entity.\n@author A true hipster")
public class TraderDTO implements Serializable {

    private String id;

    @NotNull
    private String name;

    @NotNull
    private String owner;

    @NotNull
    private MARKET market;

    @NotNull
    private SYMBOL symbol;

    @NotNull
    private INTERVAL interval;

    @NotNull
    private STRATEGY strategy;

    private String apiKey;

    private String apiSecret;

    @NotNull
    private Boolean isLive;

    @NotNull
    private Boolean isIn;

    @NotNull
    private Double budget;

    private List<Trade> tradeHistory;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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

    public STRATEGY getStrategy() {
        return strategy;
    }

    public void setStrategy(STRATEGY strategy) {
        this.strategy = strategy;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public Boolean isLive() {
        return isLive;
    }

    public void setLive(Boolean isLive) {
        this.isLive = isLive;
    }

    public Boolean isIn() {
        return isIn;
    }

    public void setIn(Boolean isIn) {
        this.isIn = isIn;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public List<Trade> getTradeHistory() {
        return tradeHistory;
    }

    public void setTradeHistory(List<Trade> tradeHistory) {
        this.tradeHistory = tradeHistory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TraderDTO)) {
            return false;
        }

        return id != null && id.equals(((TraderDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TraderDTO{" +
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
