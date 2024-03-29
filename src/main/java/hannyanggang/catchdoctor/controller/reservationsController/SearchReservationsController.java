package hannyanggang.catchdoctor.controller.reservationsController;

import hannyanggang.catchdoctor.dto.reservationsDTO.ReservationsDTO;
import hannyanggang.catchdoctor.exception.CustomValidationException;
import hannyanggang.catchdoctor.service.ReservationsService.SearchReservationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Controller
@RestController
@RequestMapping("/reservations")
public class SearchReservationsController {

    private final SearchReservationsService searchReservationsService;

    @GetMapping("/search")
    public ResponseEntity<?> searchReservation(@RequestParam(required = false) String hospitalName,
                                               @RequestParam(required = false) String date)
    {

        List<ReservationsDTO> reservationsDTOS;
        Map<String, Object> errorBody = new HashMap<>();
        LocalDate localDate = null;

        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
                // 인증된 사용자의 정보를 활용
                String userId = authentication.getName();

                if (hospitalName != null && !Pattern.matches("^[가-힣]+$", hospitalName)) {
                    System.out.println("여기?" + !Pattern.matches("^[가-힣]+$", hospitalName));
                    System.out.println(hospitalName);
                    throw new CustomValidationException(HttpStatus.BAD_REQUEST.value(), "잘못된 입력(한글만 입력)");
                }
                if (date != null){
                    try {
                        localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    } catch (DateTimeParseException e) {
                        throw new CustomValidationException(HttpStatus.BAD_REQUEST.value(), "잘못된 형식 (yyyy-MM-dd)");
                    }
                }
                if(hospitalName == null || hospitalName.isEmpty())
                {
                    hospitalName = null;
                } else {
                    //공백 없애기
                    hospitalName = hospitalName.replaceAll("\\s+", "");
                }
                reservationsDTOS = searchReservationsService.searchAppointments(userId, hospitalName, localDate);

                return ResponseEntity.ok(reservationsDTOS);

            } else {
                throw new CustomValidationException(HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 토큰");
            }


        } catch (CustomValidationException e) {
            // CustomValidationException에 대한 특별한 처리
            errorBody.put("status", HttpStatus.BAD_REQUEST.value());
            errorBody.put("message", "입력 형식 오류");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errorBody);
        } catch(Exception e)
        {
            errorBody.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorBody.put("message", "검색 오류");

            // 예외 발생 시 클라이언트에게 오류 메시지 반환
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorBody);
        }
    }

}
