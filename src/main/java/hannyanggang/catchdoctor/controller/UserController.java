package hannyanggang.catchdoctor.controller;


import hannyanggang.catchdoctor.entity.User;
import hannyanggang.catchdoctor.repository.UserRepository;
import hannyanggang.catchdoctor.response.Response;
import hannyanggang.catchdoctor.response.Response2;
import hannyanggang.catchdoctor.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
@RequiredArgsConstructor
@RestController
@Controller
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Operation(summary = "전체 회원 보기", description = "전체 회원을 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users")
    public Response<?> findAll() {
        return new Response<>("true", "조회 성공", userService.findAll());
    }

    @Operation(summary="유저 찾기", description = "개별 회원을 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{userid}")
    public Response<?> findUser(@PathVariable("userid") Long userid) {
        return new Response<>("true", "조회 성공", userService.findUser(userid));
    }

    //병원 즐겨찾기
    @Operation(summary = "병원 즐겨찾기", description="병원 즐겨찾기 등록")
    @PostMapping("/bookmarks/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response2 bookmarkHospital(@PathVariable Long id){
        return Response2.success(userService.updateBookmarkHospital(id, getPrincipal()));
    }

    @Operation(summary = "모든 즐겨찾기 요청", description="모든 즐겨찾기 요청하기")
    @GetMapping("/bookmarks/all")
    @ResponseStatus(HttpStatus.OK)
    public Response findBookmarkAll(Integer page){
        return new Response("true","리턴 성공",userService.findBookmarkAll());
    }

    @Operation(summary = "나의 즐겨찾기 요청", description="나의 병원 즐겨찾기 요청하기")
    @GetMapping("/bookmarks")
    @ResponseStatus(HttpStatus.OK)
    public Response2 findFavoriteBoards(@RequestParam(defaultValue = "0") Integer page){
        return Response2.success(userService.findBookmarkHospitals(page, getPrincipal()));
    }
    public User getPrincipal(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserid(Long.parseLong(authentication.getName()));
        String loginid = user.getId();
        return userRepository.findByUserid(Long.parseLong(authentication.getName()));

    }
}
