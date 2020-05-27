package de.ginisolutions.trader.trading.service.mapper;


import de.ginisolutions.trader.trading.domain.*;
import de.ginisolutions.trader.trading.service.dto.TraderDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Trader} and its DTO {@link TraderDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TraderMapper extends EntityMapper<TraderDTO, Trader> {



    default Trader fromId(String id) {
        if (id == null) {
            return null;
        }
        Trader trader = new Trader();
        trader.setId(id);
        return trader;
    }
}
