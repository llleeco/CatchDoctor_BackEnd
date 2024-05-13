package hannyanggang.catchdoctor.controller;


import hannyanggang.catchdoctor.response.Response;
import hannyanggang.catchdoctor.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
@RequiredArgsConstructor
@RestController
@Controller
public class UserController {

    private final UserService userService;

    @Operation(summary = "전체 회원 보기", description = "전체 회원을 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users")
    public Response<?> findAll() {
        return new Response<>("true", "조회 성공", userService.findAll());
    }

    @Operation(summary="유저 찾기", description = "개별 유저 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{userid}")
    public Response<?> findUser(@PathVariable("userid") Long userid) {
        return new Response<>("true", "조회 성공", userService.findUser(userid));
    }
}
