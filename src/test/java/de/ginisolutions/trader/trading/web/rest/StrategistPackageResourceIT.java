package de.ginisolutions.trader.trading.web.rest;

import de.ginisolutions.trader.trading.TradingServiceApp;
import de.ginisolutions.trader.trading.config.TestSecurityConfiguration;
import de.ginisolutions.trader.trading.domain.StrategistPackage;
import de.ginisolutions.trader.trading.repository.StrategistPackageRepository;

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
 * Integration tests for the {@link StrategistPackageResource} REST controller.
 */
@SpringBootTest(classes = { TradingServiceApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class StrategistPackageResourceIT {

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    @Autowired
    private StrategistPackageRepository strategistPackageRepository;

    @Autowired
    private MockMvc restStrategistPackageMockMvc;

    private StrategistPackage strategistPackage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StrategistPackage createEntity() {
        StrategistPackage strategistPackage = new StrategistPackage()
            .fieldName(DEFAULT_FIELD_NAME);
        return strategistPackage;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StrategistPackage createUpdatedEntity() {
        StrategistPackage strategistPackage = new StrategistPackage()
            .fieldName(UPDATED_FIELD_NAME);
        return strategistPackage;
    }

    @BeforeEach
    public void initTest() {
        strategistPackageRepository.deleteAll();
        strategistPackage = createEntity();
    }

    @Test
    public void createStrategistPackage() throws Exception {
        int databaseSizeBeforeCreate = strategistPackageRepository.findAll().size();
        // Create the StrategistPackage
        restStrategistPackageMockMvc.perform(post("/api/strategist-packages").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(strategistPackage)))
            .andExpect(status().isCreated());

        // Validate the StrategistPackage in the database
        List<StrategistPackage> strategistPackageList = strategistPackageRepository.findAll();
        assertThat(strategistPackageList).hasSize(databaseSizeBeforeCreate + 1);
        StrategistPackage testStrategistPackage = strategistPackageList.get(strategistPackageList.size() - 1);
        assertThat(testStrategistPackage.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
    }

    @Test
    public void createStrategistPackageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = strategistPackageRepository.findAll().size();

        // Create the StrategistPackage with an existing ID
        strategistPackage.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restStrategistPackageMockMvc.perform(post("/api/strategist-packages").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(strategistPackage)))
            .andExpect(status().isBadRequest());

        // Validate the StrategistPackage in the database
        List<StrategistPackage> strategistPackageList = strategistPackageRepository.findAll();
        assertThat(strategistPackageList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllStrategistPackages() throws Exception {
        // Initialize the database
        strategistPackageRepository.save(strategistPackage);

        // Get all the strategistPackageList
        restStrategistPackageMockMvc.perform(get("/api/strategist-packages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(strategistPackage.getId())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)));
    }
    
    @Test
    public void getStrategistPackage() throws Exception {
        // Initialize the database
        strategistPackageRepository.save(strategistPackage);

        // Get the strategistPackage
        restStrategistPackageMockMvc.perform(get("/api/strategist-packages/{id}", strategistPackage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(strategistPackage.getId()))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME));
    }
    @Test
    public void getNonExistingStrategistPackage() throws Exception {
        // Get the strategistPackage
        restStrategistPackageMockMvc.perform(get("/api/strategist-packages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateStrategistPackage() throws Exception {
        // Initialize the database
        strategistPackageRepository.save(strategistPackage);

        int databaseSizeBeforeUpdate = strategistPackageRepository.findAll().size();

        // Update the strategistPackage
        StrategistPackage updatedStrategistPackage = strategistPackageRepository.findById(strategistPackage.getId()).get();
        updatedStrategistPackage
            .fieldName(UPDATED_FIELD_NAME);

        restStrategistPackageMockMvc.perform(put("/api/strategist-packages").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedStrategistPackage)))
            .andExpect(status().isOk());

        // Validate the StrategistPackage in the database
        List<StrategistPackage> strategistPackageList = strategistPackageRepository.findAll();
        assertThat(strategistPackageList).hasSize(databaseSizeBeforeUpdate);
        StrategistPackage testStrategistPackage = strategistPackageList.get(strategistPackageList.size() - 1);
        assertThat(testStrategistPackage.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
    }

    @Test
    public void updateNonExistingStrategistPackage() throws Exception {
        int databaseSizeBeforeUpdate = strategistPackageRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStrategistPackageMockMvc.perform(put("/api/strategist-packages").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(strategistPackage)))
            .andExpect(status().isBadRequest());

        // Validate the StrategistPackage in the database
        List<StrategistPackage> strategistPackageList = strategistPackageRepository.findAll();
        assertThat(strategistPackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteStrategistPackage() throws Exception {
        // Initialize the database
        strategistPackageRepository.save(strategistPackage);

        int databaseSizeBeforeDelete = strategistPackageRepository.findAll().size();

        // Delete the strategistPackage
        restStrategistPackageMockMvc.perform(delete("/api/strategist-packages/{id}", strategistPackage.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StrategistPackage> strategistPackageList = strategistPackageRepository.findAll();
        assertThat(strategistPackageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
