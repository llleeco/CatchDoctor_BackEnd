package hannyanggang.catchdoctor.dto.hospitalDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenApiDto {

    private Double mapx;
    private Double mapy;
    private Long id;
    private String addnum;
    private String address;
    private String hospitalname;
    private String tel;
    private Double distance;

}
