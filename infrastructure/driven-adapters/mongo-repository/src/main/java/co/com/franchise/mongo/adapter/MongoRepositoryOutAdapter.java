package co.com.franchise.mongo.adapter;

import co.com.franchise.model.franchise.gateways.outport.IFranchiseRepositoryOutPort;
import co.com.franchise.model.franchise.model.Franchise;
import co.com.franchise.mongo.mapper.IFranchiseMapper;
import co.com.franchise.mongo.repository.IMongoDBRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class MongoRepositoryOutAdapter implements IFranchiseRepositoryOutPort {
    private final IMongoDBRepository repository;
    private final IFranchiseMapper mapper;

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return repository.save(mapper.toEntity(franchise))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Franchise> findById(String id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Franchise> findByName(String name) {
        return repository.findByNameIgnoreCase(name)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Franchise> findAll() {
        return repository.findAll()
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Franchise> updateName(String id, String newName) {
        return repository.findById(id)
                .flatMap(franchiseEntity -> {
                    franchiseEntity.setName(newName);
                    return repository.save(franchiseEntity);
                }).map(mapper::toDomain);
    }
}
