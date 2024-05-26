package hannyanggang.catchdoctor.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import hannyanggang.catchdoctor.entity.SearchHistory;
import hannyanggang.catchdoctor.entity.User;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
