package de.ginisolutions.trader.trading.service.dto;

import de.ginisolutions.trader.trading.domain.enumeration.STRATEGY;
import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotNull;

/**
 * A DTO for the update of a {@link de.ginisolutions.trader.trading.domain.Trader} entity.
 */
@ApiModel(description = "The TraderUpdateDTO entity.\n@author A true hipster")
public class TraderPUT {

    @NotNull
    private String id;

    @NotNull
    private String name;

    @NotNull
    private String owner;

    private STRATEGY strategy;

    private String apiKey;

    private String apiSecret;

    private Boolean isLive;

    private Boolean isIn;

    private Double budget;

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
}
