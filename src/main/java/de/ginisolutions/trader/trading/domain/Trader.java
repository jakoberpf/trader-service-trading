package de.ginisolutions.trader.trading.domain;

import io.swagger.annotations.ApiModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * The Trader entity.\n@author A true hipster
 */
@ApiModel(description = "The Trader entity.\n@author A true hipster")
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
    @Field("api_key")
    private String apiKey;

    @NotNull
    @Field("api_secret")
    private String apiSecret;

    @NotNull
    @Field("attributes")
    private String attributes;

    // jhipster-needle-entity-add-field - JHipster will add fields here
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

    public String getAttributes() {
        return attributes;
    }

    public Trader attributes(String attributes) {
        this.attributes = attributes;
        return this;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
            ", apiKey='" + getApiKey() + "'" +
            ", apiSecret='" + getApiSecret() + "'" +
            ", attributes='" + getAttributes() + "'" +
            "}";
    }
}
