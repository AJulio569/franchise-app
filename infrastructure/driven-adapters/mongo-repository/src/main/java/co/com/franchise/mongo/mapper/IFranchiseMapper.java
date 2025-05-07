package co.com.franchise.mongo.mapper;

import co.com.franchise.model.franchise.model.Branch;
import co.com.franchise.model.franchise.model.Franchise;
import co.com.franchise.model.franchise.model.Product;
import co.com.franchise.mongo.entity.BranchEntity;
import co.com.franchise.mongo.entity.FranchiseEntity;
import co.com.franchise.mongo.entity.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IFranchiseMapper {
    Franchise toDomain(FranchiseEntity entity);
    Branch toDomain(BranchEntity entity);
    Product toDomain(ProductEntity entity);

    FranchiseEntity toEntity(Franchise domain);
    BranchEntity toEntity(Branch domain);
    ProductEntity toEntity(Product domain);
}
