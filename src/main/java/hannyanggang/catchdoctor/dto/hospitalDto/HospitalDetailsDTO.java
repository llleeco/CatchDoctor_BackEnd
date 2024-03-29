package hannyanggang.catchdoctor.dto.hospitalDto;

import hannyanggang.catchdoctor.entity.Hospital;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospitalDetailsDTO {
    private Long hospitalid;
    private String hospitalName;
    private String operatingHours;
    private String department;
    private String breakTime;
    private String hospitalStatus;

    public HospitalDetailsDTO(Hospital hospital, String operatingHours, String breakTime, String hospitalStatus) {
        this.hospitalid = hospital.getHospitalid();
        this.hospitalName = hospital.getName();
        this.department = hospital.getDepartment();
        this.operatingHours = operatingHours;
        this.breakTime = breakTime;
        this.hospitalStatus = hospitalStatus;
    }

}
