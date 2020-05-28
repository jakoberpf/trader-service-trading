package de.ginisolutions.trader.trading.service;

import de.ginisolutions.trader.trading.domain.Strategist;
import de.ginisolutions.trader.trading.repository.StrategistRepository;
import de.ginisolutions.trader.trading.service.dto.StrategistDTO;
import de.ginisolutions.trader.trading.service.mapper.StrategistMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Strategist}.
 */
@Service
public class StrategistService {

    private final Logger log = LoggerFactory.getLogger(StrategistService.class);

    private final StrategistRepository strategistRepository;

    private final StrategistMapper strategistMapper;

    public StrategistService(StrategistRepository strategistRepository, StrategistMapper strategistMapper) {
        this.strategistRepository = strategistRepository;
        this.strategistMapper = strategistMapper;
    }

    /**
     * Save a strategist.
     *
     * @param strategistDTO the entity to save.
     * @return the persisted entity.
     */
    public StrategistDTO save(StrategistDTO strategistDTO) {
        log.debug("Request to save Strategist : {}", strategistDTO);
        Strategist strategist = strategistMapper.toEntity(strategistDTO);
        strategist = strategistRepository.save(strategist);
        return strategistMapper.toDto(strategist);
    }

    /**
     * Get all the strategists.
     *
     * @return the list of entities.
     */
    public List<StrategistDTO> findAll() {
        log.debug("Request to get all Strategists");
        return strategistRepository.findAll().stream()
            .map(strategistMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one strategist by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<StrategistDTO> findOne(String id) {
        log.debug("Request to get Strategist : {}", id);
        return strategistRepository.findById(id)
            .map(strategistMapper::toDto);
    }

    /**
     * Delete the strategist by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Strategist : {}", id);

        strategistRepository.deleteById(id);
    }
}
