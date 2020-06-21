package de.ginisolutions.trader.trading.web.rest;

import de.ginisolutions.trader.trading.service.TraderService;
import de.ginisolutions.trader.trading.service.dto.TraderGET;
import de.ginisolutions.trader.trading.service.dto.TraderPOST;
import de.ginisolutions.trader.trading.service.dto.TraderPUT;
import de.ginisolutions.trader.trading.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST admin controller for managing {@link de.ginisolutions.trader.trading.domain.Trader}.
 */
@RestController
@RequestMapping("/api/admin")
public class TraderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TraderController.class);

    private static final String ENTITY_NAME = "traderServiceTradingTrader";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TraderService traderService;

    public TraderController(TraderService traderService) {
        LOGGER.info("Constructing TraderResource");
        this.traderService = traderService;
    }

    // TODO start/stop
}
