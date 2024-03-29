package hannyanggang.catchdoctor.service.hospitalService;

import hannyanggang.catchdoctor.TimeRange;
import hannyanggang.catchdoctor.entity.OperatingHours;
import hannyanggang.catchdoctor.exception.CustomValidationException;
import hannyanggang.catchdoctor.repository.ReservationsRepository;
import hannyanggang.catchdoctor.repository.hospitalRepository.HospitalRepository;
import hannyanggang.catchdoctor.repository.operationTimeRepository.OperationTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AvailableTimeService {

    private final OperationTimeRepository operationTimeRepository;

    private final ReservationsRepository reservationsRepository;

    private final HospitalRepository hospitalRepository;

    public Map<String, Object> getAvailableTimes(Long hospitalId, String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        //Map<String, List<Map<String, Object>>> availableTimesMap = new HashMap<>();
        Map<String, Object> response = new HashMap<>();

        List<Map<String, Object>> availableTimes = getAvailableTimesForSingleDate(hospitalId, date);

        String hospitalName = hospitalRepository.findNameByHospitalid(hospitalId);

        // JSON 객체에 병원 이름과 사용 가능한 시간 추가
        response.put("hospitalName", hospitalName);
        response.put("availableTime", availableTimes);

        return response;
    }

    public List<Map<String, Object>> getAvailableTimesForSingleDate(Long hospitalId, LocalDate date) {
        try {
            String dayOfWeek = date.getDayOfWeek().toString().toUpperCase();
            //운영시간 확인
            List<OperatingHours> operatingHoursList = operationTimeRepository.findByHospitalIdAndDayOfWeek(hospitalId, dayOfWeek);
            List<Map<String, Object>> availableTimesWithSlots = new ArrayList<>();

            for (OperatingHours hours : operatingHoursList) {
                TimeRange operatingRange = TimeRange.fromString(hours.getOpeningHours());
                if (operatingRange == null) { //운영시간이 없으면 빈 리스트
                    return Collections.emptyList();
                }

                TimeRange breakRange = TimeRange.fromString(hours.getBreakTime());
                List<TimeRange> availableTimeRanges = operatingRange.subtract(breakRange);

                for (TimeRange range : availableTimeRanges) {
                    LocalTime startTime = range.getStartTime();
                    while (startTime.getMinute() != 0) {
                        startTime = startTime.plusMinutes(1);
                    }
                    //남는자리 있는지
                    while (!startTime.isAfter(range.getEndTime()) && startTime.plusHours(1).isBefore(range.getEndTime()) || startTime.plusHours(1).equals(range.getEndTime())) {
                        int availableSlots = calculateAvailableSlots(hospitalId, date, startTime);
                        Map<String, Object> timeWithSlots = new HashMap<>();

                        timeWithSlots.put("time", startTime.toString());
                        timeWithSlots.put("availableSlots", Math.max(availableSlots, 0));
                        availableTimesWithSlots.add(timeWithSlots);
                        startTime = startTime.plusHours(1);
                    }
                }
            }

            return availableTimesWithSlots;

        } catch (CustomValidationException e) {
            throw new CustomValidationException(HttpStatus.BAD_REQUEST.value(), "조회 오류");
        } catch (Exception e) {
            throw new CustomValidationException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "조회 실패");
        }
    }

    //@Transactional
    //남는자리 있는지 계산
    public int calculateAvailableSlots(Long hospitalId, LocalDate date, LocalTime startTime) {
        return 3 - reservationsRepository.countByHospital_HospitalIdAndReservationDateAndReservationTime(hospitalId, date, startTime);
    }
}