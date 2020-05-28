package de.ginisolutions.trader.trading.repository;

import de.ginisolutions.trader.trading.domain.TraderPackage;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the TraderPackage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TraderPackageRepository extends MongoRepository<TraderPackage, String> {
}
