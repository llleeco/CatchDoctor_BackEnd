package hannyanggang.catchdoctor.dto.reservationsDTO;

import java.time.LocalDate;
import java.time.LocalTime;

public class SearchReservationsDTO {
    private LocalDate date;
    private LocalTime time;
    private String status;

    private Long hospitalid;
    private String hospitalName;

    private Long userid;
    private String userName;
}
