package de.ginisolutions.trader.trading.web.rest;

import de.ginisolutions.trader.common.enumeration.INTERVAL;
import de.ginisolutions.trader.common.enumeration.MARKET;
import de.ginisolutions.trader.common.enumeration.SYMBOL;
import de.ginisolutions.trader.trading.TraderServiceTradingApp;
import de.ginisolutions.trader.trading.config.TestSecurityConfiguration;
import de.ginisolutions.trader.trading.domain.Trader;
import de.ginisolutions.trader.common.enumeration.STRATEGY;
import de.ginisolutions.trader.trading.management.TraderManager;
import de.ginisolutions.trader.trading.repository.TraderRepository;
import de.ginisolutions.trader.trading.service.TraderService;
import de.ginisolutions.trader.trading.service.dto.TraderDTO;
import de.ginisolutions.trader.trading.service.dto.TraderPOST;
import de.ginisolutions.trader.trading.service.dto.TraderPUT;
import de.ginisolutions.trader.trading.service.mapper.TraderMapper;
import de.ginisolutions.trader.trading.service.mapper.TraderMapperGET;
import de.ginisolutions.trader.trading.service.mapper.TraderMapperPOST;
import de.ginisolutions.trader.trading.service.mapper.TraderMapperPUT;
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
@SpringBootTest(classes = {TraderServiceTradingApp.class, TestSecurityConfiguration.class})
@AutoConfigureMockMvc
@WithMockUser
public class TraderResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_OWNER = "BBBBBBBBBB";

    private static final MARKET DEFAULT_MARKET = MARKET.BINANCE;
    private static final MARKET UPDATED_MARKET = MARKET.COMDIRECT;

    private static final SYMBOL DEFAULT_SYMBOL = SYMBOL.BTCUSDT;
    private static final SYMBOL UPDATED_SYMBOL = SYMBOL.LTCUSDT;

    private static final INTERVAL DEFAULT_INTERVAL = INTERVAL.ONE_MINUTE;
    private static final INTERVAL UPDATED_INTERVAL = INTERVAL.HOURLY;

    private static final STRATEGY DEFAULT_STRATEGY = STRATEGY.RSI;
    private static final STRATEGY UPDATED_STRATEGY = STRATEGY.RSI;

    private static final String DEFAULT_API_KEY = "DefaultApiKey";
    private static final String UPDATED_API_KEY = "UpdateApiKey";

    private static final String DEFAULT_API_SECRET = "DefaultApiSecret";
    private static final String UPDATED_API_SECRET = "UpdateApiSecret";

    private static final Boolean DEFAULT_IS_LIVE = false;
    private static final Boolean UPDATED_IS_LIVE = true;

    private static final Boolean DEFAULT_IS_IN = false;
    private static final Boolean UPDATED_IS_IN = true;

    private static final Double DEFAULT_BUDGET = 1D;
    private static final Double UPDATED_BUDGET = 2D;

    @Autowired
    private TraderRepository traderRepository;

    @Autowired
    private TraderService traderService;

    @Autowired
    private TraderManager traderManager;

    @Autowired
    private TraderMapper traderMapper;

    @Autowired
    private TraderMapperPOST traderMapperPOST;

    @Autowired
    private TraderMapperGET traderMapperGET;

    @Autowired
    private TraderMapperPUT traderMapperPUT;

    @Autowired
    private MockMvc restTraderMockMvc;

    private Trader trader;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trader createEntity() {
        Trader trader = new Trader()
            .name(DEFAULT_NAME)
            .owner(DEFAULT_OWNER)
            .market(DEFAULT_MARKET)
            .symbol(DEFAULT_SYMBOL)
            .interval(DEFAULT_INTERVAL)
            .strategy(DEFAULT_STRATEGY)
            .apiKey(DEFAULT_API_KEY)
            .apiSecret(DEFAULT_API_SECRET)
            .isLive(DEFAULT_IS_LIVE)
            .isIn(DEFAULT_IS_IN)
            .budget(DEFAULT_BUDGET);
        return trader;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trader createUpdatedEntity() {
        Trader trader = new Trader()
            .name(UPDATED_NAME)
            .owner(UPDATED_OWNER)
            .market(UPDATED_MARKET)
            .symbol(UPDATED_SYMBOL)
            .interval(UPDATED_INTERVAL)
            .strategy(UPDATED_STRATEGY)
            .apiKey(UPDATED_API_KEY)
            .apiSecret(UPDATED_API_SECRET)
            .isLive(UPDATED_IS_LIVE)
            .isIn(UPDATED_IS_IN)
            .budget(UPDATED_BUDGET);
        return trader;
    }

    @BeforeEach
    public void initTest() {
        traderRepository.findAll().forEach(trader -> traderService.delete(trader.getId()));
        assertThat(traderRepository.count()).isEqualTo(0L);
        assertThat(traderManager.count()).isEqualTo(0);
        trader = createEntity();
    }

    @Test
    public void createTrader() throws Exception {
        int databaseSizeBeforeCreate = traderRepository.findAll().size();
        // Create the Trader
        TraderPOST traderPOST = traderMapperPOST.toDto(trader);

        // Perform REST request
        restTraderMockMvc.perform(post("/api/traders").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(traderPOST)))
            .andExpect(status().isCreated());

        // Validate the Trader in the database
        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeCreate + 1);
        Trader testTrader = traderList.get(traderList.size() - 1);
        assertThat(testTrader.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTrader.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testTrader.getMarket()).isEqualTo(DEFAULT_MARKET);
        assertThat(testTrader.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testTrader.getInterval()).isEqualTo(DEFAULT_INTERVAL);
        assertThat(testTrader.getStrategy()).isEqualTo(DEFAULT_STRATEGY);
        assertThat(testTrader.getApiKey()).isEqualTo(DEFAULT_API_KEY);
        assertThat(testTrader.getApiSecret()).isEqualTo(DEFAULT_API_SECRET);
        assertThat(testTrader.isLive()).isEqualTo(DEFAULT_IS_LIVE);
        assertThat(testTrader.isIn()).isEqualTo(DEFAULT_IS_IN);
        assertThat(testTrader.getBudget()).isEqualTo(DEFAULT_BUDGET);
    }

