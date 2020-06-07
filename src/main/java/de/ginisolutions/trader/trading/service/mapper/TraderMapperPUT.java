package de.ginisolutions.trader.trading.service.mapper;

import de.ginisolutions.trader.trading.domain.Trader;
import de.ginisolutions.trader.trading.service.dto.TraderPUT;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Trader} and its DTO {@link TraderPUT}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TraderMapperPUT extends EntityMapper<TraderPUT, Trader> {
}
