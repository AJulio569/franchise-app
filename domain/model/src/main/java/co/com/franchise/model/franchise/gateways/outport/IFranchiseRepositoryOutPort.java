package co.com.franchise.model.franchise.gateways.outport;

import co.com.franchise.model.franchise.model.Franchise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IFranchiseRepositoryOutPort {
    Mono<Franchise> save (Franchise franchise);

    Mono<Franchise> findById(String id);

    Mono<Franchise> findByName(String name);

    Flux<Franchise> findAll();

    Mono<Franchise> updateName(String id, String newName);
}
