package hannyanggang.catchdoctor.dto.hospitalDto;

import hannyanggang.catchdoctor.entity.OpenApiHospital;
import lombok.Getter;

@Getter
public class OpenApiHospitalDto {
    private final Long id;
    private String hospitalname;
    private String tel;
    private String addnum;
    private String address;
    private Double mapX;
    private Double mapY;
    private Long hospitalId;

    public OpenApiHospitalDto(OpenApiHospital openApiHospital) {
        this.id = openApiHospital.getId();
        this.hospitalname = openApiHospital.getHospitalname();
        this.tel = openApiHospital.getTel();
        this.addnum = openApiHospital.getAddnum();
        this.address = openApiHospital.getAddress();
        this.mapX = openApiHospital.getMapX();
        this.mapY = openApiHospital.getMapY();
        this.hospitalId = openApiHospital.getHospital() != null ? openApiHospital.getHospital().getHospitalid() : null;
    }
}
