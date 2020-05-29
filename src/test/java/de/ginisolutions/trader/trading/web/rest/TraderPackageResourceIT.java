package de.ginisolutions.trader.trading.web.rest;

import de.ginisolutions.trader.trading.TradingServiceApp;
import de.ginisolutions.trader.trading.config.TestSecurityConfiguration;
import de.ginisolutions.trader.trading.domain.TraderPackage;
import de.ginisolutions.trader.trading.repository.TraderPackageRepository;

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
 * Integration tests for the {@link TraderPackageResource} REST controller.
 */
@SpringBootTest(classes = { TradingServiceApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class TraderPackageResourceIT {

    private static final String DEFAULT_DUMMY = "AAAAAAAAAA";
    private static final String UPDATED_DUMMY = "BBBBBBBBBB";

    @Autowired
    private TraderPackageRepository traderPackageRepository;

    @Autowired
    private MockMvc restTraderPackageMockMvc;

    private TraderPackage traderPackage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TraderPackage createEntity() {
        TraderPackage traderPackage = new TraderPackage()
            .dummy(DEFAULT_DUMMY);
        return traderPackage;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TraderPackage createUpdatedEntity() {
        TraderPackage traderPackage = new TraderPackage()
            .dummy(UPDATED_DUMMY);
        return traderPackage;
    }

    @BeforeEach
    public void initTest() {
        traderPackageRepository.deleteAll();
        traderPackage = createEntity();
    }

    @Test
    public void createTraderPackage() throws Exception {
        int databaseSizeBeforeCreate = traderPackageRepository.findAll().size();
        // Create the TraderPackage
        restTraderPackageMockMvc.perform(post("/api/trader-packages").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(traderPackage)))
            .andExpect(status().isCreated());

        // Validate the TraderPackage in the database
        List<TraderPackage> traderPackageList = traderPackageRepository.findAll();
        assertThat(traderPackageList).hasSize(databaseSizeBeforeCreate + 1);
        TraderPackage testTraderPackage = traderPackageList.get(traderPackageList.size() - 1);
        assertThat(testTraderPackage.getDummy()).isEqualTo(DEFAULT_DUMMY);
    }

    @Test
    public void createTraderPackageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = traderPackageRepository.findAll().size();

        // Create the TraderPackage with an existing ID
        traderPackage.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restTraderPackageMockMvc.perform(post("/api/trader-packages").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(traderPackage)))
            .andExpect(status().isBadRequest());

        // Validate the TraderPackage in the database
        List<TraderPackage> traderPackageList = traderPackageRepository.findAll();
        assertThat(traderPackageList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllTraderPackages() throws Exception {
        // Initialize the database
        traderPackageRepository.save(traderPackage);

        // Get all the traderPackageList
        restTraderPackageMockMvc.perform(get("/api/trader-packages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(traderPackage.getId())))
            .andExpect(jsonPath("$.[*].dummy").value(hasItem(DEFAULT_DUMMY)));
    }
    
    @Test
    public void getTraderPackage() throws Exception {
        // Initialize the database
        traderPackageRepository.save(traderPackage);

        // Get the traderPackage
        restTraderPackageMockMvc.perform(get("/api/trader-packages/{id}", traderPackage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(traderPackage.getId()))
            .andExpect(jsonPath("$.dummy").value(DEFAULT_DUMMY));
    }
    @Test
    public void getNonExistingTraderPackage() throws Exception {
        // Get the traderPackage
        restTraderPackageMockMvc.perform(get("/api/trader-packages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateTraderPackage() throws Exception {
        // Initialize the database
        traderPackageRepository.save(traderPackage);

        int databaseSizeBeforeUpdate = traderPackageRepository.findAll().size();

        // Update the traderPackage
        TraderPackage updatedTraderPackage = traderPackageRepository.findById(traderPackage.getId()).get();
        updatedTraderPackage
            .dummy(UPDATED_DUMMY);

        restTraderPackageMockMvc.perform(put("/api/trader-packages").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTraderPackage)))
            .andExpect(status().isOk());

        // Validate the TraderPackage in the database
        List<TraderPackage> traderPackageList = traderPackageRepository.findAll();
        assertThat(traderPackageList).hasSize(databaseSizeBeforeUpdate);
        TraderPackage testTraderPackage = traderPackageList.get(traderPackageList.size() - 1);
        assertThat(testTraderPackage.getDummy()).isEqualTo(UPDATED_DUMMY);
    }

    @Test
    public void updateNonExistingTraderPackage() throws Exception {
        int databaseSizeBeforeUpdate = traderPackageRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTraderPackageMockMvc.perform(put("/api/trader-packages").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(traderPackage)))
            .andExpect(status().isBadRequest());

        // Validate the TraderPackage in the database
        List<TraderPackage> traderPackageList = traderPackageRepository.findAll();
        assertThat(traderPackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteTraderPackage() throws Exception {
        // Initialize the database
        traderPackageRepository.save(traderPackage);

        int databaseSizeBeforeDelete = traderPackageRepository.findAll().size();

        // Delete the traderPackage
        restTraderPackageMockMvc.perform(delete("/api/trader-packages/{id}", traderPackage.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TraderPackage> traderPackageList = traderPackageRepository.findAll();
        assertThat(traderPackageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
