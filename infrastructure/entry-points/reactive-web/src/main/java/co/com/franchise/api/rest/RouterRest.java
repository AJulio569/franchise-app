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
        return route(GET("/api/franchise"), franchiseHandler::getAllFranchise)
                .andRoute(POST("/api/franchise"), franchiseHandler::createFranchise)
                .andRoute(GET("/api/franchise/id/{id}"), franchiseHandler::getFranchiseById)
                .andRoute(PUT("/api/franchise/id/{id}"), franchiseHandler::updateFranchise)
                .andRoute(POST("/api/franchise/id/{franchiseId}/branch"), franchiseHandler::addBranchToFranchise)
                .andRoute(POST("/api/franchise/id/{franchiseId}/branch/{branchName}/product"), franchiseHandler::addProductToBranch)
                .andRoute(DELETE("/api/franchise/id/{franchiseId}/branch/{branchName}/product/{productName}"), franchiseHandler::deleteProductFromBranch)
                .andRoute(PUT("/api/franchise/id/{franchiseId}/branch/{branchName}/product/{productName}/stock"), franchiseHandler::updateProductStock)
                .andRoute(GET("/api/franchise/id/{franchiseId}/product/top-stock"), franchiseHandler::getTopStockProductByBranch)
                .andRoute(PUT("/api/franchise/id/{franchiseId}/branch/{branchName}/name"), franchiseHandler::updateBranchName)
                .andRoute(PUT("/api/franchise/id/{franchiseId}/branch/{branchName}/product/{productName}/name"), franchiseHandler::updateProductName);

    }
}
