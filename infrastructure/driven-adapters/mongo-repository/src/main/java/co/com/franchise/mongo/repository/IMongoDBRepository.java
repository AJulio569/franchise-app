package co.com.franchise.mongo.repository;

import co.com.franchise.mongo.entity.FranchiseEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface IMongoDBRepository extends ReactiveMongoRepository<FranchiseEntity, String>, ICustomFranchiseMongoRepository{
    Mono<FranchiseEntity> findByName(String name);
}
