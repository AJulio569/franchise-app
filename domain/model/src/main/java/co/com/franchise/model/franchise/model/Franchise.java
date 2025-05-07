package co.com.franchise.model.franchise.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
public class Franchise {
    private String id;
    private String name;
    private List<Branch> branches;

    public Franchise(String id, String name,List<Branch> branches) {
        this.id = id;
        this.name = name;
        this.branches=branches !=null ? branches : new ArrayList<>();
    }

    public Franchise() {
    }
}
