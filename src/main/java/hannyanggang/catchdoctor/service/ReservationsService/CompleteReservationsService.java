package hannyanggang.catchdoctor.service.ReservationsService;

import hannyanggang.catchdoctor.dto.reservationsDTO.ReservationsDTO;
import hannyanggang.catchdoctor.entity.Hospital;
import hannyanggang.catchdoctor.entity.Reservations;
import hannyanggang.catchdoctor.entity.User;
import hannyanggang.catchdoctor.repository.ReservationsRepository;
import hannyanggang.catchdoctor.repository.hospitalRepository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class CompleteReservationsService {
    private final ReservationsRepository reservationsRepository;
    private final HospitalRepository hospitalRepository;
    @Transactional
    public ResponseEntity<?> completeReservation(ReservationsDTO reservationsDTO, String hospitalId) {
        Hospital hospital = hospitalRepository.findById(hospitalId);
        if(hospital==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 병원을 찾을 수 없습니다.");
        }
        LocalDate date = reservationsDTO.getReservationDate();
        LocalTime time = reservationsDTO.getReservationTime();
        Reservations reservations = reservationsRepository.
                findByHospitalAndReservationDateAndReservationTime(hospital, date, time);
        // 예약이 없는 경우
        if(reservations == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 일자와 시간에 예약된 정보를 찾을 수 없습니다.");
        }

        String status = reservations.getStatus();
        // 예약 상태가 예약 확정인 경우
        if(Objects.equals(status, "예약확정")){
            reservations.setStatus("진료완료");
        } // 예약 상태가 예약 확정인 경우
        else if (Objects.equals(status, "예약취소")) {
            return ResponseEntity.ok("예약 취소된 예약건 입니다. 현재 상태: " + status);
        } // 예약 상태가 예약 취소인 경우
        else if (Objects.equals(status, "예약신청")) {
            return ResponseEntity.ok("예약 확정되지 않은 예약입니다. 현재 상태: " + status);
        } // 예약 상태가 진료 완료인 경우
        else if (Objects.equals(status, "진료완료")) {
            return ResponseEntity.ok("이미 진료 완료된 예약건 입니다. 현재 상태: " + status);
        } // 그 외의 상황
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예약 상태를 확인하는 도중 오류가 발생했습니다.");
        }
        reservationsRepository.save(reservations);

        Map<String, Object> response = new HashMap<>();

        User user = reservations.getUser();
        String setStatus = reservations.getStatus();
        String userName = user.getName();
        String hospitalName = hospital.getName();

        response.put("hospitalName", hospitalName);
        response.put("userName", userName);
        response.put("date", date);
        response.put("time", time);
        response.put("status", setStatus);

        return ResponseEntity.ok(response);
    }
}
