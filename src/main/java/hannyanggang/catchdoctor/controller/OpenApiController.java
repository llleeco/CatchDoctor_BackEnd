package hannyanggang.catchdoctor.controller;

import hannyanggang.catchdoctor.entity.OpenApiHospital;
import hannyanggang.catchdoctor.repository.OpenApiRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@RequiredArgsConstructor
@RestController
public class OpenApiController {

    private final OpenApiRepository openApiRepository;

    @RequestMapping("/api")
    public String save() throws IOException {



        String result = "";

        try {
            // <주의> 전체 데이터인 77447개 데이터를 받아오므로 엄청 오래걸림. url 뒤쪽 numOfRows으로 가져올 데이터 수 조정 가능.
            String urlStr = "http://apis.data.go.kr/B551182/hospInfoServicev2/getHospBasisList?ServiceKey=%2B11swrr4a7rdAi7MbD6nyiiDgg4ySAlG6YHekwCyrVZUMFN7OcWIzD9c33gQJy%2Birg0bthGv5PkRjuEwDwJHCw%3D%3D&numOfRows=500&_type=json";
            URL url = new URL(urlStr);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

            result = br.readLine();
            System.out.println(result);

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

            JSONObject parseResponse = (JSONObject) jsonObject.get("response");
            JSONObject parseBody = (JSONObject) parseResponse.get("body");
            JSONObject parseItems = (JSONObject) parseBody.get("items");
            JSONArray array = (JSONArray) parseItems.get("item");

            for(int i = 0; i < array.size(); i++) {
                JSONObject tmp = (JSONObject) array.get(i);
                String yadmNm = (String) tmp.get("yadmNm");
                String addr = (String) tmp.get("addr");
                String telno = (String) tmp.get("telno");
                String postNo = parseStringValue(tmp.get("postNo"));
                Double xPos = parseDoubleValue(tmp.get("XPos"));
                Double yPos = parseDoubleValue(tmp.get("YPos"));

                OpenApiHospital openApiHospital = new OpenApiHospital(
                        i + (long)1,
                        yadmNm,
                        addr,
                        telno,
                        postNo,
                        xPos,
                        yPos
                );
                openApiRepository.save(openApiHospital);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "openapi 불러오기 성공";
    }

    private Double parseDoubleValue(Object value) { // double로 형변환 하는 로직.
        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof String) {
            String stringValue = (String) value;
            if (!stringValue.isEmpty()) {
                try {
                    return Double.parseDouble(stringValue);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private String parseStringValue(Object value) { // string으로 형변환 하는 로직.
        if (value instanceof Long) {
            return String.valueOf(value);
        } else if (value instanceof String) {
            return (String) value;
        } else {
            return null;
        }
    }

}