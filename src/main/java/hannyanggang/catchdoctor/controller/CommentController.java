package hannyanggang.catchdoctor.controller;

import hannyanggang.catchdoctor.dto.CommentDto;
import hannyanggang.catchdoctor.entity.User;
import hannyanggang.catchdoctor.repository.UserRepository;
import hannyanggang.catchdoctor.response.Response;
import hannyanggang.catchdoctor.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;

    // 댓글 작성
    @Operation(summary = "댓글 작성", description = "댓글을 작성한다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/comments/write/{boardId}")
    public Response writeComment(@PathVariable("boardId") Long boardId, @RequestBody CommentDto commentDto, Authentication authentication) {
        String userId = authentication.getName();
        User user = userRepository.findById(userId);
        return new Response("성공", "댓글 작성을 완료했습니다.", commentService.writeComment(boardId, commentDto, user));
    }


    // 게시글에 달린 댓글 모두 불러오기
    @Operation(summary = "댓글 불러오기", description = "게시글에 달린 댓글을 모두 불러온다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/comments/{boardId}")
    public Response getComments(@PathVariable("boardId") Long boardId) {
        return new Response("성공", "댓글을 불러왔습니다.", commentService.getComments(boardId));
    }


    // 댓글 삭제
    @Operation(summary = "댓글 삭제", description = "게시글에 달린 댓글을 삭제합니다.")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/comments/delete/{boardId}/{commentId}")
    public Response deleteComment(@PathVariable("boardId") Long boardId, @PathVariable("commentId") Long commentId, Authentication authentication) {
        String userId = authentication.getName();
        User user = userRepository.findById(userId);
        if(commentService.getComment(commentId).getWriter().equals(user.getName())) {
            return new Response("성공", "댓글 삭제 완료", commentService.deleteComment(commentId));
        }

        return new Response("실패", "댓글 작성자가 아닙니다.", null);

    }

}
