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
public class UpdateBranchNameRequest implements Serializable {
    @NotBlank(message = "The branch name cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚüÜñÑ ]*$", message = "The branch name can only contain letters, numbers, accents, and spaces")
    private String newName;
}
