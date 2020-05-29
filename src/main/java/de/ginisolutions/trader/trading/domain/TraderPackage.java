package de.ginisolutions.trader.trading.domain;

import io.swagger.annotations.ApiModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * The TraderPackage entity.\n@author A true hipster
 */
@ApiModel(description = "The TraderPackage entity.\n@author A true hipster")
@Document(collection = "trader_package")
public class TraderPackage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("dummy")
    private String dummy;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDummy() {
        return dummy;
    }

    public TraderPackage dummy(String dummy) {
        this.dummy = dummy;
        return this;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TraderPackage)) {
            return false;
        }
        return id != null && id.equals(((TraderPackage) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TraderPackage{" +
            "id=" + getId() +
            ", dummy='" + getDummy() + "'" +
            "}";
    }
}
