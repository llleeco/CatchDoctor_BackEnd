package hannyanggang.catchdoctor.controller.reservationsController;

import hannyanggang.catchdoctor.exception.CustomValidationException;
import hannyanggang.catchdoctor.service.ReservationsService.CancelReservationsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RestController
@RequestMapping("/reservations/cancel")
public class CancelReservationsController {

    private final CancelReservationsService cancelReservationsService;

    @Operation(summary = "예약 취소", description="병원 예약 취소하기")
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<?> CancelReservations(@PathVariable Long reservationId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Map<String, Object> response = new HashMap<>();
            if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
                // 인증된 사용자의 정보를 활용
                String userId = authentication.getName();

                boolean isCancelled = cancelReservationsService.cancelAppointment(reservationId, userId);
                if (isCancelled) {
                    response.put("status", HttpStatus.OK.value());
                    response.put("id", reservationId);
                    response.put("message", "예약 취소 완료");

                    return ResponseEntity.ok().body(response);
                } else {
                    response.put("status", HttpStatus.BAD_REQUEST.value());
                    response.put("id", reservationId);
                    response.put("message", "예약 취소 실패");

                    return ResponseEntity.badRequest().body(response);
                }
            } else {
                response.put("status", HttpStatus.UNAUTHORIZED.value());
                response.put("message", "유효하지 않은 토큰");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (CustomValidationException e) {
            throw new CustomValidationException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            throw new CustomValidationException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}

