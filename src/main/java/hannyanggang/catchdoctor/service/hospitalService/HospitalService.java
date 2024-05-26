package hannyanggang.catchdoctor.service.hospitalService;

import hannyanggang.catchdoctor.dto.LoginRequestDto;
import hannyanggang.catchdoctor.dto.hospitalDto.HospitalRegisterDto;
import hannyanggang.catchdoctor.entity.Hospital;
import hannyanggang.catchdoctor.entity.HospitalDetail;
import hannyanggang.catchdoctor.entity.OpenApiHospital;
import hannyanggang.catchdoctor.repository.hospitalRepository.HospitalDetailRepository;
import hannyanggang.catchdoctor.repository.hospitalRepository.HospitalRepository;
import hannyanggang.catchdoctor.repository.hospitalRepository.OpenApiRepository;
import hannyanggang.catchdoctor.role.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class HospitalService {
    private final HospitalRepository hospitalRepository;
    private final HospitalDetailRepository hospitalDetailRepository;
    private final OpenApiRepository openApiRepository;

    public Hospital register(HospitalRegisterDto registerDto) {
        Hospital hospital = Hospital.builder()
                .id(registerDto.getId())
                .password(registerDto.getPassword())
                .name(registerDto.getName())
                .role(UserRole.USER)
                .build();
        return hospitalRepository.save(hospital);
    }

    public List<Hospital> findAll() {
        return hospitalRepository.findAll();
    }

    public Hospital findHospital(Long hospitalid) {
        return hospitalRepository.findById(hospitalid).orElseThrow(()-> {
            return new IllegalArgumentException("User ID를 찾을 수 없습니다.");
        });
    }

    public Hospital login(LoginRequestDto loginDto){
        //getUsername- 로그인 아이디 가져오기
        Optional<Hospital> optionalHospital = Optional.ofNullable(hospitalRepository.findById(loginDto.getId()));

        if(optionalHospital.isEmpty()){
            return null;
        }

        Hospital hospital=optionalHospital.get();

        if(!hospital.getPassword().equals(loginDto.getPassword())){
            return null;
        }

        return hospital;

    }

    public Hospital getLoginUserByLoginId(String id) {
        if(id == null) return null;

        Optional<Hospital> optionalUser = Optional.ofNullable(hospitalRepository.findById(id));
        return optionalUser.orElse(null);

    }

    public HospitalDetail getHospitalDetailByHospitalId(String hospitalId) {
        Hospital hospital = hospitalRepository.findById(hospitalId);
        HospitalDetail hospitalDetail = hospitalDetailRepository.findByHospital(hospital);
        return hospitalDetail;
    }

    public Hospital connectOpenApi(String hospitalId,String addNum) {
       Hospital hospital = hospitalRepository.findById(hospitalId);
        String hospitalName = hospital.getName();
        OpenApiHospital openapi = openApiRepository.findByAddressAndHospitalName(addNum,hospitalName);
        // openapi가 있는지 확인
        if (openapi == null) {
            throw new RuntimeException("우펀번호가 틀렸습니다.");
        }
        // 이미 연결된 openapi가 존재하는지 확인
        OpenApiHospital existingOpenApi = openApiRepository.findByHospital(hospital);
        if (existingOpenApi != null) {
            throw new RuntimeException("이미 연결된 openapi가 존재합니다.");
        }

        openapi.setHospital(hospital);
        hospital.setOpenApiHospital(openapi);
        return hospitalRepository.save(hospital);
    }


}
