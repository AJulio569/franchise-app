package co.com.franchise.mongo.impl;

import co.com.franchise.mongo.entity.FranchiseEntity;
import co.com.franchise.mongo.repository.ICustomFranchiseMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class CustomFranchiseMongoRepositoryImpl implements ICustomFranchiseMongoRepository {
    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<FranchiseEntity> findByNameIgnoreCase(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name.trim()));

        Collation collation = Collation.of("en")
                .strength(Collation.ComparisonLevel.secondary());

        query.collation(collation);
        return mongoTemplate.findOne(query, FranchiseEntity.class);
    }
}
