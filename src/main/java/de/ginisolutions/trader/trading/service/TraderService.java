package de.ginisolutions.trader.trading.service;

import de.ginisolutions.trader.trading.domain.Trader;
import de.ginisolutions.trader.trading.management.TraderManager;
import de.ginisolutions.trader.trading.repository.TraderRepository;
import de.ginisolutions.trader.trading.service.dto.TraderGET;
import de.ginisolutions.trader.trading.service.dto.TraderPOST;
import de.ginisolutions.trader.trading.service.dto.TraderDTO;
import de.ginisolutions.trader.trading.service.dto.TraderPUT;
import de.ginisolutions.trader.trading.service.mapper.TraderMapper;
import de.ginisolutions.trader.trading.service.mapper.TraderMapperGET;
import de.ginisolutions.trader.trading.service.mapper.TraderMapperPOST;
import de.ginisolutions.trader.trading.service.mapper.TraderMapperPUT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Trader}.
 */
@Service
public class TraderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TraderService.class);

    private final TraderRepository traderRepository;

    private final TraderManager traderManager;

    private final TraderMapper traderMapper;

    private final TraderMapperPOST traderMapperPOST;

    private final TraderMapperPUT traderMapperPUT;

    private final TraderMapperGET traderMapperGET;

    public TraderService(TraderRepository traderRepository, TraderManager traderManager, TraderMapper traderMapper, TraderMapperPOST traderMapperPOST, TraderMapperPUT traderMapperPUT, TraderMapperGET traderMapperGET) {
        LOGGER.info("Constructing TradingService");
        this.traderRepository = traderRepository;
        this.traderManager = traderManager;
        this.traderMapper = traderMapper;
        this.traderMapperPOST = traderMapperPOST;
        this.traderMapperPUT = traderMapperPUT;
        this.traderMapperGET = traderMapperGET;
    }

    /**
     * Create a trader.
     *
     * @param traderPOST the trader to create.
     * @return the persisted entity.
     */
    public TraderGET create(TraderPOST traderPOST) {
        LOGGER.debug("Request to create Trader : {}", traderPOST);
        Trader trader = traderMapperPOST.toEntity(traderPOST);
        trader = this.traderManager.add(trader);
        return traderMapperGET.toDto(trader);
    }

    /**
     * Create a trader.
     *
     * @param traderPUT the trader to create.
     * @return the persisted entity.
     */
    public TraderGET update(TraderPUT traderPUT) {
        LOGGER.debug("Request to update Trader : {}", traderPUT);
        Trader trader = traderMapperPUT.toEntity(traderPUT);
        final Trader traderFromRepo = this.traderManager.get(traderPUT.getId());
        trader.setMarket(traderFromRepo.getMarket());
        trader.setSymbol(traderFromRepo.getSymbol());
        trader.setInterval(traderFromRepo.getInterval());
        trader.setTradeHistory(traderFromRepo.getTradeHistory());
        if (trader.getApiKey().isEmpty() || trader.getApiSecret().isEmpty()) {
            trader.setApiKey(traderFromRepo.getApiKey());
            trader.setApiSecret(traderFromRepo.getApiSecret());
        }
        trader = this.traderManager.edit(trader);
        return traderMapperGET.toDto(trader);
    }

    /**
     * Get all the traders.
     *
     * @return the list of entities.
     */
    public List<TraderGET> findAll() {
        LOGGER.debug("Request to get all Traders");
        return traderRepository.findAll().stream()
            .map(traderMapperGET::toDto)
            .collect(Collectors.toList());
    }


    /**
     * Get all the traders from the owner
     *
     * @param owner the name of the owner
     * @return the list of entities.
     */
    public List<TraderGET> findAllByOwner(String owner) {
        LOGGER.debug("Request to get all Traders");
        return traderRepository.findAllByOwner(owner).stream()
            .map(traderMapperGET::toDto)
            .collect(Collectors.toList());
    }


    /**
     * Get one trader by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<TraderGET> findOne(String id) {
        LOGGER.debug("Request to get Trader : {}", id);
        return traderRepository.findById(id).map(traderMapperGET::toDto);
    }

    /**
     * Delete the trader by id.
     *
     * @param id the id of the entity.
     */
    public String delete(String id) {
        LOGGER.debug("Request to delete Trader : {}", id);
        return this.traderManager.remove(id);
    }
}
