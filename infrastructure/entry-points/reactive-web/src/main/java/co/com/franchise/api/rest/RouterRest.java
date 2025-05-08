package co.com.franchise.api.rest;

import co.com.franchise.api.handler.FranchiseHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(FranchiseHandler franchiseHandler) {
        return route(GET("/api/franchises"), franchiseHandler::getAllFranchise)
                .andRoute(POST("/api/franchises"), franchiseHandler::createFranchise)
                .andRoute(GET("/api/franchises/{franchiseId}"), franchiseHandler::getFranchiseById)
                .andRoute(PUT("/api/franchises/{franchiseId}"), franchiseHandler::updateFranchise)
                .andRoute(POST("/api/franchises/{franchiseId}/branches"), franchiseHandler::addBranchToFranchise)
                .andRoute(POST("/api/franchises/{franchiseId}/branches/{branchName}/products"), franchiseHandler::addProductToBranch)
                .andRoute(DELETE("/api/franchises/{franchiseId}/branches/{branchName}/products/{productName}"), franchiseHandler::deleteProductFromBranch)
                .andRoute(PUT("/api/franchises/{franchiseId}/branches/{branchName}/products/{productName}/stock"), franchiseHandler::updateProductStock)
                .andRoute(GET("/api/franchises/{franchiseId}/products/top-stock"), franchiseHandler::getTopStockProductByBranch)
                .andRoute(PUT("/api/franchises/{franchiseId}/branches/{branchName}/name"), franchiseHandler::updateBranchName)
                .andRoute(PUT("/api/franchises/{franchiseId}/branches/{branchName}/products/{productName}/name"), franchiseHandler::updateProductName);

    }
}
