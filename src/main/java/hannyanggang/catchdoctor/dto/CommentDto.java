package hannyanggang.catchdoctor.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import hannyanggang.catchdoctor.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private String writer;
    private LocalDate regDate;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime regTime;

    public static CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getName(),
                comment.getRegDate(),
                comment.getRegTime()
        );
    }
}
