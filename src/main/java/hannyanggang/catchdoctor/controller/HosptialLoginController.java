package hannyanggang.catchdoctor.controller;

import hannyanggang.catchdoctor.dto.LoginRequestDto;
import hannyanggang.catchdoctor.dto.hospitalDto.HospitalRegisterDto;
import hannyanggang.catchdoctor.dto.userDto.UserRegisterDto;
import hannyanggang.catchdoctor.entity.Hospital;
import hannyanggang.catchdoctor.entity.User;
import hannyanggang.catchdoctor.response.Response;
import hannyanggang.catchdoctor.service.UserService;
import hannyanggang.catchdoctor.service.hospitalService.HospitalService;
import hannyanggang.catchdoctor.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jwt-hospital-login")
public class HosptialLoginController {

    private final HospitalService hospitalService;

    @Operation(summary = "병원 회원가입", description="병원 회원가입 진행")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/hospital/join")
    public Response<?> hospitaljoin(@RequestBody HospitalRegisterDto joinRequest) {

        // loginId 중복 체크
//        if(HospitalService.checkLoginIdDuplicate(joinRequest.getUserId())) {
//            return "로그인 아이디가 중복됩니다.";
//            return new Response<>("false", "가입 실패", userService.fail);
//
//
//        }
        return new Response<>("true", "가입 성공", hospitalService.register(joinRequest));
    }

    @Operation(summary = "병원 로그인", description="병원 로그인 진행")
    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDto loginRequest) {

        Hospital hospital = hospitalService.login(loginRequest);

        // 로그인 아이디나 비밀번호가 틀린 경우 global error return
        if(hospital == null) {
            return"로그인 아이디 또는 비밀번호가 틀렸습니다.";
        }

        // 로그인 성공 => Jwt Token 발급

        String secretKey = "my-secret-key-2394084098";
        long expireTimeMs = 1000 * 60 * 60;     // Token 유효 시간 = 60분
        //getUser_id-로그인 아이디
        String jwtToken = JwtTokenUtil.createToken(hospital.getId(), secretKey, expireTimeMs);

        return jwtToken;
    }
}
