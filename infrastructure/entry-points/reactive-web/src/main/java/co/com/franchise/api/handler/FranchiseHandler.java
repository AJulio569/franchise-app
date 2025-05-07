package co.com.franchise.api.handler;

import co.com.franchise.api.dto.request.FranchiseIdRequest;
import co.com.franchise.api.dto.request.FranchiseRequest;
import co.com.franchise.api.dto.request.BranchRequest;
import co.com.franchise.api.dto.request.FranchiseBranchRequest;
import co.com.franchise.api.dto.request.ProductRequest;
import co.com.franchise.api.dto.request.FranchiseProductRequest;
import co.com.franchise.api.dto.request.UpdateStockRequest;
import co.com.franchise.api.dto.request.UpdateBranchNameRequest;
import co.com.franchise.api.dto.request.UpdateProductNameRequest;
import co.com.franchise.api.dto.response.TopStockProductResponse;
import co.com.franchise.api.mapper.IFranchiseMapperDto;
import co.com.franchise.model.franchise.gateways.intport.IFranchiseServiceIntPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FranchiseHandler {
    private final IFranchiseServiceIntPort servicePort;
    private final IFranchiseMapperDto mapperDto;
    private final ValidatorHandler validatorHandler;

    public Mono<ServerResponse> createFranchise(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(FranchiseRequest.class)
                .flatMap(validatorHandler::validate)
                .map(mapperDto::toDomain)
                .flatMap(servicePort::createFranchise)
                .map(mapperDto::toResponse)
                .flatMap(response ->
                        ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response)
                        .switchIfEmpty(ServerResponse.notFound().build()));
    }

    public Mono<ServerResponse> getAllFranchise(ServerRequest request){
        return servicePort.getAllFranchise()
                .map(mapperDto::toResponse)
                .collectList()
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response)
                        .switchIfEmpty(ServerResponse.notFound().build()));
    }

    public Mono<ServerResponse> getFranchiseById(ServerRequest request) {
        return Mono.just( new FranchiseIdRequest(request.pathVariable("franchiseId")))
                .flatMap(validatorHandler::validate)
                .flatMap(validateId ->
                        servicePort.getFranchiseById(validateId.getId()))
                .map(mapperDto::toResponse)
                .flatMap(dto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dto))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> updateFranchise(ServerRequest request) {
        return Mono.just(new FranchiseIdRequest(request.pathVariable("franchiseId")))
                .flatMap(validatorHandler::validate)
                .flatMap(validId ->
                        request.bodyToMono(FranchiseRequest.class)
                                .flatMap(validatorHandler::validate)
                                .map(mapperDto::toDomain)
                                .flatMap(franchise ->
                                        servicePort.updateFranchiseName(
                                        validId.getId(),
                                        franchise.getName()))
                                .map(mapperDto::toResponse)
                                .flatMap(response -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(response))
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> addBranchToFranchise(ServerRequest request) {
        return Mono.just(new FranchiseIdRequest(request.pathVariable("franchiseId")))
                .flatMap(validatorHandler::validate)
                .flatMap(validRequest ->
                        request.bodyToMono(BranchRequest.class)
                                .flatMap(validatorHandler::validate)
                                .map(mapperDto::toDomain)
                                .flatMap(newBranch ->
                                        servicePort.addBranchToFranchise(validRequest.getId(), newBranch))
                                .map(mapperDto::toResponse)
                                .flatMap(response -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(response)));
    }

    public Mono<ServerResponse> addProductToBranch(ServerRequest request) {
        return Mono.just(new FranchiseBranchRequest(
                        request.pathVariable("franchiseId"),
                        request.pathVariable("branchName")))
                .flatMap(validatorHandler::validate)
                .flatMap(validRequest ->
                        request.bodyToMono(ProductRequest.class)
                                .flatMap(validatorHandler::validate)
                                .map(mapperDto::toDomain)
                                .flatMap(product -> servicePort.addProductToBranch(
                                        validRequest.getFranchiseId(),
                                        validRequest.getBranchName(),
                                        product
                                ))
                                .map(mapperDto::toResponse)
                                .flatMap(response -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(response))
                );
    }

    public Mono<ServerResponse> deleteProductFromBranch(ServerRequest request){
        return  Mono.just(new FranchiseProductRequest(
                request.pathVariable("franchiseId"),
                request.pathVariable("branchName"),
                request.pathVariable("productName")))
                .flatMap(validatorHandler::validate)
                .flatMap(validRequest ->
                        servicePort.deleteProductFromBranch(
                                validRequest.getFranchiseId(),
                                validRequest.getBranchName(),
                                validRequest.getProductName()
                        ).then(ServerResponse.noContent().build())
                );
    }

    public Mono<ServerResponse> updateProductStock(ServerRequest request){
        return  Mono.just(new FranchiseProductRequest(
                request.pathVariable("franchiseId"),
                request.pathVariable("branchName"),
                request.pathVariable("productName")))
                .flatMap(validatorHandler::validate)
                .flatMap(validRequest ->
                        request.bodyToMono(UpdateStockRequest.class)
                                .flatMap(validatorHandler::validate)
                                .flatMap(stockRequest ->
                                        servicePort.updateProductStock(
                                                validRequest.getFranchiseId(),
                                                validRequest.getBranchName(),
                                                validRequest.getProductName(),
                                                stockRequest.getNewStock())
                                )
                                .map(mapperDto::toResponse)
                                .flatMap(response -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(response))
                );
    }

    public Mono<ServerResponse> getTopStockProductByBranch(ServerRequest request) {
        return Mono.just(new FranchiseIdRequest(request.pathVariable("franchiseId")))
                .flatMap(validatorHandler::validate)
                .flatMap(validRequest ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(servicePort.getTopStockProductByBranch(validRequest.getId())
                                                .map(tuple ->
                                                        mapperDto.toTopStockProductResponse(tuple.getT1(), tuple.getT2())),
                                        TopStockProductResponse.class)
                );
    }

    public Mono<ServerResponse> updateBranchName(ServerRequest request) {
        return Mono.just(new FranchiseBranchRequest(
                        request.pathVariable("franchiseId"),
                        request.pathVariable("branchName")))
                .flatMap(validatorHandler::validate)
                .flatMap(validRequest ->
                        request.bodyToMono(UpdateBranchNameRequest.class)
                                .flatMap(validatorHandler::validate)
                                .flatMap(updateRequest ->
                                        servicePort.updateBranchName(
                                                validRequest.getFranchiseId(),
                                                validRequest.getBranchName(),
                                                updateRequest.getNewName()))
                                .map(mapperDto::toResponse)
                                .flatMap(response -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(response))
                                .switchIfEmpty(ServerResponse.notFound().build())
                );
    }

    public Mono<ServerResponse> updateProductName(ServerRequest request) {
        return Mono.just(new FranchiseProductRequest(
                request.pathVariable("franchiseId"),
                        request.pathVariable("branchName"),
                        request.pathVariable("productName")))
                .flatMap(validatorHandler::validate)
                .flatMap(validProductIdRequest ->
                        request.bodyToMono(UpdateProductNameRequest.class)
                                .flatMap(validatorHandler::validate)
                                .flatMap(updateRequest ->
                                        servicePort.updateProductName(
                                                validProductIdRequest.getFranchiseId(),
                                                validProductIdRequest.getBranchName(),
                                                validProductIdRequest.getProductName(),
                                                updateRequest.getNewName()
                                        )
                                )
                                .map(mapperDto::toResponse)
                                .flatMap(response -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(response))
                                .switchIfEmpty(ServerResponse.notFound().build())
                );
    }
}
