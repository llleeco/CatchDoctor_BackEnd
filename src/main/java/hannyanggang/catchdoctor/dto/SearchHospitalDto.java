package hannyanggang.catchdoctor.dto;

import hannyanggang.catchdoctor.entity.Hospital;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchHospitalDto {
    private Hospital hospital;
    private byte[] mainImage;
    private Double distance;

    public SearchHospitalDto(Hospital hospital, byte[] mainImage) {
        this.hospital = hospital;
        this.mainImage = mainImage;
    }
}
