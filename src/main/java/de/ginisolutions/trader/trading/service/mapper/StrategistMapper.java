package de.ginisolutions.trader.trading.service.mapper;


import de.ginisolutions.trader.trading.domain.*;
import de.ginisolutions.trader.trading.service.dto.StrategistDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Strategist} and its DTO {@link StrategistDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StrategistMapper extends EntityMapper<StrategistDTO, Strategist> {



    default Strategist fromId(String id) {
        if (id == null) {
            return null;
        }
        Strategist strategist = new Strategist();
        strategist.setId(id);
        return strategist;
    }
}
