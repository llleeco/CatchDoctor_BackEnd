package hannyanggang.catchdoctor.dto.hospitalDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenApiDto {

    private Double mapx;
    private Double mapy;
    private Long id;
    private String addnum;
    private String address;
    private String hospitalname;
    private String tel;
    private Double distance;

    public OpenApiDto(Double mapX, Double mapY, Long id, String addnum, String address, String hospitalname, String tel) {
         this.mapx = mapX;
        this.mapy = mapY;
        this.id = id;
        this.addnum = addnum;
        this.address = address;
        this.hospitalname = hospitalname;
        this.tel = tel;
    }

//    public OpenApiDto(Double mapX, Double mapY, Long id, String addnum, String address, String hospitalname, String tel) {
//        this.mapx = mapX;
//        this.mapy = mapY;
//        this.id = id;
//        this.addnum = addnum;
//        this.address = address;
//        this.hospitalname = hospitalname;
//        this.tel = tel;
//    }
}
