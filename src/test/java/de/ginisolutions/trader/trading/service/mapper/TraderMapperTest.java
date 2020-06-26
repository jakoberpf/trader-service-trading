package de.ginisolutions.trader.trading.service.mapper;

import de.ginisolutions.trader.common.enumeration.INTERVAL;
import de.ginisolutions.trader.common.enumeration.MARKET;
import de.ginisolutions.trader.common.enumeration.SYMBOL;
import de.ginisolutions.trader.trading.domain.Trader;
import de.ginisolutions.trader.common.enumeration.STRATEGY;
import de.ginisolutions.trader.trading.service.dto.TraderDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class TraderMapperTest {

    private static final String DEFAULT_ID = "ID";

    private static final String ENTITY_NAME = "AAAAAAAAAA";
    private static final String CREATE_NAME = "BBBBBBBBBB";
    private static final String UPDATE_NAME = "CCCCCCCCCC";

    private static final String ENTITY_OWNER = "AAAAAAAAAA";
    private static final String CREATE_OWNER = "BBBBBBBBBB";
    private static final String UPDATE_OWNER = "CCCCCCCCCC";

    private static final MARKET ENTITY_MARKET = MARKET.BINANCE;
    private static final MARKET CREATE_MARKET = MARKET.BINANCE;
    private static final MARKET UPDATE_MARKET = MARKET.BINANCE;

    private static final SYMBOL ENTITY_SYMBOL = SYMBOL.BTCUSDT;
    private static final SYMBOL CREATE_SYMBOL = SYMBOL.LTCUSDT;
    private static final SYMBOL UPDATE_SYMBOL = SYMBOL.ETHUSDT;

    private static final INTERVAL ENTITY_INTERVAL = INTERVAL.ONE_MINUTE;
    private static final INTERVAL CREATE_INTERVAL = INTERVAL.HOURLY;
    private static final INTERVAL UPDATE_INTERVAL = INTERVAL.WEEKLY;

    private static final STRATEGY ENTITY_STRATEGY = STRATEGY.RSI;
    private static final STRATEGY CREATE_STRATEGY = STRATEGY.MM;
    private static final STRATEGY UPDATE_STRATEGY = STRATEGY.CCI;

    private static final String ENTITY_API_KEY = "AAAAAAAAAA";
    private static final String CREATE_API_KEY = "BBBBBBBBBB";
    private static final String UPDATE_API_KEY = "CCCCCCCCCC";

    private static final String ENTITY_API_SECRET = "AAAAAAAAAA";
    private static final String CREATE_API_SECRET = "BBBBBBBBBB";
    private static final String UPDATE_API_SECRET = "CCCCCCCCCC";

    private static final Boolean ENTITY_IS_LIVE = false;
    private static final Boolean CREATE_IS_LIVE = true;
    private static final Boolean UPDATE_IS_LIVE = false;

    private static final Boolean ENTITY_IS_IN = false;
    private static final Boolean CREATE_IS_IN = true;
    private static final Boolean UPDATE_IS_IN = false;

    private static final Double ENTITY_BUDGET = 1D;
    private static final Double CREATE_BUDGET = 2D;
    private static final Double UPDATE_BUDGET = 3D;

    private TraderMapper traderMapper;

    private Trader trader;

    private TraderDTO createTraderDTO;

    private TraderDTO updateTraderDTO;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trader createEntity() {
        final Trader trader = new Trader()
            .id(DEFAULT_ID)
            .name(ENTITY_NAME)
            .owner(ENTITY_OWNER)
            .market(ENTITY_MARKET)
            .symbol(ENTITY_SYMBOL)
            .interval(ENTITY_INTERVAL)
            .strategy(ENTITY_STRATEGY)
            .apiKey(ENTITY_API_KEY)
            .apiSecret(ENTITY_API_SECRET)
            .isLive(ENTITY_IS_LIVE)
            .isIn(ENTITY_IS_IN)
            .budget(ENTITY_BUDGET);
        trader.setTradeHistory(new ArrayList<>());
        return trader;
    }

    /**
     * Create an dto for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TraderDTO createCreateDTO() {
        TraderDTO traderDTO = new TraderDTO();
        traderDTO.setName(CREATE_NAME);
        traderDTO.setOwner(CREATE_OWNER);
        traderDTO.setMarket(CREATE_MARKET);
        traderDTO.setSymbol(CREATE_SYMBOL);
        traderDTO.setInterval(CREATE_INTERVAL);
        traderDTO.setStrategy(CREATE_STRATEGY);
        traderDTO.setApiKey(CREATE_API_KEY);
        traderDTO.setApiSecret(CREATE_API_SECRET);
        traderDTO.setLive(CREATE_IS_LIVE);
        traderDTO.setIn(CREATE_IS_IN);
        traderDTO.setBudget(CREATE_BUDGET);
        traderDTO.setTradeHistory(new ArrayList<>());
        return traderDTO;
    }

    /**
     * Create an dto for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TraderDTO createUpdateDTO() {
        TraderDTO traderDTO = new TraderDTO();
        traderDTO.setId(DEFAULT_ID);
        traderDTO.setName(UPDATE_NAME);
        traderDTO.setOwner(UPDATE_OWNER);
        traderDTO.setMarket(UPDATE_MARKET);
        traderDTO.setSymbol(UPDATE_SYMBOL);
        traderDTO.setInterval(UPDATE_INTERVAL);
        traderDTO.setStrategy(UPDATE_STRATEGY);
        traderDTO.setApiKey(UPDATE_API_KEY);
        traderDTO.setApiSecret(UPDATE_API_SECRET);
        traderDTO.setLive(UPDATE_IS_LIVE);
        traderDTO.setIn(UPDATE_IS_IN);
        traderDTO.setBudget(UPDATE_BUDGET);
        traderDTO.setTradeHistory(new ArrayList<>());
        return traderDTO;
    }

    @BeforeEach
    public void setUp() {
        this.traderMapper = new TraderMapperImpl();
    }

    @Test
    public void toEntityCreate() {
        TraderDTO traderDTO = createCreateDTO();
        Trader trader = this.traderMapper.toEntity(traderDTO);

        assertThat(trader.getId()).isNull();
        assertThat(trader.getName()).isEqualTo(CREATE_NAME);
        assertThat(trader.getOwner()).isEqualTo(CREATE_OWNER);
        assertThat(trader.getMarket()).isEqualTo(CREATE_MARKET);
        assertThat(trader.getSymbol()).isEqualTo(CREATE_SYMBOL);
        assertThat(trader.getInterval()).isEqualTo(CREATE_INTERVAL);
        assertThat(trader.getStrategy()).isEqualTo(CREATE_STRATEGY);
        assertThat(trader.getApiKey()).isEqualTo(CREATE_API_KEY);
        assertThat(trader.getApiSecret()).isEqualTo(CREATE_API_SECRET);
        assertThat(trader.isLive()).isEqualTo(CREATE_IS_LIVE);
        assertThat(trader.isIn()).isEqualTo(CREATE_IS_IN);
        assertThat(trader.getBudget()).isEqualTo(CREATE_BUDGET);
        assertThat(trader.getTradeHistory()).isNull();
    }

    @Test
    public void toEntityUpdate() {
        TraderDTO traderDTO = createUpdateDTO();
        Trader trader = this.traderMapper.toEntity(traderDTO);

        assertThat(trader.getId()).isEqualTo(DEFAULT_ID);
        assertThat(trader.getName()).isEqualTo(UPDATE_NAME);
        assertThat(trader.getOwner()).isEqualTo(UPDATE_OWNER);
        assertThat(trader.getMarket()).isEqualTo(UPDATE_MARKET);
        assertThat(trader.getSymbol()).isEqualTo(UPDATE_SYMBOL);
        assertThat(trader.getInterval()).isEqualTo(UPDATE_INTERVAL);
        assertThat(trader.getStrategy()).isEqualTo(UPDATE_STRATEGY);
        assertThat(trader.getApiKey()).isEqualTo(UPDATE_API_KEY);
        assertThat(trader.getApiSecret()).isEqualTo(UPDATE_API_SECRET);
        assertThat(trader.isLive()).isEqualTo(UPDATE_IS_LIVE);
        assertThat(trader.isIn()).isEqualTo(UPDATE_IS_IN);
        assertThat(trader.getBudget()).isEqualTo(UPDATE_BUDGET);
        assertThat(trader.getTradeHistory()).isNull();
    }

    @Test
    public void toDTO() {
        Trader trader = createEntity();
        TraderDTO traderDTO = this.traderMapper.toDto(trader);

        assertThat(traderDTO.getId()).isEqualTo(DEFAULT_ID);
        assertThat(traderDTO.getName()).isEqualTo(ENTITY_NAME);
        assertThat(traderDTO.getOwner()).isEqualTo(ENTITY_OWNER);
        assertThat(traderDTO.getMarket()).isEqualTo(ENTITY_MARKET);
        assertThat(traderDTO.getSymbol()).isEqualTo(ENTITY_SYMBOL);
        assertThat(traderDTO.getInterval()).isEqualTo(ENTITY_INTERVAL);
        assertThat(traderDTO.getStrategy()).isEqualTo(ENTITY_STRATEGY);
        assertThat(traderDTO.getApiKey()).isNull();
        assertThat(traderDTO.getApiSecret()).isNull();
        assertThat(traderDTO.isLive()).isEqualTo(ENTITY_IS_LIVE);
        assertThat(traderDTO.isIn()).isEqualTo(ENTITY_IS_IN);
        assertThat(traderDTO.getBudget()).isEqualTo(ENTITY_BUDGET);
        assertThat(trader.getTradeHistory()).isNotNull();
    }
}
