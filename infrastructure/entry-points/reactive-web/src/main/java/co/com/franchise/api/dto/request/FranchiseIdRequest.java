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
public class FranchiseIdRequest implements Serializable {
    @NotBlank(message = "The ID cannot be empty")
    @Pattern(regexp = "^[a-fA-F0-9]{24}$", message = "The ID must be a valid ObjectId of 24 hexadecimal characters")
    private String id;
}
