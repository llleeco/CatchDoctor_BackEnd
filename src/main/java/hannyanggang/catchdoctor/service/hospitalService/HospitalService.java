package hannyanggang.catchdoctor.service.hospitalService;

import hannyanggang.catchdoctor.dto.LoginRequestDto;
import hannyanggang.catchdoctor.dto.hospitalDto.HospitalRegisterDto;
import hannyanggang.catchdoctor.entity.Hospital;
import hannyanggang.catchdoctor.repository.hospitalRepository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class HospitalService {
    private final HospitalRepository hospitalRepository;

    public Hospital register(HospitalRegisterDto registerDto) {
        Hospital hospital = Hospital.builder()
                .id(registerDto.getId())
                .password(registerDto.getPassword())
                .name(registerDto.getName())
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
}
