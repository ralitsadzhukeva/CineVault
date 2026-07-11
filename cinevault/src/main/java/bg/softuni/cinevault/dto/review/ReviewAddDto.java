package bg.softuni.cinevault.dto.review;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewAddDto {
    @NotNull
    @Min(1)
    @Max(10)
    private Integer rating;

    @Size(min = 0, max = 500)
    private String comment;
}
