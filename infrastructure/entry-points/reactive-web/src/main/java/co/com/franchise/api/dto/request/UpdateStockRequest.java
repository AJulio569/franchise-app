package co.com.franchise.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
public class UpdateStockRequest implements Serializable {
    @NotNull(message = "The new product name cannot be empty")
    @Min(value = 1, message = "The name can only contain letters, numbers, accents, and spaces")
    private Integer newStock;
}
