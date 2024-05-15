package hannyanggang.catchdoctor.dto.userDto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {
    private String id;
    private String password;
    private String name;
//    private LocalDate birthday;
}