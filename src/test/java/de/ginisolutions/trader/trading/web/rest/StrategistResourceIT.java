package de.ginisolutions.trader.trading.web.rest;

import de.ginisolutions.trader.history.domain.enumeration.INTERVAL;
import de.ginisolutions.trader.history.domain.enumeration.MARKET;
import de.ginisolutions.trader.history.domain.enumeration.STRATEGY;
import de.ginisolutions.trader.history.domain.enumeration.SYMBOL;
import de.ginisolutions.trader.trading.TradingServiceApp;
import de.ginisolutions.trader.trading.config.TestSecurityConfiguration;
import de.ginisolutions.trader.trading.domain.Strategist;
import de.ginisolutions.trader.trading.repository.StrategistRepository;
import de.ginisolutions.trader.trading.service.StrategistService;
import de.ginisolutions.trader.trading.service.dto.StrategistDTO;
import de.ginisolutions.trader.trading.service.mapper.StrategistMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link StrategistResource} REST controller.
 */
@SpringBootTest(classes = { TradingServiceApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class StrategistResourceIT {

    private static final STRATEGY DEFAULT_STRATEGY = STRATEGY.SAMPLE_ENUM;
    private static final STRATEGY UPDATED_STRATEGY = STRATEGY.SAMPLE_ENUM;

    private static final MARKET DEFAULT_MARKET = MARKET.SAMPLE_ENUM;
    private static final MARKET UPDATED_MARKET = MARKET.SAMPLE_ENUM;

    private static final SYMBOL DEFAULT_SYMBOL = SYMBOL.SAMPLE_ENUM;
    private static final SYMBOL UPDATED_SYMBOL = SYMBOL.SAMPLE_ENUM;

    private static final INTERVAL DEFAULT_INTERVAL = INTERVAL.SAMPLE_ENUM;
    private static final INTERVAL UPDATED_INTERVAL = INTERVAL.SAMPLE_ENUM;

    @Autowired
    private StrategistRepository strategistRepository;

    @Autowired
    private StrategistMapper strategistMapper;

    @Autowired
    private StrategistService strategistService;

    @Autowired
    private MockMvc restStrategistMockMvc;

    private Strategist strategist;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Strategist createEntity() {
        Strategist strategist = new Strategist()
            .strategy(DEFAULT_STRATEGY)
            .market(DEFAULT_MARKET)
            .symbol(DEFAULT_SYMBOL)
            .interval(DEFAULT_INTERVAL);
        return strategist;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Strategist createUpdatedEntity() {
        Strategist strategist = new Strategist()
            .strategy(UPDATED_STRATEGY)
            .market(UPDATED_MARKET)
            .symbol(UPDATED_SYMBOL)
            .interval(UPDATED_INTERVAL);
        return strategist;
    }

    @BeforeEach
    public void initTest() {
        strategistRepository.deleteAll();
        strategist = createEntity();
    }

    @Test
    public void createStrategist() throws Exception {
        int databaseSizeBeforeCreate = strategistRepository.findAll().size();
        // Create the Strategist
        StrategistDTO strategistDTO = strategistMapper.toDto(strategist);
        restStrategistMockMvc.perform(post("/api/strategists").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(strategistDTO)))
            .andExpect(status().isCreated());

        // Validate the Strategist in the database
        List<Strategist> strategistList = strategistRepository.findAll();
        assertThat(strategistList).hasSize(databaseSizeBeforeCreate + 1);
        Strategist testStrategist = strategistList.get(strategistList.size() - 1);
        assertThat(testStrategist.getStrategy()).isEqualTo(DEFAULT_STRATEGY);
        assertThat(testStrategist.getMarket()).isEqualTo(DEFAULT_MARKET);
        assertThat(testStrategist.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testStrategist.getInterval()).isEqualTo(DEFAULT_INTERVAL);
    }

    @Test
    public void createStrategistWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = strategistRepository.findAll().size();

        // Create the Strategist with an existing ID
        strategist.setId("existing_id");
        StrategistDTO strategistDTO = strategistMapper.toDto(strategist);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStrategistMockMvc.perform(post("/api/strategists").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(strategistDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Strategist in the database
        List<Strategist> strategistList = strategistRepository.findAll();
        assertThat(strategistList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllStrategists() throws Exception {
        // Initialize the database
        strategistRepository.save(strategist);

        // Get all the strategistList
        restStrategistMockMvc.perform(get("/api/strategists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(strategist.getId())))
            .andExpect(jsonPath("$.[*].strategy").value(hasItem(DEFAULT_STRATEGY.toString())))
            .andExpect(jsonPath("$.[*].market").value(hasItem(DEFAULT_MARKET.toString())))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL.toString())))
            .andExpect(jsonPath("$.[*].interval").value(hasItem(DEFAULT_INTERVAL.toString())));
    }

    @Test
    public void getStrategist() throws Exception {
        // Initialize the database
        strategistRepository.save(strategist);

        // Get the strategist
        restStrategistMockMvc.perform(get("/api/strategists/{id}", strategist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(strategist.getId()))
            .andExpect(jsonPath("$.strategy").value(DEFAULT_STRATEGY.toString()))
            .andExpect(jsonPath("$.market").value(DEFAULT_MARKET.toString()))
            .andExpect(jsonPath("$.symbol").value(DEFAULT_SYMBOL.toString()))
            .andExpect(jsonPath("$.interval").value(DEFAULT_INTERVAL.toString()));
    }
    @Test
    public void getNonExistingStrategist() throws Exception {
        // Get the strategist
        restStrategistMockMvc.perform(get("/api/strategists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateStrategist() throws Exception {
        // Initialize the database
        strategistRepository.save(strategist);

        int databaseSizeBeforeUpdate = strategistRepository.findAll().size();

        // Update the strategist
        Strategist updatedStrategist = strategistRepository.findById(strategist.getId()).get();
        updatedStrategist
            .strategy(UPDATED_STRATEGY)
            .market(UPDATED_MARKET)
            .symbol(UPDATED_SYMBOL)
            .interval(UPDATED_INTERVAL);
        StrategistDTO strategistDTO = strategistMapper.toDto(updatedStrategist);

        restStrategistMockMvc.perform(put("/api/strategists").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(strategistDTO)))
            .andExpect(status().isOk());

        // Validate the Strategist in the database
        List<Strategist> strategistList = strategistRepository.findAll();
        assertThat(strategistList).hasSize(databaseSizeBeforeUpdate);
        Strategist testStrategist = strategistList.get(strategistList.size() - 1);
        assertThat(testStrategist.getStrategy()).isEqualTo(UPDATED_STRATEGY);
        assertThat(testStrategist.getMarket()).isEqualTo(UPDATED_MARKET);
        assertThat(testStrategist.getSymbol()).isEqualTo(UPDATED_SYMBOL);
        assertThat(testStrategist.getInterval()).isEqualTo(UPDATED_INTERVAL);
    }

    @Test
    public void updateNonExistingStrategist() throws Exception {
        int databaseSizeBeforeUpdate = strategistRepository.findAll().size();

        // Create the Strategist
        StrategistDTO strategistDTO = strategistMapper.toDto(strategist);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStrategistMockMvc.perform(put("/api/strategists").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(strategistDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Strategist in the database
        List<Strategist> strategistList = strategistRepository.findAll();
        assertThat(strategistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteStrategist() throws Exception {
        // Initialize the database
        strategistRepository.save(strategist);

        int databaseSizeBeforeDelete = strategistRepository.findAll().size();

        // Delete the strategist
        restStrategistMockMvc.perform(delete("/api/strategists/{id}", strategist.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Strategist> strategistList = strategistRepository.findAll();
        assertThat(strategistList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
