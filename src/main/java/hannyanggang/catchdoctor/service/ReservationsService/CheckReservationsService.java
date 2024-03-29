package hannyanggang.catchdoctor.service.ReservationsService;

import hannyanggang.catchdoctor.dto.reservationsDTO.ReservationsDTO;
import hannyanggang.catchdoctor.entity.Reservations;
import hannyanggang.catchdoctor.repository.ReservationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CheckReservationsService {

    private final ReservationsRepository reservationsRepository;

    public List<ReservationsDTO> getReservationsByUserId(String userId) {

        try{
            List<Reservations> reservations = reservationsRepository.findByUser_IdOrderByReservationDateDesc(userId);
            List<ReservationsDTO> appointmentDTOs = new ArrayList<>();

            for (Reservations reservation : reservations) {
                ReservationsDTO dto = new ReservationsDTO(
                        reservation.getReservationId(),
                        reservation.getReservationDate(),
                        reservation.getReservationTime(),
                        reservation.getStatus(),
                        reservation.getHospital().getHospitalid(),
                        reservation.getHospital().getName(),
                        reservation.getUser().getName()

                );
                appointmentDTOs.add(dto);
            }
            return appointmentDTOs;

        }catch (Exception e){
            System.out.println(e);
            return null;
        }

    }
}
