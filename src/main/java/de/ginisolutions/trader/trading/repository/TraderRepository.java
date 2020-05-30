package de.ginisolutions.trader.trading.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Trader entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TraderRepository extends MongoRepository<Trader, String> {
}
