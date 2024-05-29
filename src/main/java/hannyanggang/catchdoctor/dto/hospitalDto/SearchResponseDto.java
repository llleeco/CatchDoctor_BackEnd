package hannyanggang.catchdoctor.dto.hospitalDto;

import hannyanggang.catchdoctor.entity.Hospital;
import hannyanggang.catchdoctor.util.ImageUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponseDto {
    private Long id;
    private String hospitalname;
    private String address;
    private String tel;
    private Hospital hospital;
    private byte[] mainImage;
    private Double distance;

    public SearchResponseDto(Long id, String hospitalname, String address, String tel, Hospital hospital, byte[] mainImage) {
        this.id = id;
        this.hospitalname = hospitalname;
        this.address = address;
        this.tel = tel;
        this.hospital = hospital;
        this.mainImage = mainImage;
    }
}
