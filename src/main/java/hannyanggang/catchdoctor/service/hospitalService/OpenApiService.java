package hannyanggang.catchdoctor.service.hospitalService;

import hannyanggang.catchdoctor.dto.hospitalDto.OpenApiHospitalDto;
import hannyanggang.catchdoctor.dto.hospitalDto.SearchResponseDto;
import hannyanggang.catchdoctor.entity.Hospital;
import hannyanggang.catchdoctor.entity.OpenApiHospital;
import hannyanggang.catchdoctor.entity.Reservations;
import hannyanggang.catchdoctor.exception.CustomValidationException;
import hannyanggang.catchdoctor.repository.hospitalRepository.OpenApiRepository;
import hannyanggang.catchdoctor.repository.hospitalRepository.HospitalRepository;
import hannyanggang.catchdoctor.response.Response;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class OpenApiService {
    private final OpenApiRepository openApiRepository;
    private final HospitalRepository hospitalRepository;

    public List<SearchResponseDto> searchHospitalsWithDetails(String query, double MyMapX, double MyMapY, int page, int size) {

        PageRequest pageRequest = PageRequest.of(page,size);

        System.out.println("query = " + query);
        // 동적 쿼리로 병원 검색
        Page<OpenApiHospital> openApiHospitals = openApiRepository.searchWithDynamicQuery(query, pageRequest);
        System.out.println("hospitals = " + openApiHospitals);
        // 병원 목록을 HospitalDTO 목록으로 변환
        return openApiHospitals.stream().map(openApiHospital -> {
            Double hospitalLatitude = openApiHospital.getMapY();
            Double hospitalLongitude = openApiHospital.getMapX();
                    // Null 값 예외 처리
                    if (hospitalLatitude == null || hospitalLongitude == null) {
                        hospitalLatitude = 0.0;
                        hospitalLongitude = 0.0;
//                        throw new CustomValidationException(HttpStatus.BAD_REQUEST.value(), "위치 정보가 없는 병원이 있습니다.");
                    }

            double distance = calculateDistance(MyMapX, MyMapY, hospitalLatitude, hospitalLongitude);

            // SearchResponseDto 객체 생성
            return new SearchResponseDto(
                    openApiHospital.getId(),
                    openApiHospital.getHospitalname(),
                    openApiHospital.getAddress(),
                    openApiHospital.getTel(),
                    openApiHospital.getHospital(),
                    distance
            );
        }).sorted(Comparator.comparingDouble(SearchResponseDto::getDistance))
        .collect(Collectors.toList());
    }
    public List<SearchResponseDto> searchByDepartment(String[] departments, double MyMapX, double MyMapY, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        System.out.println("departments = " + Arrays.toString(departments));
        // 동적 쿼리로 병원 검색
        List<OpenApiHospital> openApiHospitals = new ArrayList<>();
        for (String department : departments) {
            openApiHospitals.addAll(openApiRepository.searchByDepartment(department, pageRequest).getContent());
        }
        System.out.println("hospitals = " + openApiHospitals);
        // 병원 목록을 HospitalDTO 목록으로 변환
        return openApiHospitals.stream().map(openApiHospital -> {
                    double hospitalLatitude = openApiHospital.getMapY();
                    double hospitalLongitude = openApiHospital.getMapX();

                    double distance = calculateDistance(MyMapX, MyMapY, hospitalLatitude, hospitalLongitude);

                    // SearchResponseDto 객체 생성
                    return new SearchResponseDto(
                            openApiHospital.getId(),
                            openApiHospital.getHospitalname(),
                            openApiHospital.getAddress(),
                            openApiHospital.getTel(),
                            openApiHospital.getHospital(),
                            distance
                    );
                }).sorted(Comparator.comparingDouble(SearchResponseDto::getDistance))
                .collect(Collectors.toList());
    }

    // 두 지점 간의 거리를 계산하는 메서드
    private double calculateDistance(double myLatitude, double myLongitude, double hospitalLatitude, double hospitalLongitude) {

        double radiansLatitude = Math.toRadians(myLatitude);

        // 계산된 latitude -> 코사인 계산
        double cosLatitude = Math.cos(radiansLatitude);
        double cosHospitalLatitude = Math.cos(Math.toRadians(hospitalLatitude));

        // 계산된 latitude -> 사인 계산
        double sinLatitude = Math.sin(radiansLatitude);
        double sinHospitalLatitude = Math.sin(Math.toRadians(hospitalLatitude));

        // 사이 거리 계산
        double cosLongitude = Math.cos(Math.toRadians(hospitalLongitude) - Math.toRadians(myLongitude));
        double acosExpression = Math.acos(cosLatitude * cosHospitalLatitude * cosLongitude + sinLatitude * sinHospitalLatitude);

        // 최종 계산
        return 6371 * acosExpression; // 거리 계산
    }

    public List<SearchResponseDto> getAllHospitals(int page, int size) {

        String today = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date());
        PageRequest pageRequest = PageRequest.of(page,size);
        Page<OpenApiHospital> openApiHospitals = openApiRepository.findAll(pageRequest);
        // 병원 목록을 HospitalDTO 목록으로 변환
        return openApiHospitals.stream().map(openApiHospital -> {
            // HospitalDTO 객체 생성
            // SearchResponseDto 객체 생성
            return new SearchResponseDto(
                    openApiHospital.getId(),
                    openApiHospital.getAddress(),
                    openApiHospital.getHospitalname(),
                    openApiHospital.getTel(),
                    openApiHospital.getHospital()
            );
        }).collect(toList());

    }
    public boolean checkhospital(String hospitalname){
        OpenApiHospital openApiHospital = openApiRepository.findByHospitalName(hospitalname);
        if (openApiHospital == null) {
            return false;
        }
        String openApiName = openApiHospital.getHospitalname();
        return hospitalname.equals(hospitalname);
    }

    public List<OpenApiHospitalDto> findAll() {
        List<OpenApiHospital> openApiHospitals = openApiRepository.findAll();
        return openApiHospitals.stream()
                .map(OpenApiHospitalDto::new)
                .collect(Collectors.toList());
    }
}
