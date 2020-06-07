package de.ginisolutions.trader.trading.web.rest;

import de.ginisolutions.trader.trading.service.TraderService;
import de.ginisolutions.trader.trading.service.dto.TraderPOST;
import de.ginisolutions.trader.trading.service.dto.TraderGET;
import de.ginisolutions.trader.trading.service.dto.TraderPUT;
import de.ginisolutions.trader.trading.web.rest.errors.BadRequestAlertException;
import de.ginisolutions.trader.trading.service.dto.TraderDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(TraderResource.class);

    private static final String ENTITY_NAME = "traderServiceTradingTrader";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TraderService traderService; // TODO user Platform to control Traders

    public TraderResource(TraderService traderService) {
        LOGGER.info("Constructing TraderResource");
        this.traderService = traderService;
    }

    /**
     * {@code POST  /traders} : Create a new trader.
     *
     * @param traderPOST the trader to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trader, or with status {@code 400 (Bad Request)} if the trader has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/traders")
    public ResponseEntity<TraderGET> createTrader(@Valid @RequestBody TraderPOST traderPOST) throws URISyntaxException {
        LOGGER.debug("REST request to save Trader : {}", traderPOST);
        TraderGET result = traderService.create(traderPOST);
        return ResponseEntity.created(new URI("/api/traders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /traders} : Updates an existing trader.
     *
     * @param traderPUT the trader to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated traderDTO,
     * or with status {@code 400 (Bad Request)} if the trader is not valid,
     * or with status {@code 500 (Internal Server Error)} if the traderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/traders")
    public ResponseEntity<TraderGET> updateTrader(@Valid @RequestBody TraderPUT traderPUT) throws URISyntaxException {
        LOGGER.debug("REST request to update Trader : {}", traderPUT);
        if (traderPUT.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TraderGET result = traderService.update(traderPUT);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, traderPUT.getId()))
            .body(result);
    }

    /**
     * {@code GET  /traders} : get all the traders.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of traders in body.
     */
    @GetMapping("/traders")
    public List<TraderGET> getAllTraders() {
        LOGGER.debug("REST request to get all Traders");
        return traderService.findAll();
    }

    /**
     * {@code GET  /traders/:id} : get the "id" trader.
     *
     * @param id the id of the traderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the traderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/traders/{id}")
    public ResponseEntity<TraderGET> getTrader(@PathVariable String id) {
        LOGGER.debug("REST request to get Trader : {}", id);
        Optional<TraderGET> traderGET = traderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(traderGET);
    }

    /**
     * {@code DELETE  /traders/:id} : delete the "id" trader.
     *
     * @param id the id of the traderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/traders/{id}")
    public ResponseEntity<Void> deleteTrader(@PathVariable String id) {
        LOGGER.debug("REST request to delete Trader : {}", id);

        traderService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    /**
     * @return an optional of the username of the currently authenticated user/principal
     */
    private static Optional<String> getUsernameFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<String> username = Optional.empty();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = Optional.of(userDetails.getUsername());
        }
        return username;
    }
}
