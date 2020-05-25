package de.ginisolutions.trader.trading.web.rest;

import de.ginisolutions.trader.trading.domain.Trader;
import de.ginisolutions.trader.trading.repository.TraderRepository;
import de.ginisolutions.trader.trading.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link de.ginisolutions.trader.trading.domain.Trader}.
 */
@RestController
@RequestMapping("/api")
public class TraderResource {

    private final Logger log = LoggerFactory.getLogger(TraderResource.class);

    private static final String ENTITY_NAME = "tradingServiceTrader";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TraderRepository traderRepository;

    public TraderResource(TraderRepository traderRepository) {
        this.traderRepository = traderRepository;
    }

    /**
     * {@code POST  /traders} : Create a new trader.
     *
     * @param trader the trader to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trader, or with status {@code 400 (Bad Request)} if the trader has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/traders")
    public ResponseEntity<Trader> createTrader(@Valid @RequestBody Trader trader) throws URISyntaxException {
        log.debug("REST request to save Trader : {}", trader);
        if (trader.getId() != null) {
            throw new BadRequestAlertException("A new trader cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Trader result = traderRepository.save(trader);
        return ResponseEntity.created(new URI("/api/traders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /traders} : Updates an existing trader.
     *
     * @param trader the trader to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trader,
     * or with status {@code 400 (Bad Request)} if the trader is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trader couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/traders")
    public ResponseEntity<Trader> updateTrader(@Valid @RequestBody Trader trader) throws URISyntaxException {
        log.debug("REST request to update Trader : {}", trader);
        if (trader.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Trader result = traderRepository.save(trader);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trader.getId()))
            .body(result);
    }

    /**
     * {@code GET  /traders} : get all the traders.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of traders in body.
     */
    @GetMapping("/traders")
    public List<Trader> getAllTraders() {
        log.debug("REST request to get all Traders");
        return traderRepository.findAll();
    }

    /**
     * {@code GET  /traders/:id} : get the "id" trader.
     *
     * @param id the id of the trader to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trader, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/traders/{id}")
    public ResponseEntity<Trader> getTrader(@PathVariable String id) {
        log.debug("REST request to get Trader : {}", id);
        Optional<Trader> trader = traderRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(trader);
    }

    /**
     * {@code DELETE  /traders/:id} : delete the "id" trader.
     *
     * @param id the id of the trader to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/traders/{id}")
    public ResponseEntity<Void> deleteTrader(@PathVariable String id) {
        log.debug("REST request to delete Trader : {}", id);

        traderRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
