package hannyanggang.catchdoctor.controller.reservationsController;

import hannyanggang.catchdoctor.dto.reservationsDTO.ReservationsDTO;
import hannyanggang.catchdoctor.exception.CustomValidationException;
import hannyanggang.catchdoctor.service.ReservationsService.ReservationsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RestController
@RequestMapping("/reservations")
public class ReservationsController {
    private final ReservationsService reservationsService;

    @Operation(summary = "병원 예약", description="병원 예약 진행")
    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody ReservationsDTO reservationsDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
                // 인증된 사용자의 정보를 활용


                String userId = authentication.getName();
                return reservationsService.createReservation(reservationsDTO, userId);

            } else {
                throw new CustomValidationException(HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 토큰");
            }


        } catch (CustomValidationException e) {
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("status", e.getStatus());
            errorDetails.put("message", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.valueOf(e.getStatus()))
                    .body(errorDetails);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
