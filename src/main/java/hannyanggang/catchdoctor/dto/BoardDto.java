package hannyanggang.catchdoctor.dto;

import hannyanggang.catchdoctor.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {
    private String title;
    private String content;
    private String writer;

    public static BoardDto toDto(Board board) {
        return new BoardDto(
                board.getTitle(),
                board.getContent(),
                board.getUser().getName());
    }

}
