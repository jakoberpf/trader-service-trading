package de.ginisolutions.trader.trading.service.mapper;

import de.ginisolutions.trader.trading.domain.Trader;
import de.ginisolutions.trader.trading.service.dto.TraderPOST;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Trader} and its DTO {@link TraderPOST}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TraderMapperPOST extends EntityMapper<TraderPOST, Trader> {
}
