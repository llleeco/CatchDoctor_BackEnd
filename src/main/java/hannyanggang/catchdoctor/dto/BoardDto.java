package hannyanggang.catchdoctor.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import hannyanggang.catchdoctor.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {
    private String title;
    private String content;
    private String writer;
    private LocalDate regDate;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime regTime;

    public static BoardDto toDto(Board board) {
        return new BoardDto(
                board.getTitle(),
                board.getContent(),
                board.getUser().getName(),
                board.getRegDate(),
                board.getRegTime());
    }

}
