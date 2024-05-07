package hannyanggang.catchdoctor.dto.hospitalDto;

import hannyanggang.catchdoctor.entity.Hospital;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospitalDetailDto {
    private String hospitalName;
    private String hospitalInfo;
    private String department;
    private String doctorInfo;
    private String mon_open; // 월요일
    private String mon_close;
    private String tue_open; // 화요일
    private String tue_close;
    private String wed_open; // 수요일
    private String wed_close;
    private String thu_open; // 목요일
    private String thu_close;
    private String fri_open; // 금요일
    private String fri_close;
    private String sat_open; // 토요일
    private String sat_close;
    private String sun_open; // 일요일
    private String sun_close;
    private String hol_open; // 공휴일
    private String hol_close;

}
