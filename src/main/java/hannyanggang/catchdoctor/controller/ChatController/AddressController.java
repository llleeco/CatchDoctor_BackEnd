package hannyanggang.catchdoctor.controller.ChatController;

import hannyanggang.catchdoctor.dto.chatDto.ChatResponseStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

@RestController
public class AddressController {

    @Autowired
    private ChatResponseStorage responseStorage;

    @GetMapping("/chat/recommend")
    public String getHospitalfromKaoKaoAPI(@RequestParam(name="longitude") String longitude,
                                           @RequestParam(name="latitude") String latitude){
        String apiKey = "0c09aa556dac483956e1f9d7c6c922a8";
        String apiURL = "https://dapi.kakao.com/v2/local/search/keyword.json";
        String jsonString = null;

        try{
            String addr = apiURL + "?query=" + URLEncoder.encode(responseStorage.getRecommendedDepartments().get(0), "UTF-8")
                    + "&category_group_code=HP8" + "&x=" + longitude + "&y=" + latitude + "&radius=20000" + "&sort=distance";

            URL url = new URL(addr);

            URLConnection conn = url.openConnection();

            conn.setRequestProperty("Authorization", "KakaoAK " + apiKey);

            BufferedReader rd = null;
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            StringBuffer docJson = new StringBuffer();
            String line;

            while((line=rd.readLine()) != null){
                docJson.append(line);
            }

            jsonString = docJson.toString();
            rd.close();



        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return  jsonString;
    }
}
