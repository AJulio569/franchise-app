package co.com.franchise.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class FranchiseRequest implements Serializable{
    @NotBlank(message = "The franchise name cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚüÜñÑ ]*$", message = "The name can only contain letters, numbers, accents, and spaces")
    private String name;
    private List<BranchRequest> branches;
}
