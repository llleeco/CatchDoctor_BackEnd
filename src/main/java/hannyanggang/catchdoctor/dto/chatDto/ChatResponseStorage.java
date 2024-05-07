package hannyanggang.catchdoctor.dto.chatDto;
import hannyanggang.catchdoctor.dto.chatDto.ChatGPTResponseDto;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
@Data
@Component
public class ChatResponseStorage {

    private List<String> recommendedDepartments;

    // recommendedDepartments 값을 설정하는 메서드
    public void setRecommendedDepartments(List<String> recommendedDepartments) {
        this.recommendedDepartments = recommendedDepartments;
    }

    public List<String> getRecommendedDepartments() {
        return recommendedDepartments;
    }


}