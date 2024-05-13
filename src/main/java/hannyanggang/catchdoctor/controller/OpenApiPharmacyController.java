package hannyanggang.catchdoctor.controller;

import hannyanggang.catchdoctor.entity.OpenApiPharmacy;
import hannyanggang.catchdoctor.repository.OpenApiPharmacyRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@RequiredArgsConstructor
@RestController
public class OpenApiPharmacyController {
    private final OpenApiPharmacyRepository openApiPharmacyRepository;

    @Operation(summary = "약국 OPENAPI 저장", description="약국 OPENAPI DB에 저장하기")
    @GetMapping("/apipharmacy")
    public String save() throws IOException {
        String result = "";

        try {
            //
            String urlStr = "https://apis.data.go.kr/B552657/ErmctInsttInfoInqireService/getParmacyBassInfoInqire?serviceKey=%2B11swrr4a7rdAi7MbD6nyiiDgg4ySAlG6YHekwCyrVZUMFN7OcWIzD9c33gQJy%2Birg0bthGv5PkRjuEwDwJHCw%3D%3D&_type=json&numOfRows=1000";
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
                String dutyName = (String) tmp.get("dutyName");
                String dutyAddr = (String) tmp.get("dutyAddr");
                String dutyTel1 = (String) tmp.get("dutyTel1");
                String dutyTime1s = parseStringValue(tmp.get("dutyTime1s"));
                String dutyTime1c = parseStringValue(tmp.get("dutyTime1c"));
                String dutyTime2s = parseStringValue(tmp.get("dutyTime2s"));
                String dutyTime2c = parseStringValue(tmp.get("dutyTime2c"));
                String dutyTime3s = parseStringValue(tmp.get("dutyTime3s"));
                String dutyTime3c = parseStringValue(tmp.get("dutyTime3c"));
                String dutyTime4s = parseStringValue(tmp.get("dutyTime4s"));
                String dutyTime4c = parseStringValue(tmp.get("dutyTime4c"));
                String dutyTime5s = parseStringValue(tmp.get("dutyTime5s"));
                String dutyTime5c = parseStringValue(tmp.get("dutyTime5c"));
                String dutyTime6s = parseStringValue(tmp.get("dutyTime6s"));
                String dutyTime6c = parseStringValue(tmp.get("dutyTime6c"));
                String dutyTime7s = parseStringValue(tmp.get("dutyTime7s"));
                String dutyTime7c = parseStringValue(tmp.get("dutyTime7c"));
                String dutyTime8s = parseStringValue(tmp.get("dutyTime8s"));
                String dutyTime8c = parseStringValue(tmp.get("dutyTime8c"));
                Double wgs84Lon = parseDoubleValue(tmp.get("wgs84Lon"));
                Double wgs84Lat = parseDoubleValue(tmp.get("wgs84Lat"));

                OpenApiPharmacy openApiPharmacy = new OpenApiPharmacy(
                        i + (long)1,
                        dutyName,
                        dutyAddr,
                        dutyTel1,
                        dutyTime1s,
                        dutyTime1c,
                        dutyTime2s,
                        dutyTime2c,
                        dutyTime3s,
                        dutyTime3c,
                        dutyTime4s,
                        dutyTime4c,
                        dutyTime5s,
                        dutyTime5c,
                        dutyTime6s,
                        dutyTime6c,
                        dutyTime7s,
                        dutyTime7c,
                        dutyTime8s,
                        dutyTime8c,
                        wgs84Lon,
                        wgs84Lat
                );
                openApiPharmacyRepository.save(openApiPharmacy);
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
