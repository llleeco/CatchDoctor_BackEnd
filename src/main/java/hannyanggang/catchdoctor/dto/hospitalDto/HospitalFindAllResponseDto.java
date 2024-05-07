package hannyanggang.catchdoctor.dto.hospitalDto;

import hannyanggang.catchdoctor.entity.Hospital;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HospitalFindAllResponseDto {
    private Long id;
    private String name;

    public static HospitalFindAllResponseDto toDto(Hospital hospital){
        return new HospitalFindAllResponseDto(hospital.getHospitalid(), hospital.getName());
    }
}
