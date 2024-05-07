package hannyanggang.catchdoctor.dto.hospitalDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponseDto {
    private Long id;
    private String hospitalname;
    private Double distance;

    public SearchResponseDto(Long id, String hospitalname) {
    }
}
