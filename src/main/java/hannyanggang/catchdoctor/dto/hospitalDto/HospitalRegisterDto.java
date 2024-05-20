package hannyanggang.catchdoctor.dto.hospitalDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospitalRegisterDto {
    private String id;
    private String password;
    private String name;
//    private String addnum;
}
