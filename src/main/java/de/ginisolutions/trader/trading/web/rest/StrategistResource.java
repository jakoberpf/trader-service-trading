package de.ginisolutions.trader.trading.web.rest;

import de.ginisolutions.trader.trading.service.StrategistService;
import de.ginisolutions.trader.trading.web.rest.errors.BadRequestAlertException;
import de.ginisolutions.trader.trading.service.dto.StrategistDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link de.ginisolutions.trader.trading.domain.Strategist}.
 */
@RestController
@RequestMapping("/api")
public class StrategistResource {

    private final Logger log = LoggerFactory.getLogger(StrategistResource.class);

    private static final String ENTITY_NAME = "tradingServiceStrategist";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StrategistService strategistService;

    public StrategistResource(StrategistService strategistService) {
        this.strategistService = strategistService;
    }

    /**
     * {@code POST  /strategists} : Create a new strategist.
     *
     * @param strategistDTO the strategistDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new strategistDTO, or with status {@code 400 (Bad Request)} if the strategist has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/strategists")
    public ResponseEntity<StrategistDTO> createStrategist(@RequestBody StrategistDTO strategistDTO) throws URISyntaxException {
        log.debug("REST request to save Strategist : {}", strategistDTO);
        if (strategistDTO.getId() != null) {
            throw new BadRequestAlertException("A new strategist cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StrategistDTO result = strategistService.save(strategistDTO);
        return ResponseEntity.created(new URI("/api/strategists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /strategists} : Updates an existing strategist.
     *
     * @param strategistDTO the strategistDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated strategistDTO,
     * or with status {@code 400 (Bad Request)} if the strategistDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the strategistDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/strategists")
    public ResponseEntity<StrategistDTO> updateStrategist(@RequestBody StrategistDTO strategistDTO) throws URISyntaxException {
        log.debug("REST request to update Strategist : {}", strategistDTO);
        if (strategistDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StrategistDTO result = strategistService.save(strategistDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, strategistDTO.getId()))
            .body(result);
    }

    /**
     * {@code GET  /strategists} : get all the strategists.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of strategists in body.
     */
    @GetMapping("/strategists")
    public List<StrategistDTO> getAllStrategists() {
        log.debug("REST request to get all Strategists");
        return strategistService.findAll();
    }

    /**
     * {@code GET  /strategists/:id} : get the "id" strategist.
     *
     * @param id the id of the strategistDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the strategistDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/strategists/{id}")
    public ResponseEntity<StrategistDTO> getStrategist(@PathVariable String id) {
        log.debug("REST request to get Strategist : {}", id);
        Optional<StrategistDTO> strategistDTO = strategistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(strategistDTO);
    }

    /**
     * {@code DELETE  /strategists/:id} : delete the "id" strategist.
     *
     * @param id the id of the strategistDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/strategists/{id}")
    public ResponseEntity<Void> deleteStrategist(@PathVariable String id) {
        log.debug("REST request to delete Strategist : {}", id);

        strategistService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
