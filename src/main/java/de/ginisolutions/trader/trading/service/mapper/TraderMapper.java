package de.ginisolutions.trader.trading.service.mapper;


import de.ginisolutions.trader.trading.domain.*;
import de.ginisolutions.trader.trading.service.dto.TraderDTO;

import org.mapstruct.*;

import java.util.List;

/**
 * Mapper for the entity {@link Trader} and its DTO {@link TraderDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TraderMapper extends EntityMapper<TraderDTO, Trader> {

    @Mapping(target = "tradeHistory", ignore = true)
    Trader toEntity(TraderDTO dto);

    @Mapping(target = "apiKey", ignore = true)
    @Mapping(target = "apiSecret", ignore = true)
    TraderDTO toDto(Trader entity);
}
