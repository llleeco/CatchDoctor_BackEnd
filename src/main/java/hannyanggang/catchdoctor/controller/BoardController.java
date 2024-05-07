package hannyanggang.catchdoctor.controller;

import hannyanggang.catchdoctor.dto.BoardDto;
import hannyanggang.catchdoctor.entity.User;
import hannyanggang.catchdoctor.repository.UserRepository;
import hannyanggang.catchdoctor.response.Response;
import hannyanggang.catchdoctor.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;
    private final UserRepository userRepository;


    // 전체 게시글 조회
    @Operation(summary = "전체 게시글 보기", description = "전체 게시글을 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/boards")
    public Response getBoards() {
        return new Response("성공", "전체 게시물 리턴", boardService.getBoards());
    }
    // 개별 게시글 조회
    @Operation(summary = "개별 게시글 보기", description = "개별 게시글 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/boards/{id}")
    public Response getBoard(@PathVariable("id") Integer id) {
        return new Response("성공", "개별 게시물 리턴", boardService.getBoard(id));
    }
    // 게시글 작성
    @Operation(summary = "게시글 작성", description = "게시글을 작성한다.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/boards/write")
    public Response write(@RequestBody BoardDto boardDto, Authentication authentication) {
        String userId = authentication.getName();
        String Id = userRepository.findUsernameByUserId(userId);
        User user = userRepository.findById(Id);
        return new Response("성공", "글 작성 성공", boardService.write(boardDto, user));
    }
    // 게시글 수정
    @Operation(summary = "게시글 수정", description = "게시글을 수정한다.")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/boards/update/{id}")
    public Response edit(@RequestBody BoardDto boardDto, @PathVariable("id") Integer id, Authentication authentication) {
        // 원래 로그인을 하면, User 정보는 세션을 통해서 구하고 주면 되지만,
        // 지금은 핵심 개념을 알기 위해서, JWT 로그인은 생략하고, 임의로 findById 로 유저 정보를 넣어줬습니다.

        // 추후에 JWT 로그인을 배우고나서 적용해야할 것

        // 1. 현재 요청을 보낸 유저의 JWT 토큰 정보(프론트엔드가 헤더를 통해 보내줌)를 바탕으로
        // 현재 로그인한 유저의 정보가 PathVariable로 들어오는 BoardID 의 작성자인 user정보와 일치하는지 확인하고
        // 맞으면 아래 로직 수행, 틀리면 다른 로직(ResponseFail 등 커스텀으로 만들어서) 수행
        // 이건 if문으로 처리할 수 있습니다. * 이 방법 말고 service 내부에서 확인해도 상관 없음

        String userId = authentication.getName();
        String Id = userRepository.findUsernameByUserId(userId);
        User user = userRepository.findById(Id);
        if (user.getName().equals(boardService.getBoard(id).getWriter())) {
            // 로그인된 유저의 글이 맞다면
            return new Response("성공", "글 수정 성공", boardService.update(id, boardDto));
        } else {
            return new Response("실패", "본인 게시물만 수정할 수 있습니다.", null);

        }
    }
    // 게시글 삭제
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제한다.")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/boards/delete/{id}")
    public Response delete(@PathVariable("id") Integer id, Authentication authentication) {
        // 이것도 마찬가지로, JWT(로그인 관련) 공부를 하고나서 현재 이 요청을 보낸 로그인된 유저의 정보가
        // 게시글의 주인인지 확인하고, 맞으면 삭제 수행 후 리턴해주고, 틀리면 에러 리턴해주면 됩니다.

        String userId = authentication.getName();
        String Id = userRepository.findUsernameByUserId(userId);
        User user = userRepository.findById(Id);
        if (user.getName().equals(boardService.getBoard(id).getWriter())) {
            // 로그인된 유저가 글 작성자와 같다면
            boardService.delete(id); // 이 메소드는 반환값이 없으므로 따로 삭제 수행해주고, 리턴에는 null을 넣어줌
            return new Response("성공", "글 삭제 성공", null);
        } else {
            return new Response("실패", "본인 게시물만 삭제할 수 있습니다.", null);
        }
    }
}

