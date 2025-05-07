package co.com.franchise.model.franchise.gateways.intport;

import co.com.franchise.model.franchise.model.Branch;
import co.com.franchise.model.franchise.model.Franchise;
import co.com.franchise.model.franchise.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

public interface IFranchiseServiceIntPort {
    Mono<Franchise> createFranchise(Franchise franchise);

    Mono<Franchise> getFranchiseById(String franchiseId);

    Mono<Franchise> getFranchiseByName(String name);

    Flux<Franchise> getAllFranchise();

    Mono<Franchise> updateFranchiseName(String franchiseId, String newName);

    Mono<Franchise> addBranchToFranchise(String franchiseId, Branch newBranch);

    Mono<Product> addProductToBranch(String franchiseId, String branchName, Product product);

    Mono<Void> deleteProductFromBranch(String franchiseId, String branchName,String productName);

    Mono<Product> updateProductStock(String franchiseId, String branchName, String productName, Integer newStock);

    Flux<Tuple2<String, Product>> getTopStockProductByBranch(String franchiseId);

    Mono<Branch> updateBranchName(String franchiseId, String currentBranchName, String newBranchName);

    Mono<Product> updateProductName(String franchiseId, String branchName, String currentProductName, String newProductName);
}
