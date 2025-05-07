package co.com.franchise.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FranchiseProductRequest implements Serializable{
    @NotBlank(message = "The franchise ID cannot be empty")
    @Pattern(regexp = "^[a-fA-F0-9]{24}$", message = "The ID must be a valid ObjectId of 24 hexadecimal characters")
    private String franchiseId;

    @NotBlank(message = "The branch name cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚüÜñÑ ]*$", message = "The branch name can only contain letters, numbers, accents, and spaces")
    private String branchName;

    @NotBlank(message = "The product name cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚüÜñÑ ]*$", message = "The product name can only contain letters, numbers, accents, and spaces")
    private String productName;
}