//    @Test
//    public void createTraderWithExistingId() throws Exception {
//        int databaseSizeBeforeCreate = traderRepository.findAll().size();
//
//        // Create the Trader with an existing ID
//        trader.setId("existing_id");
//        TraderDTO traderDTO = traderMapper.toDto(trader);
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restTraderMockMvc.perform(post("/api/traders").with(csrf())
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(TestUtil.convertObjectToJsonBytes(traderDTO)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the Trader in the database
//        List<Trader> traderList = traderRepository.findAll();
//        assertThat(traderList).hasSize(databaseSizeBeforeCreate);
//    }


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
    public void checkMarketIsRequired() throws Exception {
        int databaseSizeBeforeTest = traderRepository.findAll().size();
        // set the field null
        trader.setMarket(null);

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
    public void checkSymbolIsRequired() throws Exception {
        int databaseSizeBeforeTest = traderRepository.findAll().size();
        // set the field null
        trader.setSymbol(null);

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
    public void checkIsLiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = traderRepository.findAll().size();
        // set the field null
        trader.setLive(null);

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
    public void checkIsInIsRequired() throws Exception {
        int databaseSizeBeforeTest = traderRepository.findAll().size();
        // set the field null
        trader.setIn(null);

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
    public void checkBudgetIsRequired() throws Exception {
        int databaseSizeBeforeTest = traderRepository.findAll().size();
        // set the field null
        trader.setBudget(null);

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
            .andExpect(jsonPath("$.[*].market").value(hasItem(DEFAULT_MARKET.toString())))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL.toString())))
            .andExpect(jsonPath("$.[*].interval").value(hasItem(DEFAULT_INTERVAL.toString())))
            .andExpect(jsonPath("$.[*].strategy").value(hasItem(DEFAULT_STRATEGY.toString())))
            .andExpect(jsonPath("$.apiKey").doesNotExist())
            .andExpect(jsonPath("$.apiSecret").doesNotExist())
            .andExpect(jsonPath("$.[*].live").value(hasItem(DEFAULT_IS_LIVE)))
            .andExpect(jsonPath("$.[*].in").value(hasItem(DEFAULT_IS_IN)))
            .andExpect(jsonPath("$.[*].budget").value(hasItem(DEFAULT_BUDGET)));
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
            .andExpect(jsonPath("$.market").value(DEFAULT_MARKET.toString()))
            .andExpect(jsonPath("$.symbol").value(DEFAULT_SYMBOL.toString()))
            .andExpect(jsonPath("$.interval").value(DEFAULT_INTERVAL.toString()))
            .andExpect(jsonPath("$.strategy").value(DEFAULT_STRATEGY.toString()))
            .andExpect(jsonPath("$.apiKey").doesNotExist())
            .andExpect(jsonPath("$.apiSecret").doesNotExist())
            .andExpect(jsonPath("$.live").value(DEFAULT_IS_LIVE))
            .andExpect(jsonPath("$.in").value(DEFAULT_IS_IN))
            .andExpect(jsonPath("$.budget").value(DEFAULT_BUDGET));
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
        int databaseSizeBeforeInsert = traderRepository.findAll().size();

        final Trader insertedTrader = this.traderManager.add(trader);
        assertThat(traderRepository.count()).isEqualTo(databaseSizeBeforeInsert + 1);

        int databaseSizeBeforeUpdate = traderRepository.findAll().size();

        // Update the trader
        Trader updatedTrader = traderRepository.findById(insertedTrader.getId()).get();
        updatedTrader
            .name(UPDATED_NAME)
            .owner(DEFAULT_OWNER)
            .market(UPDATED_MARKET)
            .symbol(UPDATED_SYMBOL)
            .interval(UPDATED_INTERVAL)
            .strategy(UPDATED_STRATEGY)
            .apiKey(UPDATED_API_KEY)
            .apiSecret(UPDATED_API_SECRET)
            .isLive(UPDATED_IS_LIVE)
            .isIn(UPDATED_IS_IN)
            .budget(UPDATED_BUDGET);

        TraderPUT traderPUT = traderMapperPUT.toDto(updatedTrader);

        restTraderMockMvc.perform(put("/api/traders").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(traderPUT)))
            .andExpect(status().isOk());

        // Validate the Trader in the database
        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeUpdate);
        Trader testTrader = traderRepository.findById(insertedTrader.getId()).get();
        assertThat(testTrader.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTrader.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testTrader.getMarket()).isEqualTo(DEFAULT_MARKET);
        assertThat(testTrader.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testTrader.getInterval()).isEqualTo(DEFAULT_INTERVAL);
        assertThat(testTrader.getStrategy()).isEqualTo(UPDATED_STRATEGY);
        assertThat(testTrader.getApiKey()).isEqualTo(UPDATED_API_KEY);
        assertThat(testTrader.getApiSecret()).isEqualTo(UPDATED_API_SECRET);
        assertThat(testTrader.isLive()).isEqualTo(UPDATED_IS_LIVE);
        assertThat(testTrader.isIn()).isEqualTo(UPDATED_IS_IN);
        assertThat(testTrader.getBudget()).isEqualTo(UPDATED_BUDGET);
    }

    @Test
    public void updateTraderName() throws Exception {
        // Initialize the database
        int databaseSizeBeforeInsert = traderRepository.findAll().size();

        final Trader insertedTrader = this.traderManager.add(trader);
        assertThat(traderRepository.count()).isEqualTo(databaseSizeBeforeInsert + 1);

        int databaseSizeBeforeUpdate = traderRepository.findAll().size();

        TraderPUT traderPUT = new TraderPUT();
        traderPUT.setId(insertedTrader.getId());
        traderPUT.setName(UPDATED_NAME);
        traderPUT.setOwner(insertedTrader.getOwner());

        restTraderMockMvc.perform(put("/api/traders").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(traderPUT)))
            .andExpect(status().isOk());

        // Validate the Trader in the database
        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeUpdate);
        Trader testTrader = traderList.get(traderList.size() - 1);
        assertThat(testTrader.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTrader.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testTrader.getMarket()).isEqualTo(DEFAULT_MARKET);
        assertThat(testTrader.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testTrader.getInterval()).isEqualTo(DEFAULT_INTERVAL);
        assertThat(testTrader.getStrategy()).isEqualTo(DEFAULT_STRATEGY);
        assertThat(testTrader.getApiKey()).isEqualTo(DEFAULT_API_KEY);
        assertThat(testTrader.getApiSecret()).isEqualTo(DEFAULT_API_SECRET);
        assertThat(testTrader.isLive()).isEqualTo(DEFAULT_IS_LIVE);
        assertThat(testTrader.isIn()).isEqualTo(DEFAULT_IS_IN);
        assertThat(testTrader.getBudget()).isEqualTo(DEFAULT_BUDGET);
    }

    @Test
    public void updateTraderStrategy() throws Exception {
        // Initialize the database
        int databaseSizeBeforeInsert = traderRepository.findAll().size();

        final Trader insertedTrader = this.traderManager.add(trader);
        assertThat(traderRepository.count()).isEqualTo(databaseSizeBeforeInsert + 1);

        int databaseSizeBeforeUpdate = traderRepository.findAll().size();

        TraderPUT traderPUT = new TraderPUT();
        traderPUT.setId(insertedTrader.getId());
        traderPUT.setName(insertedTrader.getName());
        traderPUT.setOwner(insertedTrader.getOwner());
        traderPUT.setStrategy(UPDATED_STRATEGY);

        restTraderMockMvc.perform(put("/api/traders").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(traderPUT)))
            .andExpect(status().isOk());

        // Validate the Trader in the database
        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeUpdate);
        Trader testTrader = traderList.get(traderList.size() - 1);
        assertThat(testTrader.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTrader.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testTrader.getMarket()).isEqualTo(DEFAULT_MARKET);
        assertThat(testTrader.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testTrader.getInterval()).isEqualTo(DEFAULT_INTERVAL);
        assertThat(testTrader.getStrategy()).isEqualTo(UPDATED_STRATEGY);
        assertThat(testTrader.getApiKey()).isEqualTo(DEFAULT_API_KEY);
        assertThat(testTrader.getApiSecret()).isEqualTo(DEFAULT_API_SECRET);
        assertThat(testTrader.isLive()).isEqualTo(DEFAULT_IS_LIVE);
        assertThat(testTrader.isIn()).isEqualTo(DEFAULT_IS_IN);
        assertThat(testTrader.getBudget()).isEqualTo(DEFAULT_BUDGET);
    }

    @Test
    public void updateTraderApiKeyAndSecret() throws Exception {
        // Initialize the database
        int databaseSizeBeforeInsert = traderRepository.findAll().size();

        final Trader insertedTrader = this.traderManager.add(trader);
        assertThat(traderRepository.count()).isEqualTo(databaseSizeBeforeInsert + 1);

        int databaseSizeBeforeUpdate = traderRepository.findAll().size();

        TraderPUT traderPUT = new TraderPUT();
        traderPUT.setId(insertedTrader.getId());
        traderPUT.setName(insertedTrader.getName());
        traderPUT.setOwner(insertedTrader.getOwner());
        traderPUT.setApiKey(UPDATED_API_KEY);
        traderPUT.setApiSecret(UPDATED_API_SECRET);

        restTraderMockMvc.perform(put("/api/traders").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(traderPUT)))
            .andExpect(status().isOk());

        // Validate the Trader in the database
        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeUpdate);
        Trader testTrader = traderList.get(traderList.size() - 1);
        assertThat(testTrader.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTrader.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testTrader.getMarket()).isEqualTo(DEFAULT_MARKET);
        assertThat(testTrader.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testTrader.getInterval()).isEqualTo(DEFAULT_INTERVAL);
        assertThat(testTrader.getStrategy()).isEqualTo(DEFAULT_STRATEGY);
        assertThat(testTrader.getApiKey()).isEqualTo(UPDATED_API_KEY);
        assertThat(testTrader.getApiSecret()).isEqualTo(UPDATED_API_SECRET);
        assertThat(testTrader.isLive()).isEqualTo(DEFAULT_IS_LIVE);
        assertThat(testTrader.isIn()).isEqualTo(DEFAULT_IS_IN);
        assertThat(testTrader.getBudget()).isEqualTo(DEFAULT_BUDGET);
    }

    @Test
    public void updateTraderApiKeyWithoutSecret() throws Exception {
        // Initialize the database
        int databaseSizeBeforeInsert = traderRepository.findAll().size();

        final Trader insertedTrader = this.traderManager.add(trader);
        assertThat(traderRepository.count()).isEqualTo(databaseSizeBeforeInsert + 1);

        int databaseSizeBeforeUpdate = traderRepository.findAll().size();

        TraderPUT traderPUT = new TraderPUT();
        traderPUT.setId(insertedTrader.getId());
        traderPUT.setName(insertedTrader.getName());
        traderPUT.setOwner(insertedTrader.getOwner());
        traderPUT.setApiKey(UPDATED_API_KEY);

        restTraderMockMvc.perform(put("/api/traders").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(traderPUT)))
            .andExpect(status().isBadRequest());

        // Validate the Trader in the database
        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeUpdate);
        Trader testTrader = traderList.get(traderList.size() - 1);
        assertThat(testTrader.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTrader.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testTrader.getMarket()).isEqualTo(DEFAULT_MARKET);
        assertThat(testTrader.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testTrader.getInterval()).isEqualTo(DEFAULT_INTERVAL);
        assertThat(testTrader.getStrategy()).isEqualTo(DEFAULT_STRATEGY);
        assertThat(testTrader.getApiKey()).isEqualTo(DEFAULT_API_KEY);
        assertThat(testTrader.getApiSecret()).isEqualTo(DEFAULT_API_SECRET);
        assertThat(testTrader.isLive()).isEqualTo(DEFAULT_IS_LIVE);
        assertThat(testTrader.isIn()).isEqualTo(DEFAULT_IS_IN);
        assertThat(testTrader.getBudget()).isEqualTo(DEFAULT_BUDGET);
    }

    @Test
    public void updateTraderApiSecretWithoutKey() throws Exception {
        // Initialize the database
        int databaseSizeBeforeInsert = traderRepository.findAll().size();

        final Trader insertedTrader = this.traderManager.add(trader);
        assertThat(traderRepository.count()).isEqualTo(databaseSizeBeforeInsert + 1);

        int databaseSizeBeforeUpdate = traderRepository.findAll().size();

        TraderPUT traderPUT = new TraderPUT();
        traderPUT.setId(insertedTrader.getId());
        traderPUT.setName(insertedTrader.getName());
        traderPUT.setOwner(insertedTrader.getOwner());
        traderPUT.setApiSecret(UPDATED_API_SECRET);

        restTraderMockMvc.perform(put("/api/traders").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(traderPUT)))
            .andExpect(status().isBadRequest());

        // Validate the Trader in the database
        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeUpdate);
        Trader testTrader = traderList.get(traderList.size() - 1);
        assertThat(testTrader.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTrader.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testTrader.getMarket()).isEqualTo(DEFAULT_MARKET);
        assertThat(testTrader.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testTrader.getInterval()).isEqualTo(DEFAULT_INTERVAL);
        assertThat(testTrader.getStrategy()).isEqualTo(DEFAULT_STRATEGY);
        assertThat(testTrader.getApiKey()).isEqualTo(DEFAULT_API_KEY);
        assertThat(testTrader.getApiSecret()).isEqualTo(DEFAULT_API_SECRET);
        assertThat(testTrader.isLive()).isEqualTo(DEFAULT_IS_LIVE);
        assertThat(testTrader.isIn()).isEqualTo(DEFAULT_IS_IN);
        assertThat(testTrader.getBudget()).isEqualTo(DEFAULT_BUDGET);
    }

    // TODO updateTraderLive
    // TODO updateTraderIn
    // TODO updateTraderBudget

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
