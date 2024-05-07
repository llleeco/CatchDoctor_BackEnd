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
    private String department;

    public HospitalDTO(Long hospitalid, String name, String department) {
        this.hospitalid=hospitalid;
        this.name = name;
        this.department = department;
    }

//    public HospitalDTO(Long hospitalid, String name, String department, String operatingHours) {
//        this.hospitalid=hospitalid;
//        this.name = name;
//        this.department = department;
//        this.operatingHours = operatingHours;
//    }
}
