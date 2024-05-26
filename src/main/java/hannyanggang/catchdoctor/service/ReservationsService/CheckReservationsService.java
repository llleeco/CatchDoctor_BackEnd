package hannyanggang.catchdoctor.service.ReservationsService;

import hannyanggang.catchdoctor.dto.reservationsDTO.ReservationCheckDto;
import hannyanggang.catchdoctor.entity.Reservations;
import hannyanggang.catchdoctor.repository.ReservationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CheckReservationsService {
    private final ReservationsRepository reservationsRepository;

    public List<ReservationCheckDto> getReservationsByUserId(String userId) {

        try {
            List<Reservations> reservations = reservationsRepository.findByUser_IdOrderByReservationDateAsc(userId);
            List<ReservationCheckDto> appointmentDTOs = new ArrayList<>();

            for (Reservations reservation : reservations) {
                boolean reviewWrite = reservation.isReviewWrite();
                ReservationCheckDto dto = new ReservationCheckDto(
                        reservation.getReservationId(),
                        reservation.getReservationDate(),
                        reservation.getReservationTime(),
                        reservation.getStatus(),
                        reservation.getHospital().getHospitalid(),
                        reservation.getHospital().getName(),
                        reservation.getUser().getName(),
                        reservation.getHospital().getHospitalDetail().getDepartment(),
                        reviewWrite
                );

                appointmentDTOs.add(dto);
            }
            return appointmentDTOs;

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
