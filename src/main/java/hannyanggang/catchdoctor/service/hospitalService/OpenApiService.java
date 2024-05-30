package hannyanggang.catchdoctor.service.hospitalService;

import hannyanggang.catchdoctor.dto.hospitalDto.OpenApiHospitalDto;
import hannyanggang.catchdoctor.dto.hospitalDto.SearchResponseDto;
import hannyanggang.catchdoctor.entity.Hospital;
import hannyanggang.catchdoctor.entity.HospitalDetail;
import hannyanggang.catchdoctor.entity.OpenApiHospital;
import hannyanggang.catchdoctor.repository.hospitalRepository.OpenApiRepository;
import hannyanggang.catchdoctor.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
                    }
            double distance = calculateDistance(MyMapX, MyMapY, hospitalLatitude, hospitalLongitude);
                    byte[] mainImage = null;
                    if (openApiHospital.getHospital() != null && openApiHospital.getHospital().getHospitalDetail() != null) {
                        byte[] compressedImage = openApiHospital.getHospital().getHospitalDetail().getBoardImage1();
                        if (compressedImage != null) {
                            mainImage = ImageUtils.decompressImage(compressedImage);
                        }
                    }
            // SearchResponseDto 객체 생성
            return new SearchResponseDto(
                    openApiHospital.getId(),
                    openApiHospital.getHospitalname(),
                    openApiHospital.getAddress(),
                    openApiHospital.getTel(),
                    openApiHospital.getHospital(),
                    mainImage,
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
                    byte[] mainImage = null;
                    if (openApiHospital.getHospital() != null && openApiHospital.getHospital().getHospitalDetail() != null) {
                        byte[] compressedImage = openApiHospital.getHospital().getHospitalDetail().getBoardImage1();
                        if (compressedImage != null) {
                            mainImage = ImageUtils.decompressImage(compressedImage);
                        }
                    }
                    // SearchResponseDto 객체 생성
                    return new SearchResponseDto(
                            openApiHospital.getId(),
                            openApiHospital.getHospitalname(),
                            openApiHospital.getAddress(),
                            openApiHospital.getTel(),
                            openApiHospital.getHospital(),
                            mainImage,
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

    public List<SearchResponseDto> getAllHospitals(double MyMapX, double MyMapY, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<OpenApiHospital> openApiHospitals = openApiRepository.findAll(pageRequest);

        return openApiHospitals.stream().map(openApiHospital -> {
                    byte[] mainImage = null;
                    double distance;
                    Double hospitalLatitude = openApiHospital.getMapY();
                    Double hospitalLongitude = openApiHospital.getMapX();

                    if (hospitalLatitude != null && hospitalLongitude != null) {
                        distance = calculateDistance(MyMapY, MyMapX, hospitalLatitude, hospitalLongitude);
                    } else {
                        distance = Double.MAX_VALUE;
                    }

                    if (openApiHospital.getHospital() != null) {
                        Hospital hospital = openApiHospital.getHospital();
                        if (hospital.getHospitalDetail() != null) {
                            HospitalDetail hospitalDetail = hospital.getHospitalDetail();
                            if (hospitalDetail.getBoardImage1() != null) {
                                byte[] compressedImage = hospitalDetail.getBoardImage1();
                                if (compressedImage != null) {
                                    mainImage = ImageUtils.decompressImage(compressedImage);
                                }
                            }
                        }
                    }

                    return new SearchResponseDto(
                            openApiHospital.getId(),
                            openApiHospital.getHospitalname(),
                            openApiHospital.getAddress(),
                            openApiHospital.getTel(),
                            openApiHospital.getHospital(),
                            mainImage,
                            distance
                    );
                }).sorted(Comparator.comparingDouble(SearchResponseDto::getDistance))
                .collect(Collectors.toList());
    }

    public boolean checkHospital(String hospitalName){
        OpenApiHospital openApiHospital = openApiRepository.findByHospitalName(hospitalName);
        if (openApiHospital == null) {
            return false;
        }
        String openApiName = openApiHospital.getHospitalname();
        return hospitalName.equals(openApiName);
    }

    public List<OpenApiHospitalDto> findAll() {
        List<OpenApiHospital> openApiHospitals = openApiRepository.findAll();
        return openApiHospitals.stream()
                .map(OpenApiHospitalDto::new)
                .collect(Collectors.toList());
    }
}
