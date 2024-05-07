package hannyanggang.catchdoctor.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL) //  null 값을 가지는 필드는, JSON 응답에 포함되지 않음
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Response2 {
    private boolean success;
    private int code;
    private Result result;

    public static Response2 success() {
        return new Response2(true, 0, null);
    }

    public static <T> Response2 success(T data) {
        return new Response2(true, 0, new Success<>(data));
    }

    public static Response2 failure(int code, String msg) {
        return new Response2(false, code, new Failure(msg));
    }
}
