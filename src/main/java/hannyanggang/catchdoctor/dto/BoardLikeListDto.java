package hannyanggang.catchdoctor.dto;

import hannyanggang.catchdoctor.entity.BoardLike;
import hannyanggang.catchdoctor.entity.BookMark;
import lombok.Getter;

@Getter
public class BoardLikeListDto {
    private Long boardlikeid;
    private Long boardid;
    private Long userid;

    public BoardLikeListDto(BoardLike boardLike) {
        this.boardlikeid = boardLike.getId();
        this.boardid = boardLike.getBoard().getId();
        this.userid = boardLike.getUser().getUserid();
    }
}
