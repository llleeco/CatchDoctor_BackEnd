package hannyanggang.catchdoctor.service.ReservationsService;

import hannyanggang.catchdoctor.entity.Reservations;
import hannyanggang.catchdoctor.exception.CustomValidationException;
import hannyanggang.catchdoctor.repository.ReservationsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
@RequiredArgsConstructor
@Service
public class CancelReservationsService {

    private final ReservationsRepository reservationsRepository;

    @Transactional
    public boolean cancelAppointment(Long reservationId, String userId) {
        try {
            Optional<Reservations> reservation = reservationsRepository.findByUser_IdAndReservationId(reservationId, userId);
            if (reservation.isPresent()) {
                Reservations res = reservation.get();
                // 예약신청 상태인지 확인
                if ("예약신청".equals(res.getStatus())) {
                    res.setStatus("예약취소");
                    reservationsRepository.save(res);
                    return true;
                } else {
                    // 예약 상태가 예약신청이 아닐 경우
                    throw new CustomValidationException(HttpStatus.BAD_REQUEST.value(), "예약신청 상태가 아닙니다.");
                }
            } else {
                return false;
            }
        } catch (Exception e){
            throw new CustomValidationException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}
