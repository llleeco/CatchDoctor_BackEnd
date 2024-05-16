package hannyanggang.catchdoctor.dto.hospitalDto;

import hannyanggang.catchdoctor.entity.HospitalDetail;
import hannyanggang.catchdoctor.entity.Reservations;
import hannyanggang.catchdoctor.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospitalFindDto {
    private Long hospitalid;
    private String id;
    private String name;
    private String addnum;
    private String role;
    private HospitalDetail hospitalDetail;
    private List<Review> review = new ArrayList<>();
    private Set<Reservations> reservations;

}
