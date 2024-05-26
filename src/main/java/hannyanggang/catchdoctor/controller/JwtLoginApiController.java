package hannyanggang.catchdoctor.controller;

import hannyanggang.catchdoctor.dto.LoginRequestDto;
import hannyanggang.catchdoctor.dto.userDto.UserRegisterDto;
import hannyanggang.catchdoctor.entity.User;
import hannyanggang.catchdoctor.response.Response;
import hannyanggang.catchdoctor.service.UserService;
import hannyanggang.catchdoctor.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jwt-login")
public class JwtLoginApiController {

    private final UserService userService;

    @Operation(summary = "유저 회원가입", description="유저 회원가입 진행")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/user/join")
    public Response<?> userjoin(@RequestBody UserRegisterDto joinRequest) {
       return new Response<>("true", "가입 성공", userService.register(joinRequest));
    }

    @Operation(summary = "유저 로그인", description="유저 로그인 진행")
    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDto loginRequest) {

        User user = userService.login(loginRequest);

        // 로그인 아이디나 비밀번호가 틀린 경우 global error return
        if(user == null) {
            return"로그인 아이디 또는 비밀번호가 틀렸습니다.";
        }

        // 로그인 성공 => Jwt Token 발급

        String secretKey = "my-secret-key-2394084098";
        long expireTimeMs = 1000 * 60 * 60;     // Token 유효 시간 = 60분
                                                     //getUser_id-로그인 아이디
        String jwtToken = JwtTokenUtil.createToken(user.getId(), secretKey, expireTimeMs);

        return jwtToken;
    }

    @Operation(summary = "나의 정보", description="로그인한 유저의 정보를 찾기")
    @GetMapping("/info")
    public String userInfo(Authentication auth) {
        User loginUser = userService.getLoginUserByLoginId(auth.getName());

        return String.format("loginId : %s\nname : %s\nrole : %s",
                loginUser.getId(), loginUser.getName(), loginUser.getRole().name());
    }
}
