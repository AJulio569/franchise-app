package co.com.franchise.model.franchise.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
public class Branch {
    private String name;
    private List<Product> products;

    public Branch(String name, List<Product> products) {
        this.name = name;
        this.products = products != null ? products : new ArrayList<>();
    }

    public Branch() {
    }
}
