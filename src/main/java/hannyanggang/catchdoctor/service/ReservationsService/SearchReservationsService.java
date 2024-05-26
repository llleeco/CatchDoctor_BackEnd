package hannyanggang.catchdoctor.service.ReservationsService;

import hannyanggang.catchdoctor.dto.reservationsDTO.ReservationsDTO;
import hannyanggang.catchdoctor.entity.Reservations;
import hannyanggang.catchdoctor.repository.ReservationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchReservationsService {
    private final ReservationsRepository reservationsRepository;

    public List<ReservationsDTO> getReservationsByHospitalId(String hospitalId) {

        try{
            List<Reservations> reservations = reservationsRepository.findByHospital_IdOrderByReservationDateAscReservationTimeAsc(hospitalId);
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
