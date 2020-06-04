package de.ginisolutions.trader.trading.repository;

import de.ginisolutions.trader.trading.domain.Trader;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the Trader entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TraderRepository extends MongoRepository<Trader, String> {
    List<Trader> findAllByOwner(String owner);
}
