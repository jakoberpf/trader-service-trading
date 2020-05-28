package de.ginisolutions.trader.trading.repository;

import de.ginisolutions.trader.trading.domain.StrategistPackage;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the StrategistPackage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StrategistPackageRepository extends MongoRepository<StrategistPackage, String> {
}
