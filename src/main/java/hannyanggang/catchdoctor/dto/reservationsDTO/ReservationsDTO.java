package hannyanggang.catchdoctor.dto.reservationsDTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationsDTO {
    private Long reservationId; // 예약 아이디(고유번호)
    private LocalDate reservationDate; // 예약 날짜
    private LocalTime reservationTime; //시간
    private String status; //상태

    private Long hospitalid; //병원 아이디(고유번호-Long)
    private String hospitalName; //병원 이름

    private String userName; //환자 이름
}
