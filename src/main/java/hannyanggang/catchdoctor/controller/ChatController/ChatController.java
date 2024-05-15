package hannyanggang.catchdoctor.controller.ChatController;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import hannyanggang.catchdoctor.dto.chatDto.ChatGPTRequestDto;
import hannyanggang.catchdoctor.dto.chatDto.ChatGPTResponseDto;
import hannyanggang.catchdoctor.dto.chatDto.ChatResponseStorage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bot")
public class ChatController {
    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

    @Autowired
    private ChatResponseStorage responseStorage;

    @Operation(summary = "챗봇", description="챗봇 질문 요청")
    @GetMapping("/chat")
    public String chat(@RequestParam(name = "prompt") String prompt) {
        // 메시지를 추가하여 OpenAPI에게 요청을 보냄
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 사용자가 제공한 증상에 대한 메시지와 함께 요청을 보냄
        String message =  prompt + "이러한 증상에 대한 추천 진료과목을 다음과 같은 json 형식으로 보내줘. {\"recommended_departments\": }" ;
        ChatGPTRequestDto request = new ChatGPTRequestDto(model, message);
        HttpEntity<ChatGPTRequestDto> entity = new HttpEntity<>(request, headers);

        // OpenAPI에게 요청을 보내고 응답을 받음
        ChatGPTResponseDto chatGPTResponse = template.postForObject(apiURL, entity, ChatGPTResponseDto.class);

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(chatGPTResponse.getChoices().get(0).getMessage().getContent()).getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("recommended_departments");

// recommendedDepartments를 저장하기 위한 리스트
        List<String> recommendedDepartments = new ArrayList<>();

// JSON 배열의 각 요소를 문자열로 변환하여 리스트에 추가
        for (JsonElement element : jsonArray) {
            recommendedDepartments.add(element.getAsString());
        }

        responseStorage.setRecommendedDepartments(recommendedDepartments);

        // OpenAPI의 응답에서 첫 번째 선택지의 메시지를 가져와서 반환
        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }
//    @GetMapping("/chat")
//    public String chat(@RequestParam(name = "prompt") String prompt) {
//        ChatGPTRequestDto request = new ChatGPTRequestDto(model, prompt);
//        ChatGPTResponseDto chatGPTResponse = template.postForObject(apiURL, request, ChatGPTResponseDto.class);
//        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
//    }
}
