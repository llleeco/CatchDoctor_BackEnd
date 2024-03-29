package hannyanggang.catchdoctor.service.ReservationsService;

import hannyanggang.catchdoctor.dto.reservationsDTO.ReservationsDTO;
import hannyanggang.catchdoctor.entity.Reservations;
import hannyanggang.catchdoctor.entity.User;
import hannyanggang.catchdoctor.exception.CustomValidationException;
import hannyanggang.catchdoctor.repository.ReservationsRepository;
import hannyanggang.catchdoctor.repository.UserRepository;
import hannyanggang.catchdoctor.repository.hospitalRepository.HospitalRepository;
import jakarta.persistence.LockTimeoutException;
//import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReservationsService {
    private final ReservationsRepository reservationsRepository;

    private final UserRepository patientRepository;

    private final HospitalRepository hospitalRepository;

    // 매일 자정에 실행되는 스케줄링된 작업
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정 (0시 0분)에 실행
    public void updateStatusForPastAppointments() {
        LocalDate currentDate = LocalDate.now();
        reservationsRepository.updateStatusForPastReservations(currentDate);
    }

    @Transactional
    public ResponseEntity<?> createReservation(ReservationsDTO reservationsDTO, String userId) {
        LocalDate date = reservationsDTO.getReservationDate();
        LocalTime time = reservationsDTO.getReservationTime();
        Long hospitalId = reservationsDTO.getHospitalid();
        LocalDateTime appointmentDateTime = LocalDateTime.of(date, time);

        if (appointmentDateTime.isBefore(LocalDateTime.now())) {
            throw new CustomValidationException(HttpStatus.BAD_REQUEST.value(), "과거 날짜 및 시간에는 예약할 수 없습니다.");
        }
        try {
            // 예약 부도 확인 및 패널티 적용
            if (isPenaltyApplied(userId)) {
                throw new CustomValidationException(HttpStatus.FORBIDDEN.value(), "예약 부도 패널티 적용 중 ");
            }
            // 최대 예약 건수 확인 (상태가 '진료전'인 경우에만)
            if (isMaximumReservationsReached(userId)) {
                throw new CustomValidationException(HttpStatus.FORBIDDEN.value(), "최대 예약 2건 초과");
            }

            // 해당 병원에서 이미 예약한 날짜 확인 ( 예약한 날짜의 다른 시간대 예약 불가 )
            if (isDuplicateReservation1(userId, date, hospitalId)) {
                throw new CustomValidationException(HttpStatus.FORBIDDEN.value(), "이미 예약한 날짜입니다");
            }

            // 모든 병원에서 이미 예약한 날짜와 시간 확인
            if (isDuplicateReservation(userId, date, time)) {
                throw new CustomValidationException(HttpStatus.FORBIDDEN.value(), "다른 병원과 예약 시간이 겹칩니다.");
            }

            // 예약 시간대 검증 - 비관적 락
            if (!isTimeSlotAvailable(reservationsDTO.getHospitalid(), reservationsDTO.getReservationDate(), reservationsDTO.getReservationTime())) {
                throw new CustomValidationException(HttpStatus.BAD_REQUEST.value(), "예약 불가능한 시간대입니다.");
            }

            // 예약 저장
            reservationsRepository.save(convertDtoToEntity(reservationsDTO, userId));
            User patients = patientRepository.findById(userId);
            String userName = patients.getName();
            Map<String, Object> response = new HashMap<>();
            response.put("userName", userName);
            response.put("hospitalId", hospitalId);
            response.put("date", date);
            response.put("time", time);

            return ResponseEntity.ok(response);

        } catch (LockTimeoutException e) {
            // 잠금 시간 초과 예외 처리
            throw new CustomValidationException(HttpStatus.REQUEST_TIMEOUT.value(), "예약 시도 중 시간 초과가 발생했습니다. 잠시 후 다시 시도해주세요.");
        } catch (PessimisticLockingFailureException e) {
            // 데드락 혹은 기타 비관적 잠금 실패 처리
            throw new CustomValidationException(HttpStatus.CONFLICT.value(), "시스템 내부 오류로 인한 예약 실패. 다시 시도해주세요.");
        }
    }

    private Reservations convertDtoToEntity(ReservationsDTO appointmentsDTO, String userId) {
        Reservations reservations = new Reservations();

        // DTO -> 엔티티 전환
        reservations.setUserId(userId, patientRepository);
        reservations.setReservationDate(appointmentsDTO.getReservationDate());
        reservations.setReservationTime(appointmentsDTO.getReservationTime());
        reservations.setHospitalId(appointmentsDTO.getHospitalid(), hospitalRepository);
        reservations.setStatus("진료전");

        return reservations;
    }

    private boolean isPenaltyApplied(String userId) {
        // 2달 이내에 "예약부도" 상태인 예약 확인
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(2);
        int penaltyCount = reservationsRepository.countByUser_IdAndStatusAndReservationDateAfter(userId, "예약부도", oneMonthAgo);
        return penaltyCount >= 5;
    }

    private boolean isDuplicateReservation(String userId, LocalDate date, LocalTime time) {
        // 모든 병원에서 동일한 날짜와 시간에 대한 예약 확인
        return reservationsRepository.existsByUser_IdAndReservationDateAndReservationTime(userId, date, time);
    }

    private boolean isDuplicateReservation1(String userId, LocalDate date, Long hospitalId) {
        // 이미 예약한 날짜에 해당 병원에서의 중복 예약 확인 ( 해당 병원의 해당 날짜에서 다른 시간대 예약 불가 )
        return reservationsRepository.existsByUser_IdAndHospital_HospitalidAndReservationDate(userId, hospitalId, date);
    }

    //예약 가능 시간대 확인 - 비관적 락
    @Transactional(readOnly = true)
    boolean isTimeSlotAvailable(Long hospitalId, LocalDate date, LocalTime time) {
        //int seat = availableTimeService.calculateAvailableSlots(hospitalId, date, time);
        int seat = 3 - reservationsRepository.countReservationsForTimeSlot(hospitalId, date, time);
        if(seat > 0) // 남은자리가 0 이상이면 예약 가능
        {
            return true;
        }
        return false;
    }

    private boolean isMaximumReservationsReached(String userId) {
        int maxReservationCount = 2;
        int userActiveReservationCount = reservationsRepository.countByUser_IdAndStatus(userId, "진료전");
        return userActiveReservationCount >= maxReservationCount;
    }
}
