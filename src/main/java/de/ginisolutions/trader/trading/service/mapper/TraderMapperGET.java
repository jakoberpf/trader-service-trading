package de.ginisolutions.trader.trading.service.mapper;

import de.ginisolutions.trader.trading.domain.model.Trade;
import de.ginisolutions.trader.trading.domain.Trader;
import de.ginisolutions.trader.trading.service.dto.TraderGET;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Trader} and its DTO {@link TraderGET}.
 */
@Mapper(componentModel = "spring", uses = {Trade.class})
public interface TraderMapperGET extends EntityMapper<TraderGET, Trader> {
}
