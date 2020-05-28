package de.ginisolutions.trader.trading.web.rest;

import de.ginisolutions.trader.trading.domain.StrategistPackage;
import de.ginisolutions.trader.trading.repository.StrategistPackageRepository;
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
 * REST controller for managing {@link de.ginisolutions.trader.trading.domain.StrategistPackage}.
 */
@RestController
@RequestMapping("/api")
public class StrategistPackageResource {

    private final Logger log = LoggerFactory.getLogger(StrategistPackageResource.class);

    private static final String ENTITY_NAME = "tradingServiceStrategistPackage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StrategistPackageRepository strategistPackageRepository;

    public StrategistPackageResource(StrategistPackageRepository strategistPackageRepository) {
        this.strategistPackageRepository = strategistPackageRepository;
    }

    /**
     * {@code POST  /strategist-packages} : Create a new strategistPackage.
     *
     * @param strategistPackage the strategistPackage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new strategistPackage, or with status {@code 400 (Bad Request)} if the strategistPackage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/strategist-packages")
    public ResponseEntity<StrategistPackage> createStrategistPackage(@RequestBody StrategistPackage strategistPackage) throws URISyntaxException {
        log.debug("REST request to save StrategistPackage : {}", strategistPackage);
        if (strategistPackage.getId() != null) {
            throw new BadRequestAlertException("A new strategistPackage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StrategistPackage result = strategistPackageRepository.save(strategistPackage);
        return ResponseEntity.created(new URI("/api/strategist-packages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /strategist-packages} : Updates an existing strategistPackage.
     *
     * @param strategistPackage the strategistPackage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated strategistPackage,
     * or with status {@code 400 (Bad Request)} if the strategistPackage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the strategistPackage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/strategist-packages")
    public ResponseEntity<StrategistPackage> updateStrategistPackage(@RequestBody StrategistPackage strategistPackage) throws URISyntaxException {
        log.debug("REST request to update StrategistPackage : {}", strategistPackage);
        if (strategistPackage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StrategistPackage result = strategistPackageRepository.save(strategistPackage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, strategistPackage.getId()))
            .body(result);
    }

    /**
     * {@code GET  /strategist-packages} : get all the strategistPackages.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of strategistPackages in body.
     */
    @GetMapping("/strategist-packages")
    public List<StrategistPackage> getAllStrategistPackages() {
        log.debug("REST request to get all StrategistPackages");
        return strategistPackageRepository.findAll();
    }

    /**
     * {@code GET  /strategist-packages/:id} : get the "id" strategistPackage.
     *
     * @param id the id of the strategistPackage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the strategistPackage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/strategist-packages/{id}")
    public ResponseEntity<StrategistPackage> getStrategistPackage(@PathVariable String id) {
        log.debug("REST request to get StrategistPackage : {}", id);
        Optional<StrategistPackage> strategistPackage = strategistPackageRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(strategistPackage);
    }

    /**
     * {@code DELETE  /strategist-packages/:id} : delete the "id" strategistPackage.
     *
     * @param id the id of the strategistPackage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/strategist-packages/{id}")
    public ResponseEntity<Void> deleteStrategistPackage(@PathVariable String id) {
        log.debug("REST request to delete StrategistPackage : {}", id);

        strategistPackageRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
