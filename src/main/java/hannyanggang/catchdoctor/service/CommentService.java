package hannyanggang.catchdoctor.service;

import hannyanggang.catchdoctor.dto.CommentDto;
import hannyanggang.catchdoctor.entity.Board;
import hannyanggang.catchdoctor.entity.Comment;
import hannyanggang.catchdoctor.entity.User;
import hannyanggang.catchdoctor.repository.BoardRepository;
import hannyanggang.catchdoctor.repository.CommentRepository;
import hannyanggang.catchdoctor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 댓글 작성하기
    @Transactional
    public CommentDto writeComment(Long boardId, CommentDto commentDto, User user) {
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());



        // 게시판 번호로 게시글 찾기
        Board board = boardRepository.findById(boardId).orElseThrow(() -> {
            return new IllegalArgumentException("게시판을 찾을 수 없습니다.");
        });

        comment.setUser(user);
        comment.setBoard(board);
        comment.setRegDate(LocalDate.now());
        comment.setRegTime(LocalTime.now());
        commentRepository.save(comment);

        return CommentDto.toDto(comment);
    }

    // 댓글 불러오기 (1개)
    @Transactional(readOnly = true)
    public CommentDto getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
            return new IllegalArgumentException("댓글이 존재하지 않습니다.");
        });

        return CommentDto.toDto(comment);
    }



    // 글에 해당하는 전체 댓글 불러오기
    @Transactional(readOnly = true)
    public List<CommentDto> getComments(Long boardId) {
        List<Comment> comments = commentRepository.findAllByBoardId(boardId);
        List<CommentDto> commentDtos = new ArrayList<>();

        comments.forEach(s -> commentDtos.add(CommentDto.toDto(s)));
        return commentDtos;
    }


    // 댓글 삭제하기
    @Transactional
    public String deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> {
            return new IllegalArgumentException("댓글 Id를 찾을 수 없습니다.");
        });
        commentRepository.deleteById(commentId);
        return "삭제 완료";
    }
}


