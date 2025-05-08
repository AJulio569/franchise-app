package co.com.franchise.usecase.franchise.usecase;

import co.com.franchise.model.franchise.gateways.outport.IFranchiseRepositoryOutPort;
import co.com.franchise.model.franchise.model.Branch;
import co.com.franchise.model.franchise.model.Franchise;
import co.com.franchise.model.franchise.model.Product;
import co.com.franchise.usecase.franchise.exception.EntityAlreadyExistsException;
import co.com.franchise.usecase.franchise.exception.EntityNotFoundException;
import co.com.franchise.model.franchise.gateways.intport.IFranchiseServiceIntPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;
import java.util.Comparator;

public class FranchiseUseCase implements IFranchiseServiceIntPort {
    private final IFranchiseRepositoryOutPort repository;

    public FranchiseUseCase(IFranchiseRepositoryOutPort repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Franchise> createFranchise(Franchise franchise) {
        return repository.findByName(franchise.getName())
                .flatMap(existing ->
                        Mono.<Franchise>error(new EntityAlreadyExistsException("Franchise already exists with name: " + existing.getName()))
                )
                .switchIfEmpty(repository.save(franchise));
    }

    @Override
    public Mono<Franchise> getFranchiseById(String franchiseId) {
        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new
                        EntityNotFoundException("Franchise not found with id: " + franchiseId)));
    }

    @Override
    public Mono<Franchise> getFranchiseByName(String name) {
        return repository.findByName(normalizeName(name))
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Franchise not found with name: " + name)));
    }

    @Override
    public Flux<Franchise> getAllFranchise() {
        return repository.findAll()
                .switchIfEmpty(Flux.error(new
                        EntityNotFoundException("No franchises found")));
    }

    @Override
    public Mono<Franchise> updateFranchiseName(String franchiseId, String newName) {
        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Franchise not found with id: " + franchiseId)))
                .flatMap(existingFranchise ->

                        repository.findAll()
                                .filter(f -> !f.getId().equalsIgnoreCase(franchiseId) &&
                                        normalizeName(f.getName()).equals(normalizeName(newName)))
                                .hasElements()
                                .flatMap(exists -> {
                                    if (exists) {
                                        return Mono.error(new EntityAlreadyExistsException("Franchise already exists with name: " + newName));
                                    }
                                    return repository.updateName(franchiseId, newName.trim());
                                })
                );
    }

    @Override
    public Mono<Franchise> addBranchToFranchise(String franchiseId, Branch newBranch) {
        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Franchise not found with id: " + franchiseId)))
                .flatMap(franchise -> {
                    String normalizedNewName = normalizeName(newBranch.getName());

                    boolean branchExists = franchise.getBranches().stream()
                            .map(branch -> normalizeName(branch.getName()))
                            .anyMatch(name -> name.equals(normalizedNewName));

                    if (branchExists) {
                        return Mono.error(new EntityAlreadyExistsException(
                                "Branch already exists with name: " + newBranch.getName()));
                    }
                    franchise.getBranches().add(newBranch);
                    return repository.save(franchise);
                });
    }

    @Override
    public Mono<Product> addProductToBranch(String franchiseId, String branchName, Product product) {
        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Franchise not found with id: " + franchiseId)))
                .flatMap(franchise -> {
                    Branch branch =franchise.getBranches().stream()
                            .filter(branch1 -> branch1.getName().equalsIgnoreCase(branchName))
                            .findFirst()
                            .orElseThrow(() -> new EntityNotFoundException("Branch not found with name: " + branchName));

                    boolean productExists = branch.getProducts().stream()
                            .anyMatch(product1 -> product1.getName().equalsIgnoreCase(product.getName()));

                    if (productExists){
                        return Mono.error(new EntityAlreadyExistsException("Product already exists with name: "+product.getName()));
                    }

                    branch.getProducts().add(product);
                    return  repository.save(franchise)
                            .thenReturn(product);
                });
    }

    @Override
    public Mono<Void> deleteProductFromBranch(String franchiseId, String branchName, String productName) {
        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Franchise not found with id: " + franchiseId)))
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(branch1 -> branch1.getName().equalsIgnoreCase(branchName))
                            .findFirst()
                            .orElseThrow(()-> new EntityNotFoundException("Branch not found with name: " + branchName));
                    boolean removed = branch.getProducts().removeIf(
                            product -> product.getName().equalsIgnoreCase(productName));
                    if (!removed){
                        return  Mono.error(new EntityNotFoundException("Product not found with name: " + productName));
                    }
                    return  repository.save(franchise).then();

                });
    }

    @Override
    public Mono<Product> updateProductStock(String franchiseId, String branchName, String productName, Integer newStock) {
        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Franchise not found with id: " + franchiseId)))
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter( branch1 -> branch1.getName().equalsIgnoreCase(branchName))
                            .findFirst()
                            .orElseThrow(() -> new EntityNotFoundException("Branch not found with name: " + branchName) );

                    Product updateProduct =branch.getProducts().stream()
                            .filter(product -> product.getName().equalsIgnoreCase(productName))
                            .findFirst()
                            .orElseThrow(()-> new EntityNotFoundException("Product not found with name: " + productName));

                    updateProduct.setStock(newStock);
                    return  repository.save(franchise)
                            .thenReturn(updateProduct);
                });
    }

    @Override
    public Flux<Tuple2<String, Product>> getTopStockProductByBranch(String franchiseId) {
        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Franchise not found with id: " + franchiseId)))
                .flatMapMany(franchise -> Flux.fromIterable(franchise.getBranches()))
                .flatMap(branch -> Mono.justOrEmpty(
                        branch.getProducts().stream()
                                .max(Comparator.comparing(Product::getStock))
                                .map(product -> Tuples.of(branch.getName(),product))));
    }

    @Override
    public Mono<Branch> updateBranchName(String franchiseId, String currentBranchName, String newBranchName) {
        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Franchise not found with id: " + franchiseId)))
                .flatMap(franchise -> {
                    Branch targetBranch = franchise.getBranches().stream()
                            .filter(branch -> normalizeName(branch.getName()).equals(normalizeName(currentBranchName)))
                            .findFirst()
                            .orElseThrow(() -> new EntityNotFoundException("Branch not found with name: " + currentBranchName));

                    boolean branchNameExists = franchise.getBranches().stream()
                            .filter(branch -> !normalizeName(branch.getName()).equals(normalizeName(currentBranchName)))
                            .anyMatch(branch -> normalizeName(branch.getName()).equals(normalizeName(newBranchName)));

                    if (branchNameExists) {
                        return Mono.error(new EntityAlreadyExistsException("Branch already exists with name: " + newBranchName));
                    }

                    targetBranch.setName(newBranchName.trim());
                    return repository.save(franchise).thenReturn(targetBranch);
                });
    }

    @Override
    public Mono<Product> updateProductName(String franchiseId, String branchName, String currentProductName, String newProductName) {
        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Franchise not found with id: " + franchiseId)))
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> normalizeName(b.getName()).equals(normalizeName(branchName)))
                            .findFirst()
                            .orElseThrow(() -> new EntityNotFoundException("Branch not found with name: " + branchName));

                    Product product = branch.getProducts().stream()
                            .filter(p -> normalizeName(p.getName()).equals(normalizeName(currentProductName)))
                            .findFirst()
                            .orElseThrow(() -> new EntityNotFoundException("Product not found with name: " + currentProductName));

                    boolean productNameExists = branch.getProducts().stream()
                            .filter(p -> !normalizeName(p.getName()).equals(normalizeName(currentProductName)))
                            .anyMatch(p -> normalizeName(p.getName()).equals(normalizeName(newProductName)));

                    if (productNameExists) {
                        return Mono.error(new EntityAlreadyExistsException("Product already exists with name: " + newProductName));
                    }

                    product.setName(newProductName.trim());
                    return repository.save(franchise).thenReturn(product);
                });
    }

    private String normalizeName(String name) {
        return name.trim().toLowerCase();
    }
}
