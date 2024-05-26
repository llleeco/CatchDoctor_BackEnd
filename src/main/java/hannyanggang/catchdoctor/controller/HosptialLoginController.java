package hannyanggang.catchdoctor.controller;

import hannyanggang.catchdoctor.dto.LoginRequestDto;
import hannyanggang.catchdoctor.dto.hospitalDto.HospitalLoginResponseDto;
import hannyanggang.catchdoctor.dto.hospitalDto.HospitalRegisterDto;
import hannyanggang.catchdoctor.entity.Hospital;
import hannyanggang.catchdoctor.response.Response;
import hannyanggang.catchdoctor.service.hospitalService.HospitalService;
import hannyanggang.catchdoctor.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jwt-hospital-login")
public class HosptialLoginController {

    private final HospitalService hospitalService;

    @Operation(summary = "병원 회원가입", description="병원 회원가입 진행")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/hospital/join")
    public Response<?> hospitaljoin(@RequestBody HospitalRegisterDto joinRequest) {
        return new Response<>("true", "가입 성공", hospitalService.register(joinRequest));
    }

    @Operation(summary = "병원 로그인", description="병원 로그인 진행")
    @PostMapping("/login")
    public Response<HospitalLoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {

        Hospital hospital = hospitalService.login(loginRequest);

        // 로그인 아이디나 비밀번호가 틀린 경우 global error return
        if (hospital == null) {
            return new Response<>("false", "로그인 아이디 또는 비밀번호가 틀렸습니다.", null);
        }

        // 로그인 성공 => Jwt Token 발급

        String secretKey = "my-secret-key-2394084098";
        long expireTimeMs = 1000 * 60 * 60;     // Token 유효 시간 = 60분
        //getUser_id-로그인 아이디
        String jwtToken = JwtTokenUtil.createToken(hospital.getId(), secretKey, expireTimeMs);

        // 로그인 응답 DTO 생성
        HospitalLoginResponseDto loginResponse = new HospitalLoginResponseDto(jwtToken, hospital.getHospitalid());

        return new Response<>("true", "로그인 성공",
                loginResponse);
    }
}
