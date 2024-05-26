package hannyanggang.catchdoctor.dto;

import hannyanggang.catchdoctor.entity.BookMark;
import lombok.Getter;

@Getter
public class BookmarkListDto {
    private Long bookmarkid;
    private Long hospitalid;
    private Long userid;
    public BookmarkListDto(BookMark bookMark) {
        this.bookmarkid = bookMark.getId();
        this.hospitalid = bookMark.getHospital().getHospitalid();
        this.userid = bookMark.getUser().getUserid();
    }
}
