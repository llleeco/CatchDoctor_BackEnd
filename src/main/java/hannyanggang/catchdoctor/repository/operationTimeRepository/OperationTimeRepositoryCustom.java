package hannyanggang.catchdoctor.repository.operationTimeRepository;

import hannyanggang.catchdoctor.entity.OperatingHours;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationTimeRepositoryCustom {
    String findOperatingHoursByHospitalIdAndDay(Long hospitalId, String dayOfWeek);

    String findBreakTimeByHospitalIdAndDay(Long hospitalId, String dayOfWeek);

    List<OperatingHours> findByHospitalIdAndDayOfWeek(Long hospitalId, String dayOfWeek);

    List<OperatingHours> findOperatingHoursByHospitalIdAndDate(Long hospitalId, String date);

}
