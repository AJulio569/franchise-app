package co.com.franchise.mongo.repository;

import co.com.franchise.mongo.entity.FranchiseEntity;
import reactor.core.publisher.Mono;

public interface ICustomFranchiseMongoRepository {
    Mono<FranchiseEntity> findByNameIgnoreCase(String name);
}
