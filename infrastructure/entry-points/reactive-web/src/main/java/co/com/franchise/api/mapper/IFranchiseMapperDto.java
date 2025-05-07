package co.com.franchise.api.mapper;

import co.com.franchise.api.dto.request.BranchRequest;
import co.com.franchise.api.dto.request.FranchiseRequest;
import co.com.franchise.api.dto.request.ProductRequest;
import co.com.franchise.api.dto.response.BranchResponse;
import co.com.franchise.api.dto.response.FranchiseResponse;
import co.com.franchise.api.dto.response.ProductResponse;
import co.com.franchise.api.dto.response.TopStockProductResponse;
import co.com.franchise.model.franchise.model.Branch;
import co.com.franchise.model.franchise.model.Franchise;
import co.com.franchise.model.franchise.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE )
public interface IFranchiseMapperDto {
    Franchise toDomain(FranchiseRequest request);
    Branch toDomain(BranchRequest request);
    Product toDomain(ProductRequest request);

    FranchiseResponse toResponse(Franchise domain);
    BranchResponse toResponse(Branch branch);
    ProductResponse toResponse(Product product);

    List<Branch> toBranchDomainList(List<BranchRequest> branches);
    List<Product> toProductDomainList(List<ProductRequest> products);

    TopStockProductResponse toTopStockProductResponse(String branchName, Product product);
}
