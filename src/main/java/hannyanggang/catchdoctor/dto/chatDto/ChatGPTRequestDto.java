package hannyanggang.catchdoctor.dto.chatDto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatGPTRequestDto {
    private String model;
    private List<MessageDto> messages;

    public ChatGPTRequestDto(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new MessageDto("user", prompt));
    }
}
