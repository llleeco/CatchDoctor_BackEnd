package hannyanggang.catchdoctor.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.SpringApplication;

@JsonInclude(JsonInclude.Include.NON_NULL) //  null 값을 가지는 필드는, JSON 응답에 포함되지 않음
@Getter
@AllArgsConstructor
public class Success<T> implements Result {
    private T data;
}
