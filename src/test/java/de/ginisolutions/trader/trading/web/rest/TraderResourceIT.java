package de.ginisolutions.trader.trading.web.rest;

import de.ginisolutions.trader.trading.TradingServiceApp;
import de.ginisolutions.trader.trading.config.TestSecurityConfiguration;
import de.ginisolutions.trader.trading.domain.Trader;
import de.ginisolutions.trader.trading.repository.TraderRepository;
import de.ginisolutions.trader.trading.service.TraderService;
import de.ginisolutions.trader.trading.service.dto.TraderDTO;
import de.ginisolutions.trader.trading.service.mapper.TraderMapper;

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
 * Integration tests for the {@link TraderResource} REST controller.
 */
@SpringBootTest(classes = { TradingServiceApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class TraderResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_OWNER = "BBBBBBBBBB";

    private static final String DEFAULT_API_KEY = "AAAAAAAAAA";
    private static final String UPDATED_API_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_API_SECRET = "AAAAAAAAAA";
    private static final String UPDATED_API_SECRET = "BBBBBBBBBB";

    private static final String DEFAULT_ATTRIBUTES = "AAAAAAAAAA";
    private static final String UPDATED_ATTRIBUTES = "BBBBBBBBBB";

    @Autowired
    private TraderRepository traderRepository;

    @Autowired
    private TraderMapper traderMapper;

    @Autowired
    private TraderService traderService;

    @Autowired
    private MockMvc restTraderMockMvc;

    private Trader trader;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trader createEntity() {
        Trader trader = new Trader()
            .name(DEFAULT_NAME)
            .owner(DEFAULT_OWNER)
            .apiKey(DEFAULT_API_KEY)
            .apiSecret(DEFAULT_API_SECRET)
            .attributes(DEFAULT_ATTRIBUTES);
        return trader;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trader createUpdatedEntity() {
        Trader trader = new Trader()
            .name(UPDATED_NAME)
            .owner(UPDATED_OWNER)
            .apiKey(UPDATED_API_KEY)
            .apiSecret(UPDATED_API_SECRET)
            .attributes(UPDATED_ATTRIBUTES);
        return trader;
    }

    @BeforeEach
    public void initTest() {
        traderRepository.deleteAll();
        trader = createEntity();
    }

    @Test
    public void createTrader() throws Exception {
        int databaseSizeBeforeCreate = traderRepository.findAll().size();
        // Create the Trader
        TraderDTO traderDTO = traderMapper.toDto(trader);
        restTraderMockMvc.perform(post("/api/traders").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(traderDTO)))
            .andExpect(status().isCreated());

        // Validate the Trader in the database
        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeCreate + 1);
        Trader testTrader = traderList.get(traderList.size() - 1);
        assertThat(testTrader.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTrader.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testTrader.getApiKey()).isEqualTo(DEFAULT_API_KEY);
        assertThat(testTrader.getApiSecret()).isEqualTo(DEFAULT_API_SECRET);
        assertThat(testTrader.getAttributes()).isEqualTo(DEFAULT_ATTRIBUTES);
    }

    @Test
    public void createTraderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = traderRepository.findAll().size();

        // Create the Trader with an existing ID
        trader.setId("existing_id");
        TraderDTO traderDTO = traderMapper.toDto(trader);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTraderMockMvc.perform(post("/api/traders").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(traderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Trader in the database
        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = traderRepository.findAll().size();
        // set the field null
        trader.setName(null);

        // Create the Trader, which fails.
        TraderDTO traderDTO = traderMapper.toDto(trader);


        restTraderMockMvc.perform(post("/api/traders").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(traderDTO)))
            .andExpect(status().isBadRequest());

        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkOwnerIsRequired() throws Exception {
        int databaseSizeBeforeTest = traderRepository.findAll().size();
        // set the field null
        trader.setOwner(null);

        // Create the Trader, which fails.
        TraderDTO traderDTO = traderMapper.toDto(trader);


        restTraderMockMvc.perform(post("/api/traders").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(traderDTO)))
            .andExpect(status().isBadRequest());

        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkApiKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = traderRepository.findAll().size();
        // set the field null
        trader.setApiKey(null);

        // Create the Trader, which fails.
        TraderDTO traderDTO = traderMapper.toDto(trader);


        restTraderMockMvc.perform(post("/api/traders").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(traderDTO)))
            .andExpect(status().isBadRequest());

        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkApiSecretIsRequired() throws Exception {
        int databaseSizeBeforeTest = traderRepository.findAll().size();
        // set the field null
        trader.setApiSecret(null);

        // Create the Trader, which fails.
        TraderDTO traderDTO = traderMapper.toDto(trader);


        restTraderMockMvc.perform(post("/api/traders").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(traderDTO)))
            .andExpect(status().isBadRequest());

        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkAttributesIsRequired() throws Exception {
        int databaseSizeBeforeTest = traderRepository.findAll().size();
        // set the field null
        trader.setAttributes(null);

        // Create the Trader, which fails.
        TraderDTO traderDTO = traderMapper.toDto(trader);


        restTraderMockMvc.perform(post("/api/traders").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(traderDTO)))
            .andExpect(status().isBadRequest());

        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllTraders() throws Exception {
        // Initialize the database
        traderRepository.save(trader);

        // Get all the traderList
        restTraderMockMvc.perform(get("/api/traders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trader.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)))
            .andExpect(jsonPath("$.[*].apiKey").value(hasItem(DEFAULT_API_KEY)))
            .andExpect(jsonPath("$.[*].apiSecret").value(hasItem(DEFAULT_API_SECRET)))
            .andExpect(jsonPath("$.[*].attributes").value(hasItem(DEFAULT_ATTRIBUTES)));
    }
    
    @Test
    public void getTrader() throws Exception {
        // Initialize the database
        traderRepository.save(trader);

        // Get the trader
        restTraderMockMvc.perform(get("/api/traders/{id}", trader.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trader.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER))
            .andExpect(jsonPath("$.apiKey").value(DEFAULT_API_KEY))
            .andExpect(jsonPath("$.apiSecret").value(DEFAULT_API_SECRET))
            .andExpect(jsonPath("$.attributes").value(DEFAULT_ATTRIBUTES));
    }
    @Test
    public void getNonExistingTrader() throws Exception {
        // Get the trader
        restTraderMockMvc.perform(get("/api/traders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateTrader() throws Exception {
        // Initialize the database
        traderRepository.save(trader);

        int databaseSizeBeforeUpdate = traderRepository.findAll().size();

        // Update the trader
        Trader updatedTrader = traderRepository.findById(trader.getId()).get();
        updatedTrader
            .name(UPDATED_NAME)
            .owner(UPDATED_OWNER)
            .apiKey(UPDATED_API_KEY)
            .apiSecret(UPDATED_API_SECRET)
            .attributes(UPDATED_ATTRIBUTES);
        TraderDTO traderDTO = traderMapper.toDto(updatedTrader);

        restTraderMockMvc.perform(put("/api/traders").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(traderDTO)))
            .andExpect(status().isOk());

        // Validate the Trader in the database
        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeUpdate);
        Trader testTrader = traderList.get(traderList.size() - 1);
        assertThat(testTrader.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTrader.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testTrader.getApiKey()).isEqualTo(UPDATED_API_KEY);
        assertThat(testTrader.getApiSecret()).isEqualTo(UPDATED_API_SECRET);
        assertThat(testTrader.getAttributes()).isEqualTo(UPDATED_ATTRIBUTES);
    }

    @Test
    public void updateNonExistingTrader() throws Exception {
        int databaseSizeBeforeUpdate = traderRepository.findAll().size();

        // Create the Trader
        TraderDTO traderDTO = traderMapper.toDto(trader);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTraderMockMvc.perform(put("/api/traders").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(traderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Trader in the database
        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteTrader() throws Exception {
        // Initialize the database
        traderRepository.save(trader);

        int databaseSizeBeforeDelete = traderRepository.findAll().size();

        // Delete the trader
        restTraderMockMvc.perform(delete("/api/traders/{id}", trader.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
