package hannyanggang.catchdoctor.dto.hospitalDto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospitalDTO {
    private Long hospitalid;
    private String name;
    private String operatingHours;
}
