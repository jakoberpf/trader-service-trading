package de.ginisolutions.trader.trading.service;

import de.ginisolutions.trader.trading.repository.TraderRepository;
import de.ginisolutions.trader.trading.service.dto.TraderDTO;
import de.ginisolutions.trader.trading.service.mapper.TraderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Trader}.
 */
@Service
public class TraderService {

    private final Logger log = LoggerFactory.getLogger(TraderService.class);

    private final TraderRepository traderRepository;

    private final TraderMapper traderMapper;

    public TraderService(TraderRepository traderRepository, TraderMapper traderMapper) {
        this.traderRepository = traderRepository;
        this.traderMapper = traderMapper;
    }

    /**
     * Save a trader.
     *
     * @param traderDTO the entity to save.
     * @return the persisted entity.
     */
    public TraderDTO save(TraderDTO traderDTO) {
        log.debug("Request to save Trader : {}", traderDTO);
        Trader trader = traderMapper.toEntity(traderDTO);
        trader = traderRepository.save(trader);
        return traderMapper.toDto(trader);
    }

    /**
     * Get all the traders.
     *
     * @return the list of entities.
     */
    public List<TraderDTO> findAll() {
        log.debug("Request to get all Traders");
        return traderRepository.findAll().stream()
            .map(traderMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one trader by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<TraderDTO> findOne(String id) {
        log.debug("Request to get Trader : {}", id);
        return traderRepository.findById(id)
            .map(traderMapper::toDto);
    }

    /**
     * Delete the trader by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Trader : {}", id);

        traderRepository.deleteById(id);
    }
}
