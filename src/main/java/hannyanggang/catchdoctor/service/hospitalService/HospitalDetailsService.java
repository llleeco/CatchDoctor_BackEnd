package hannyanggang.catchdoctor.service.hospitalService;

import hannyanggang.catchdoctor.TimeRange;
import hannyanggang.catchdoctor.dto.hospitalDto.HospitalDetailsDTO;
import hannyanggang.catchdoctor.entity.Hospital;
import hannyanggang.catchdoctor.repository.hospitalRepository.HospitalRepository;
import hannyanggang.catchdoctor.repository.operationTimeRepository.OperationTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HospitalDetailsService {
    // @Autowired 필드들...
    private final HospitalRepository hospitalRepository;
    private final OperationTimeRepository operationTimeRepository;

    public HospitalDetailsDTO getHospitalDetails(String userId, Long hospitalId) {
        String today = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date());
        Hospital hospital = hospitalRepository.findByHospitalid(hospitalId).orElseThrow(()-> new RuntimeException("Hospital not found"));
        String operatingHours = operationTimeRepository.findOperatingHoursByHospitalIdAndDay(hospitalId, today);

        String breakTime = operationTimeRepository.findBreakTimeByHospitalIdAndDay(hospitalId, today);
        String hospitalStatus = determineHospitalStatus(operatingHours, breakTime);

        return new HospitalDetailsDTO(hospital, operatingHours, breakTime, hospitalStatus);
    }

    private String determineHospitalStatus(String operatingHours, String breakTime) {
        LocalTime currentTime = LocalTime.now(ZoneId.of("Asia/Seoul"));
        TimeRange operatingRange = TimeRange.fromString(operatingHours);
        TimeRange breakRange = TimeRange.fromString(breakTime);

        System.out.println("시간 = "+currentTime+operatingHours+breakTime);
        if (operatingRange == null) {
            return "오늘휴무";
        } else if (isCurrentTimeInRange(currentTime, breakRange)) {
            return "휴게시간";
        } else if (isCurrentTimeInRange(currentTime, operatingRange)) {
            return "영업중";
        }
        else if (currentTime.isBefore(operatingRange.getStartTime())) {
            return "영업전";
        } else if(currentTime.isAfter(operatingRange.getEndTime())){
            return "영업종료";
        }  else{
            return "영업정보없음";
        }
    }

    private boolean isCurrentTimeInRange(LocalTime currentTime, TimeRange range) {
        if (range == null) {
            return false;
        }
        return !currentTime.isBefore(range.getStartTime()) && !currentTime.isAfter(range.getEndTime());
    }
}
