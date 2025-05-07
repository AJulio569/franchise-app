package co.com.franchise.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest implements Serializable {
    @NotBlank(message = "The product name cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚüÜñÑ ]*$", message = "The name can only contain letters, numbers, accents, and spaces")
    private String name;

    @NotNull(message =  "Stock cannot be null")
    @Min(value = 1, message = "Stock must be greater than or equal to 1")
    private Integer stock;
}
