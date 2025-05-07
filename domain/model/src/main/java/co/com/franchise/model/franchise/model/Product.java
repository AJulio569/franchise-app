package co.com.franchise.model.franchise.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class Product {
    private String name;
    private Integer stock;

    public Product(String name, Integer stock) {
        this.name = name;
        this.stock = stock;
    }

    public Product() {
    }

}
