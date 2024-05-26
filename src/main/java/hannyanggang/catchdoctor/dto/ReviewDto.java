package hannyanggang.catchdoctor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    //review num
    private Long review_id;

    private Long hospital_id;

    private Long user_id;

    private Long reservation_id;

    private String username;

    private float grade;

    private String text;

    private LocalDate regDate, modDate;
}
