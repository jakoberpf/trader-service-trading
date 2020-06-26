package de.ginisolutions.trader.trading.service.dto;

import de.ginisolutions.trader.common.enumeration.INTERVAL;
import de.ginisolutions.trader.common.enumeration.MARKET;
import de.ginisolutions.trader.common.enumeration.SYMBOL;
import de.ginisolutions.trader.trading.domain.model.Trade;
import de.ginisolutions.trader.common.enumeration.STRATEGY;
import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * A DTO for the getting of a {@link de.ginisolutions.trader.trading.domain.Trader} entity.
 */
@ApiModel(description = "The TraderGetDTO entity.\n@author A true hipster")
public class TraderGET {

    @NotNull
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

    @NotNull
    private Boolean isLive;

    @NotNull
    private Boolean isIn;

    @NotNull
    private Double budget;

    @NotNull
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

    public Boolean getLive() {
        return isLive;
    }

    public void setLive(Boolean live) {
        isLive = live;
    }

    public Boolean getIn() {
        return isIn;
    }

    public void setIn(Boolean in) {
        isIn = in;
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
}
