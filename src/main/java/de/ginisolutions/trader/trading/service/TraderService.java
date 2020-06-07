package de.ginisolutions.trader.trading.service;

import de.ginisolutions.trader.trading.domain.Trader;
import de.ginisolutions.trader.trading.management.TraderManager;
import de.ginisolutions.trader.trading.repository.TraderRepository;
import de.ginisolutions.trader.trading.service.dto.TraderDTO;
import de.ginisolutions.trader.trading.service.mapper.TraderMapper;
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

    public TraderService(TraderRepository traderRepository, TraderManager traderManager, TraderMapper traderMapper) {
        LOGGER.info("Constructing TradingService");
        this.traderRepository = traderRepository;
        this.traderManager = traderManager;
        this.traderMapper = traderMapper;
    }

    /**
     * Create a trader.
     *
     * @param traderDTO the trader to create.
     * @return the persisted entity.
     */
    public TraderDTO create(TraderDTO traderDTO) {
        LOGGER.debug("Request to create Trader : {}", traderDTO);
        if (traderDTO.getId() != null) {
            throw new IllegalArgumentException("A new trader cannot already have an ID");
        }
        if (traderDTO.getTradeHistory() != null) {
            throw new IllegalArgumentException("A new trader cannot already have a trader history");
        }
        Trader trader = traderMapper.toEntity(traderDTO);
        trader = this.traderManager.add(trader);
        return traderMapper.toDto(trader);
    }

    /**
     * Create a trader.
     *
     * @param traderDTO the trader to create.
     * @return the persisted entity.
     */
    public TraderDTO update(TraderDTO traderDTO) {
        LOGGER.debug("Request to update Trader : {}", traderDTO);
        if (traderDTO.getId() == null) {
            throw new IllegalArgumentException("A trader must have a ID to be updated");
        }
        if (traderDTO.getTradeHistory() != null) {
            throw new IllegalArgumentException("A traders history can not be updated");
        }
        Trader trader = traderMapper.toEntity(traderDTO);
        if (trader.getApiKey() == null || trader.getApiSecret() == null) {
            final Optional<Trader> traderFromRepo = this.traderRepository.findById(trader.getId());
            if (traderFromRepo.isPresent()) {
                trader.setApiKey(traderFromRepo.get().getApiKey());
                trader.setApiSecret(traderFromRepo.get().getApiSecret());
            } else {
                throw new RuntimeException("Trader to update not found");
            }
        }
        trader = this.traderManager.edit(trader);
        return traderMapper.toDto(trader);
    }

    /**
     * Get all the traders.
     *
     * @return the list of entities.
     */
    public List<TraderDTO> findAll() {
        LOGGER.debug("Request to get all Traders");
        return traderRepository.findAll().stream()
            .map(traderMapper::toDto)
            .collect(Collectors.toList());
    }


    /**
     * Get all the traders.
     * *
     * @param owner the name of the owner
     * @return the list of entities.
     */
    public List<TraderDTO> findAllByOwner(String owner) {
        LOGGER.debug("Request to get all Traders");
        return traderRepository.findAllByOwner(owner).stream()
            .map(traderMapper::toDto)
            .collect(Collectors.toList());
    }


    /**
     * Get one trader by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<TraderDTO> findOne(String id) {
        LOGGER.debug("Request to get Trader : {}", id);
        return traderRepository.findById(id).map(traderMapper::toDto);
    }

    /**
     * Delete the trader by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        LOGGER.debug("Request to delete Trader : {}", id);
        Trader trader = this.traderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Unable to find trader with id: " + id));
        trader = this.traderManager.remove(trader);
        // TODO return message
    }
}
