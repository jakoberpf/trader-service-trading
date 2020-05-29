package de.ginisolutions.trader.trading.repository;

import de.ginisolutions.trader.trading.domain.Strategist;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Strategist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StrategistRepository extends MongoRepository<Strategist, String> {
}
