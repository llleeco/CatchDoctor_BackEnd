package hannyanggang.catchdoctor.dto;

import hannyanggang.catchdoctor.entity.SearchHistory;
import hannyanggang.catchdoctor.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchHistoryDto {

    private String keyword;

    private LocalDate searchDate;

    private LocalTime searchTime;

    private User user;

    public SearchHistoryDto(SearchHistory searchHistory) {
        this.keyword = searchHistory.getKeyword();
        this.searchDate = searchHistory.getSearchDate();
        this.searchTime = searchHistory.getSearchTime();
        this.user = searchHistory.getUser();
    }
}
