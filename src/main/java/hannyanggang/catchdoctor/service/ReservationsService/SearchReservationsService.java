package hannyanggang.catchdoctor.service.ReservationsService;

import hannyanggang.catchdoctor.dto.reservationsDTO.ReservationsDTO;
import hannyanggang.catchdoctor.entity.Reservations;
import hannyanggang.catchdoctor.exception.CustomValidationException;
import hannyanggang.catchdoctor.repository.ReservationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SearchReservationsService {
    private final ReservationsRepository reservationsRepository;

    public List<ReservationsDTO> searchAppointments(String userId, String hospitalName, LocalDate date) {

        try {
            List<Reservations> reservations = new ArrayList<>(); // 빈 리스트로 초기화
            if (hospitalName != null && date != null) {
                reservations = reservationsRepository.findByUser_IdAndHospital_NameLikeAndReservationDate(userId, "%" + hospitalName + "%", date);

            } else if (hospitalName == null && date != null) {
                reservations =reservationsRepository.findByUser_IdAndReservationDate(userId, date);

            } else if (hospitalName != null) {
                reservations = reservationsRepository.findByUser_IdAndHospital_Name(userId, "%" + hospitalName + "%");
            } else {
                throw new CustomValidationException(HttpStatus.BAD_REQUEST.value(), "검색어나 날짜를 입력해주세요");
            }
            // Appointments 엔티티를 AppointmentsDTO로 변환
            return reservations.stream().map(this::convertToDTO).collect(Collectors.toList());

        } catch (Exception e){
            throw new IllegalStateException();
        }
    }

    private ReservationsDTO convertToDTO(Reservations reservation) {
        ReservationsDTO dto = new ReservationsDTO(
                reservation.getReservationId(), // int -> Long 변환
                reservation.getReservationDate(),
                reservation.getReservationTime(),
                reservation.getStatus(),
                reservation.getHospital().getHospitalid(),
                reservation.getHospital().getName(),
                reservation.getUser().getName()
        );
        return dto;
    }
}
