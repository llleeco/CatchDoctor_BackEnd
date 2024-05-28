package hannyanggang.catchdoctor.controller.ChatController;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import hannyanggang.catchdoctor.dto.chatDto.ChatGPTRequestDto;
import hannyanggang.catchdoctor.dto.chatDto.ChatGPTResponseDto;
import hannyanggang.catchdoctor.dto.chatDto.ChatResponseStorage;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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

    @Operation(summary = "챗봇", description = "챗봇 질문 요청")
    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam(name = "prompt") String prompt) {
        // 메시지를 추가하여 OpenAPI에게 요청을 보냄
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 사용자가 제공한 증상에 대한 메시지와 함께 요청을 보냄
//        String message =  prompt + "이러한 증상에 대한 추천 진료과목을 다음과 같은 json 형식으로 보내줘. {\"recommended_departments\": }" ;
        String message = prompt;
        ChatGPTRequestDto request = new ChatGPTRequestDto(model, message);
        HttpEntity<ChatGPTRequestDto> entity = new HttpEntity<>(request, headers);

        // OpenAPI에게 요청을 보내고 응답을 받음
        ChatGPTResponseDto chatGPTResponse = template.postForObject(apiURL, entity, ChatGPTResponseDto.class);

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(chatGPTResponse.getChoices().get(0).getMessage().getContent()).getAsJsonObject();
        if (jsonObject.has("recommended_departments")) {
            // 추천 부서가 포함된 응답 처리
            JsonArray jsonArray = jsonObject.getAsJsonArray("recommended_departments");
            List<String> recommendedDepartments = new ArrayList<>();
            for (JsonElement element : jsonArray) {
                recommendedDepartments.add(element.getAsString());
            }
            responseStorage.setRecommendedDepartments(recommendedDepartments);
            return ResponseEntity.ok(chatGPTResponse.getChoices().get(0).getMessage().getContent());
        } else if (jsonObject.has("fail_message")) {
            // 실패 메시지가 포함된 응답 처리
            JsonObject failResponse = new JsonObject();
            failResponse.addProperty("fail_message", jsonObject.get("fail_message").getAsString());
            return ResponseEntity.ok(failResponse.toString());
        } else {
            // 이해할 수 없는 형식의 응답 처리
            throw new RuntimeException("AI가 처리하지 못했습니다.");
        }
    }
}