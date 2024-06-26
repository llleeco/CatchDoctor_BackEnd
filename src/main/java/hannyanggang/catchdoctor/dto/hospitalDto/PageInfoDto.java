package hannyanggang.catchdoctor.dto.hospitalDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PageInfoDto {
    private int totalPage; // 전체 페이지 수
    private int nowPage; // 현재 페이지
    private int numberOfElements; // 현재 페이지에 나올 데이터 수
    private boolean isNext; // 다음 페이지 존재 여부

    public PageInfoDto(Page result) {
        this.totalPage = result.getTotalPages();
        this.nowPage = result.getNumber();
        this.numberOfElements = result.getNumberOfElements();
        this.isNext = result.hasNext();
    }
}
