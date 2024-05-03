package hannyanggang.catchdoctor.service.hospitalService;

import hannyanggang.catchdoctor.dto.hospitalDto.HospitalDetailDto;
import hannyanggang.catchdoctor.entity.Hospital;
import hannyanggang.catchdoctor.entity.HospitalDetail;
import hannyanggang.catchdoctor.entity.User;
import hannyanggang.catchdoctor.repository.hospitalRepository.HospitalDetailRepository;
import hannyanggang.catchdoctor.repository.hospitalRepository.HospitalRepository;
import hannyanggang.catchdoctor.role.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HospitalDetailService {
    private final HospitalDetailRepository hospitalDetailRepository;
    private final HospitalRepository hospitalRepository;
    public ResponseEntity<?> hospitalMyPage(HospitalDetailDto hospitalDetailsDto, String hospitalId) {
        Hospital hospital = hospitalRepository.findById(hospitalId);

        HospitalDetail hospitalDetail = HospitalDetail.builder()
                .hospital(hospital)
                .hospitalInfo(hospitalDetailsDto.getHospitalInfo())
                .department(hospitalDetailsDto.getDepartment())
                .doctorInfo(hospitalDetailsDto.getDoctorInfo())
                .mon_open(hospitalDetailsDto.getMon_open())
                .mon_close(hospitalDetailsDto.getMon_close())
                .tue_open(hospitalDetailsDto.getTue_open())
                .tue_close(hospitalDetailsDto.getTue_close())
                .wed_open(hospitalDetailsDto.getWed_open())
                .wed_close(hospitalDetailsDto.getWed_close())
                .thu_open(hospitalDetailsDto.getThu_open())
                .thu_close(hospitalDetailsDto.getThu_close())
                .fri_open(hospitalDetailsDto.getFri_open())
                .fri_close(hospitalDetailsDto.getFri_close())
                .sat_open(hospitalDetailsDto.getSat_open())
                .sat_close(hospitalDetailsDto.getSat_close())
                .sun_open(hospitalDetailsDto.getSun_open())
                .sun_close(hospitalDetailsDto.getSun_close())
                .hol_open(hospitalDetailsDto.getHol_open())
                .hol_close(hospitalDetailsDto.getHol_close())
                .build();
        hospitalDetailRepository.save(hospitalDetail);

//        HospitalDetail Detail = hospitalDetailRepository.findById(hospitalId);
//        String hospitalInfo = Detail.getHospitalInfo();
        Map<String, Object> response = new HashMap<>();
//        response.put("hospitalInfo", hospitalInfo);
        response.put("hospitalId", hospitalId);

        return ResponseEntity.ok(response);
    }
}
