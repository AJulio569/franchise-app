package co.com.franchise.api.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseResponse implements Serializable{
    private String id;
    private String name;
    private List<BranchResponse> branches;
}
