package de.ginisolutions.trader.trading.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Trader Service Trading.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

//    @Value("${jhipster.application.trading.live}")
//    private boolean isLive; // TODO implement and test

}
