package hannyanggang.catchdoctor.controller;

import hannyanggang.catchdoctor.dto.BoardDto;
import hannyanggang.catchdoctor.entity.User;
import hannyanggang.catchdoctor.repository.UserRepository;
import hannyanggang.catchdoctor.response.Response;
import hannyanggang.catchdoctor.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    public Response getBoard(@PathVariable("id") Long id) {
        return new Response("성공", "개별 게시물 리턴", boardService.getBoard(id));
    }
    // 게시글 작성
    @Operation(summary = "게시글 작성", description = "게시글을 작성한다.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/boards/write")
    public Response write(@RequestBody BoardDto boardDto, Authentication authentication) {
        String userId = authentication.getName();
        User user = userRepository.findById(userId);
        return new Response("성공", "글 작성 성공", boardService.write(boardDto, user));
    }
    // 게시글 수정
    @Operation(summary = "게시글 수정", description = "게시글을 수정한다.")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/boards/update/{id}")
    public Response edit(@RequestBody BoardDto boardDto, @PathVariable("id") Long id, Authentication authentication) {
        String userId = authentication.getName();
        User user = userRepository.findById(userId);
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
    public Response delete(@PathVariable("id") Long id, Authentication authentication) {
        String userId = authentication.getName();
        User user = userRepository.findById(userId);
        if (user.getName().equals(boardService.getBoard(id).getWriter())) {
            // 로그인된 유저가 글 작성자와 같다면
            boardService.delete(id); // 이 메소드는 반환값이 없으므로 따로 삭제 수행해주고, 리턴에는 null을 넣어줌
            return new Response("성공", "글 삭제 성공", null);
        } else {
            return new Response("실패", "본인 게시물만 삭제할 수 있습니다.", null);
        }
    }
    // 업로드
    @PostMapping("/boards/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        String uploadImage = boardService.uploadImage(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }

    // 다운로드
    @GetMapping("/boards/download/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable("fileName") String fileName) {
        byte[] downloadImage = boardService.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(downloadImage);
    }
}

