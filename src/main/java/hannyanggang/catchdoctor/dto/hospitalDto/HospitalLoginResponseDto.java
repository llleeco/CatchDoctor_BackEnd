package hannyanggang.catchdoctor.dto.hospitalDto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HospitalLoginResponseDto {
    private String token;
    private Long hospitalId;
}
