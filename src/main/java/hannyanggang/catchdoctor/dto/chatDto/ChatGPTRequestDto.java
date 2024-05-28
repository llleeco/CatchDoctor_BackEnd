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
        this.messages.add(new MessageDto("system", "너는 사용자가 증상에 대해 입력하면 그 증상에 대한 진료과목을 추천해서 진료과목만 json형식으로 보내줘 그치만 증상에 대한 내용이 아니라 다른 이야기를 하면 '죄송합니다. 질문에 대해 이해하지 못했습니다. 증상에 대해서만 이야기 해주세요.'라고 응답해줘"));
        this.messages.add(new MessageDto("user", prompt));
    }
}
