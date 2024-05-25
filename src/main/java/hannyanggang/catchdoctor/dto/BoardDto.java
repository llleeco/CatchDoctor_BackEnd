package hannyanggang.catchdoctor.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import hannyanggang.catchdoctor.entity.Board;
import hannyanggang.catchdoctor.util.ImageUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {
    private Long boardId;
    private String title;
    private String content;
    private String writer;
    private LocalDate regDate;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime regTime;
    private byte[] mainImage;

    public static BoardDto toDto(Board board) {
        return new BoardDto(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getUser().getName(),
                board.getRegDate(),
                board.getRegTime(),
                ImageUtils.decompressImage(board.getBoardImage1())
        );
    }

}
