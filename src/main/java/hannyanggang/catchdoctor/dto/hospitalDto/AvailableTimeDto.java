package hannyanggang.catchdoctor.dto.hospitalDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AvailableTimeDto {
    private String availableDate;
    private String availableTime;
}
