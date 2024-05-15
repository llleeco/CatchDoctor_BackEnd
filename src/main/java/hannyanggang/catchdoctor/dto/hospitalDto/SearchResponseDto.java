package hannyanggang.catchdoctor.dto.hospitalDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import hannyanggang.catchdoctor.entity.Hospital;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponseDto {
    private Long id;
    private String hospitalname;
    private String address;
    private String tel;
    private Hospital hospital;
    private Double distance;

    public SearchResponseDto(Long id, String hospitalname, String address, String tel, Hospital hospital) {
    }
}
