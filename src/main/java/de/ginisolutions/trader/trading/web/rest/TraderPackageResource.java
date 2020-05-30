package de.ginisolutions.trader.trading.web.rest;

import de.ginisolutions.trader.trading.domain.TraderPackage;
import de.ginisolutions.trader.trading.repository.TraderPackageRepository;
import de.ginisolutions.trader.trading.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link de.ginisolutions.trader.trading.domain.TraderPackage}.
 */
@RestController
@RequestMapping("/api")
public class TraderPackageResource {

    private final Logger log = LoggerFactory.getLogger(TraderPackageResource.class);

    private static final String ENTITY_NAME = "traderServiceTradingTraderPackage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TraderPackageRepository traderPackageRepository;

    public TraderPackageResource(TraderPackageRepository traderPackageRepository) {
        this.traderPackageRepository = traderPackageRepository;
    }

    /**
     * {@code POST  /trader-packages} : Create a new traderPackage.
     *
     * @param traderPackage the traderPackage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new traderPackage, or with status {@code 400 (Bad Request)} if the traderPackage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trader-packages")
    public ResponseEntity<TraderPackage> createTraderPackage(@RequestBody TraderPackage traderPackage) throws URISyntaxException {
        log.debug("REST request to save TraderPackage : {}", traderPackage);
        if (traderPackage.getId() != null) {
            throw new BadRequestAlertException("A new traderPackage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TraderPackage result = traderPackageRepository.save(traderPackage);
        return ResponseEntity.created(new URI("/api/trader-packages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /trader-packages} : Updates an existing traderPackage.
     *
     * @param traderPackage the traderPackage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated traderPackage,
     * or with status {@code 400 (Bad Request)} if the traderPackage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the traderPackage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trader-packages")
    public ResponseEntity<TraderPackage> updateTraderPackage(@RequestBody TraderPackage traderPackage) throws URISyntaxException {
        log.debug("REST request to update TraderPackage : {}", traderPackage);
        if (traderPackage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TraderPackage result = traderPackageRepository.save(traderPackage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, traderPackage.getId()))
            .body(result);
    }

    /**
     * {@code GET  /trader-packages} : get all the traderPackages.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of traderPackages in body.
     */
    @GetMapping("/trader-packages")
    public List<TraderPackage> getAllTraderPackages() {
        log.debug("REST request to get all TraderPackages");
        return traderPackageRepository.findAll();
    }

    /**
     * {@code GET  /trader-packages/:id} : get the "id" traderPackage.
     *
     * @param id the id of the traderPackage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the traderPackage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trader-packages/{id}")
    public ResponseEntity<TraderPackage> getTraderPackage(@PathVariable String id) {
        log.debug("REST request to get TraderPackage : {}", id);
        Optional<TraderPackage> traderPackage = traderPackageRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(traderPackage);
    }

    /**
     * {@code DELETE  /trader-packages/:id} : delete the "id" traderPackage.
     *
     * @param id the id of the traderPackage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trader-packages/{id}")
    public ResponseEntity<Void> deleteTraderPackage(@PathVariable String id) {
        log.debug("REST request to delete TraderPackage : {}", id);

        traderPackageRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
