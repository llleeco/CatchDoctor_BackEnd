package hannyanggang.catchdoctor.service.hospitalService;

import hannyanggang.catchdoctor.dto.hospitalDto.HospitalDTO;
import hannyanggang.catchdoctor.entity.Hospital;
import hannyanggang.catchdoctor.repository.hospitalRepository.HospitalRepository;
import hannyanggang.catchdoctor.repository.operationTimeRepository.OperationTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Service
public class HospitalService {
    private final HospitalRepository hospitalRepository;

    private final OperationTimeRepository operationTimeRepository;



    public List<HospitalDTO> searchHospitalsWithDetails(String query, int page, int size) {

        PageRequest pageRequest = PageRequest.of(page,size);

        // 오늘 날짜에 해당하는 요일을 한글로 구합니다.
        String today = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date());
        System.out.println("query = " + query);
        // 동적 쿼리로 병원 검색
        Page<Hospital> hospitals = hospitalRepository.searchWithDynamicQuery(query, pageRequest);
        System.out.println("hospitals = " + hospitals);
        // 병원 목록을 HospitalDTO 목록으로 변환
        return hospitals.stream().map(hospital -> {
            // 운영시간 및 좋아요 수 조회
            String operatingHours = operationTimeRepository.findOperatingHoursByHospitalIdAndDay(hospital.getHospitalid(), today);

            // HospitalDTO 객체 생성
            return new HospitalDTO(
                    hospital.getHospitalid(),
                    hospital.getName(),
                    hospital.getDepartment(),
                    operatingHours
            );
        }).collect(Collectors.toList());

    }


    public List<HospitalDTO> searchByDepartment(String department, int page, int size) {
        // 오늘 날짜에 해당하는 요일을 한글로 구합니다.
        String today = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date());
        PageRequest pageRequest = PageRequest.of(page,size);
        Page<Hospital> hospitals = hospitalRepository.findByDepartment(department, pageRequest);

        // 병원 목록을 HospitalDTO 목록으로 변환
        return hospitals.stream().map(hospital -> {
            // 운영시간 및 좋아요 수 조회
            String operatingHours = operationTimeRepository.findOperatingHoursByHospitalIdAndDay(hospital.getHospitalid(), today);

            // HospitalDTO 객체 생성
            return new HospitalDTO(
                    hospital.getHospitalid(),
                    hospital.getName(),
                    hospital.getDepartment(),
                    operatingHours
            );
        }).collect(Collectors.toList());
    }

    public List<HospitalDTO> getAllHospitals(int page, int size) {

        String today = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date());
        PageRequest pageRequest = PageRequest.of(page,size);
        Page<Hospital> hospitals = hospitalRepository.findAll(pageRequest);

        // 병원 목록을 HospitalDTO 목록으로 변환
        return hospitals.stream().map(hospital -> {
            // 운영시간 및 좋아요 수 조회
            String operatingHours = operationTimeRepository.findOperatingHoursByHospitalIdAndDay(hospital.getHospitalid(), today);

            // HospitalDTO 객체 생성
            return new HospitalDTO(
                    hospital.getHospitalid(),
                    hospital.getName(),
                    hospital.getDepartment(),
                    operatingHours
            );
        }).collect(Collectors.toList());

    }
}
